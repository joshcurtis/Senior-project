extern crate gtk;

mod ini_data;
mod gtk_helper;

use std::rc::Rc;
use std::cell::RefCell;
use std::sync::{Arc,Mutex};
use gtk::traits::*;
use gtk::signal::Inhibit;


fn create_ini_file( _ : gtk::Button) {
    let params = Arc::new(Mutex::new(Vec::new()));
    let params_ref = params.clone();

    let add_param_window = create_default_window("Add Parameter");
    let add_param_dialog = gtk::Dialog::with_buttons("Add Parameter",
                                                         Some(&add_param_window),
                                                         gtk::DialogFlags::empty(),
                                                         [("Add", gtk::ResponseType::Accept),
                                                          ("Cancel", gtk::ResponseType::Cancel)]);
    add_param_dialog.set_window_position(gtk::WindowPosition::Center);

    // Create a parameter entry box for creating new parameters
    let (add_param_entry, add_param_label) = gtk_helper::build_entry_and_label("Parameter Name".to_string());
    let param_entry_box = add_param_dialog.get_content_area();
    param_entry_box.add(&add_param_label);
    param_entry_box.add(&add_param_entry);

    let window = Arc::new(Mutex::new(create_default_window("Create INI File")));
    let window_ref = window.clone();

    let add_param_button = gtk::Button::new_with_label("Add Parameter").unwrap();

    let disp = gtk::Box::new(gtk::Orientation::Vertical, 10).unwrap();
    disp.pack_start(&add_param_button, false, false, 10);

    window.lock().unwrap().add(&disp);

    let display = Arc::new(Mutex::new(disp));
    let display_ref = display.clone();

    // Copy the window so it can be modified inside and outside the closure
    add_param_button.connect_clicked(move |_| {
        add_param_dialog.show_all();
        if add_param_dialog.run() == gtk::ResponseType::Accept as i32 {
            let s = add_param_entry.get_text().unwrap();
            println!("{}", s);
            params_ref.lock().unwrap().push(s.clone());

            let (param_entries, param_labels) =  gtk_helper::build_entry_and_label(s.to_string());

            let entry_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
            entry_box.pack_start(&param_labels, false, false, 10);
            entry_box.pack_start(&param_entries, false, false, 0);

            let display = display_ref.lock().unwrap();
            display.pack_start(&entry_box, false, false, 10);

            let window = window_ref.lock().unwrap();
            //window.add(&display);
            window.show_all();
        }
        add_param_dialog.hide();
    });
    window.lock().unwrap().show_all();
}

fn create_default_window(title: &str) -> gtk::Window {
    let window = gtk::Window::new(gtk::WindowType::Toplevel).unwrap();
    window.set_window_position(gtk::WindowPosition::Center);
    window.set_title(title);
    window.connect_delete_event(|window, _| {
        window.destroy();
        return Inhibit(true);
    });

    return window;
}

fn gui_main() {

    // Make sure GTK loads
    gtk::init().ok().expect("Unable to load GTK");

    // Create the main display
    let display = gtk::Box::new(gtk::Orientation::Vertical,10).unwrap();

    // Initialize some example INI information
    let empty = "".to_string();
    let mut sample_ini = ini_data::IniData::new(vec![
        ("Alpha".to_string(), vec![("Temperature".to_string(), empty.clone()),
                                   ("Operating System".to_string(), empty.clone())]),
        ("Beta".to_string(),  vec![("Height".to_string(), empty.clone()),
                                   ( "Width".to_string(), empty.clone())])
    ]);

    display.pack_start(&sample_ini.get_entry_boxes(), false, false, 10);

    // Create a Rc (reference can be cloned) from
    // a RefCell (which can give access to mutable data)
    let main_ini_ref = Rc::new(RefCell::new(sample_ini));
    {
        // Clone the RC<RefCell<IniData>> so the closure can own it
        let cloned_ref = main_ini_ref.clone();

        // Add button click handling for the INI generator button
        let borrowed_button = &(*main_ini_ref).borrow().generate_button;
        &borrowed_button.connect_clicked(move |_| {
            let ini_ref = (*cloned_ref).borrow_mut();

            for i in (0..(*ini_ref).section_values_vec.len()) {
                println!("[{}]", (*ini_ref).section_values_vec[i].0);
                for j in (0..(*ini_ref).section_values_vec[i].1.len()) {
                    let label = ((*ini_ref).section_values_vec[i].1)[j].clone();
                    let input = (*ini_ref).entries_vec[i][j].get_text().unwrap();
                    println!("{}={}", label.0, input);
                }
                println!("");
            }
        });

        display.pack_start(borrowed_button, false, false, 0);
    }

    {
        // Clone the RC<RefCell<IniData>> so the closure can own it
        let cloned_ref = main_ini_ref.clone();

        // Create a button and associate it with a file chooser dialog
        let file_window = gtk::Window::new(gtk::WindowType::Toplevel).unwrap();
        let file_chooser =  gtk::FileChooserDialog::new("Load INI file",
            Some(&file_window), gtk::FileChooserAction::Open,
            [("Ok", gtk::ResponseType::Accept), ("Cancel", gtk::ResponseType::Cancel)]);

        // Add button click handling for the load INI button
        let borrowed_button = &(*main_ini_ref).borrow().open_button;
        borrowed_button.connect_clicked(move |_| {
            file_chooser.show_all();
            if file_chooser.run() == gtk::ResponseType::Accept as i32 {
                let filename = file_chooser.get_filename().unwrap();

                // TODO: Load data here using rust-ini
                println!("{}", filename);
            }
            file_chooser.hide();
        });

        display.pack_start(borrowed_button, false, false, 0);
    }

    // Create a button and associate it with a window for creating new INI files
    let create_ini_button = gtk::Button::new_with_label("Create INI file").unwrap();
    create_ini_button.connect_clicked(create_ini_file);
    display.pack_start(&create_ini_button, false, false, 0);

    // Create and set up the main window
    let window = gtk::Window::new(gtk::WindowType::Toplevel).unwrap();
    window.set_title("3D Printer INI Generator");
    window.set_border_width(10);
    window.set_window_position(gtk::WindowPosition::Center);
    window.set_default_size(350, 400);
    window.connect_delete_event(|_, _| {
        gtk::main_quit();
        Inhibit(false)
    });
    window.add(&display);

    // Open the window and run the GTK main
    window.show_all();
    gtk::main();
}


fn main() {
    gui_main();
}
