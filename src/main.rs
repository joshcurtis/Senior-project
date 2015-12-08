extern crate gtk;
extern crate ini;

mod gtk_helper;
mod ini_data;

use gtk::signal::Inhibit;
use gtk::traits::*;
use ini::Ini;
use std::sync::{Arc,Mutex};

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

fn edit_ini_file() {
    // Run a file choosing dialog
    let file_window = gtk::Window::new(gtk::WindowType::Toplevel).unwrap();
    let file_chooser =  gtk::FileChooserDialog::new(
        "Load INI file",
        Some(&file_window),
        gtk::FileChooserAction::Open,
        [("Ok", gtk::ResponseType::Accept),
         ("Cancel", gtk::ResponseType::Cancel)]);

    // Get the INI filename
    let filename_opt;
    if file_chooser.run() == gtk::ResponseType::Accept as i32 {
        let f = file_chooser.get_filename().unwrap();
        filename_opt = Some(f.clone());
        println!("Chosen File: {}", f);
    } else {
        println!("Returning to main menu");
        file_chooser.destroy();
        return;
    }
    file_chooser.destroy();

    let filename = filename_opt.unwrap();
    let conf = Ini::load_from_file(&filename).unwrap();
    let mut ini_data = ini_data::IniData::new();
    ini_data.load(conf);

    let gtk_ini_data_arc = Arc::new(Mutex::new(ini_data));
    // Reference for saving
    let gtk_ini_data_ref = gtk_ini_data_arc.clone();
    // Reference for adding new kv pairs
    let ini_data_add_ref = gtk_ini_data_arc.clone();
    let mut gtk_ini_data = gtk_ini_data_arc.lock().unwrap();

    let window = Arc::new(Mutex::new(create_default_window("TITLE OF INI FILE")));
    let window_ref = window.clone();
    let window_add_button_ref = window.clone();
    let display = gtk::Box::new(gtk::Orientation::Vertical, 10).unwrap();
    display.pack_start(&gtk_ini_data.get_entry_boxes(), false, false, 10);

    let create_button = gtk::Button::new_with_label("Create INI File").unwrap();
    let file_save_window = gtk::Window::new(gtk::WindowType::Toplevel).unwrap();
    let file_save_dialog = gtk::FileChooserDialog::new(
        "Load INI file",
        Some(&file_save_window),
        gtk::FileChooserAction::Save,
        [("Save", gtk::ResponseType::Accept),
         ("Cancel", gtk::ResponseType::Cancel)]);

    create_button.connect_clicked(move |_| {
        if file_save_dialog.run() == gtk::ResponseType::Accept as i32 {
            let save_filename = file_save_dialog.get_filename().unwrap();
            println!("Saving to file: {}", save_filename);
            gtk_ini_data_ref.lock().unwrap().save(save_filename);

            file_save_dialog.destroy();
            window_ref.lock().unwrap().destroy();
            return;
        } else {
            println!("Still editing {}", filename);
        }
        file_save_dialog.hide();
    });


    //  Create labels and boxes for adding new key values
    let section_label = gtk::Label::new("Section").unwrap();
    let section_list = gtk::ComboBoxText::new_with_entry().unwrap();
    for section_name in gtk_ini_data.section_names().iter() {
        section_list.append("", &section_name);
    }

    let section_box = gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap();
    section_box.pack_start(&section_label, false, false, 0);
    section_box.pack_start(&section_list, false, false, 0);

    let key_entry = gtk::Entry::new().unwrap();
    let key_label = gtk::Label::new("New Key").unwrap();
    let key_box = gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap();
    key_box.pack_start(&key_label, false, false, 0);
    key_box.pack_start(&key_entry, false, false, 0);

    let value_entry = gtk::Entry::new().unwrap();
    let value_label = gtk::Label::new("New Entry").unwrap();
    let value_box = gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap();
    value_box.pack_start(&value_label, false, false, 0);
    value_box.pack_start(&value_entry, false, false, 0);

    let key_value_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
    key_value_box.pack_start(&key_box, false, false, 0);
    key_value_box.pack_start(&value_box, false, false, 0);

    let add_button = gtk::Button::new_with_label("Add new key value pair").unwrap();
    add_button.connect_clicked(move |_| {
        let section = section_list.get_active_text().unwrap();
        let key = key_entry.get_text().unwrap();
        let value = value_entry.get_text().unwrap();
        println!("[{}]\n{} = {}", section, key, value);
        let mut ini_data = ini_data_add_ref.lock().unwrap();
        ini_data.add(section, key, value);
        let window = window_add_button_ref.lock().unwrap();


        window.show_all();

    });

    display.pack_start(&section_box, false, false, 0);
    display.pack_start(&key_value_box, false,  false, 0);
    display.pack_start(&add_button, false, false, 0);
    display.pack_start(&create_button, false, false, 20);

    window.lock().unwrap().add(&display);
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

    // Create a button for editing existing INI files
    let edit_button = gtk::Button::new_with_label("Edit Existing INI File").unwrap();
    edit_button.connect_clicked(move |_| {
        edit_ini_file();
    });
    display.pack_start(&edit_button, false, false, 0);

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
