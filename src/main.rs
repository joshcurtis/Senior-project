extern crate gtk;
use std::rc::Rc;
use std::cell::RefCell;
use std::sync::{Arc,Mutex};
use gtk::traits::*;
use gtk::signal::Inhibit;

/**
 * Contains shared data for manipulating INIs.
 **/
struct IniData<'a> {
    section_values_vec: Vec<(&'a str, Vec<&'a str>)>,
    entries_vec: Vec<Vec<gtk::Entry>>,
    generate_button: gtk::Button,
    open_button: gtk::Button
}

/**
 * Currently, only used for IniData::new() which creates a default IniData object.
 **/
impl<'a> IniData<'a> {
    fn new(initial_data: Vec<(&'a str, Vec<&'a str>)>) -> IniData<'a> {
        IniData {
            section_values_vec: initial_data,
            entries_vec: Vec::new(),
            generate_button: gtk::Button::new_with_label("Generate INI File").unwrap(),
            open_button: gtk::Button::new_with_label("Load INI File").unwrap()
        }
    }
}


fn build_entry_box_with_labels(labels: Vec<String>) -> (Vec<gtk::Entry>, Vec<gtk::Label>) {
    let n = labels.len();
    let entries: Vec<gtk::Entry> = (0..n).map(|_| {
        gtk::Entry::new().unwrap()
    }).collect();

    let gtk_labels: Vec<gtk::Label> = (0..n).map(|i| {
        let name = format!("{}", labels[i]);
        gtk::Label::new(&name).unwrap()
    }).collect();

    return (entries, gtk_labels);
}


/**
 * Builds entry boxes with a label and input field for every string label.
 *
 * @param names Strings for each box
 *
 * @return Tuple of (vec<Entry>, vec<Box>)
 **/
fn build_entry_boxes(names: &Vec<&str>) -> (Vec<gtk::Entry>, Vec<gtk::Box>) {
    let n = names.len();
    let labels: Vec<gtk::Label> = (0..n).map(|i| {
        let name = format!("{}", names[i]);
        gtk::Label::new(&name).unwrap()
    }).collect();

    let entries: Vec<gtk::Entry> = (0..n).map(|_| {
        gtk::Entry::new().unwrap()
    }).collect();

    let boxes: Vec<gtk::Box> = (0..n).map(|i| {
        let entry_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
        entry_box.pack_start(&labels[i], false, false, 10);
        entry_box.pack_start(&entries[i], false, false, 0);
        entry_box
    }).collect();

    return (entries, boxes);
}


fn create_ini_file( _ : gtk::Button) {
    let params = Arc::new(Mutex::new(Vec::new()));
    let display = Arc::new(Mutex::new(gtk::Box::new(gtk::Orientation::Vertical, 10).unwrap()));

    let add_param_window = create_default_window("Add Parameter");
    let add_param_dialog = gtk::Dialog::with_buttons("Add Parameter",
                                                         Some(&add_param_window),
                                                         gtk::DialogFlags::empty(),
                                                         [("Add", gtk::ResponseType::Accept),
                                                          ("Cancel", gtk::ResponseType::Cancel)]);
    add_param_dialog.set_window_position(gtk::WindowPosition::Center);

    // Create a parameter entry box for creating new parameters
    let (add_param_entry, add_param_label) = build_entry_box_with_labels(vec!["Parameter Name".to_string()]);
    let param_entry_box = add_param_dialog.get_content_area();
    param_entry_box.add(&add_param_label[0]);
    param_entry_box.add(&add_param_entry[0]);

    let window = create_default_window("Create INI File");
    let add_param_button = gtk::Button::new_with_label("Add Parameter").unwrap();

    let d = display.clone();
    let w = window.clone();
    add_param_button.connect_clicked(move |button| {
        add_param_dialog.show_all();
        if add_param_dialog.run() == gtk::ResponseType::Accept as i32 {
            let s = add_param_entry[0].get_text().unwrap();
            println!("{}", s);
            params.lock().unwrap().push(s.clone());

            let (param_entries, param_labels) =
                build_entry_box_with_labels(vec![s]);
            let entry_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
            entry_box.pack_start(&param_labels[0], false, false, 10);
            entry_box.pack_start(&param_entries[0], false, false, 0);
            let display = gtk::Box::new(gtk::Orientation::Vertical, 10).unwrap();
            display.pack_start(&entry_box, false, false, 10);
            let window = button.get_parent().unwrap();
            window.add(&display);
            window.show_all();
        }
        add_param_dialog.hide();
    });

    d.lock().unwrap().pack_start(&add_param_button, false, false, 10);
    w.add(&d.lock().unwrap().clone());
    w.show_all();
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
    let mut sample_ini = IniData::new(vec![
        ("Alpha", vec!["Temperature", "Operating System"]),
        ("Beta",  vec!["Height", "Width"])
    ]);

    // Initialize the GUI with the sample INI
    for i in (0..sample_ini.section_values_vec.len()) {
        let (ret_entries, ret_boxes) = build_entry_boxes(&sample_ini.section_values_vec[i].1);
        sample_ini.entries_vec.push(ret_entries);

        let section_label = gtk::Label::new(sample_ini.section_values_vec[i].0).unwrap();
        let section_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
        section_box.pack_start(&section_label, false, false, 10);
        display.pack_start(&section_box, false, false, 0);

        for j in (0..ret_boxes.len()) {
            display.pack_start(&ret_boxes[j], false, false, 0);
        }
    }

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
                    let label = ((*ini_ref).section_values_vec[i].1)[j];
                    let input = (*ini_ref).entries_vec[i][j].get_text().unwrap();
                    println!("{}={}", label, input);
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
