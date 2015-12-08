extern crate gtk;
extern crate ini;

mod gtk_helper;
mod ini_data;

use gtk::signal::Inhibit;
use gtk::traits::*;
use ini::Ini;
use std::sync::{Arc,Mutex};

fn create_ini_file( _ : gtk::Button) {

    // Create empty IniData
    let mut ini_data = ini_data::IniData::new();

    // Initialize the window display box
    let display = gtk::Box::new(gtk::Orientation::Vertical, 10).unwrap();

    // Pack the outer ini data box first
    let scrolled_window = gtk::ScrolledWindow::new(None, None).unwrap();
    scrolled_window.set_min_content_width(0);
    scrolled_window.set_min_content_height(0);
    scrolled_window.add(&ini_data.outer_box);
    display.pack_start(&scrolled_window, false, false, 0);

    // Create and clone an ARC to the IniData
    let ini_data_arc = Arc::new(Mutex::new(ini_data));
    let ini_data_save_ref = ini_data_arc.clone();
    let ini_data_add_ref = ini_data_arc.clone();

    // Create and clone an ARC to an editing window
    let window = Arc::new(Mutex::new(create_default_window("Creating New INI")));
    let window_save_ref = window.clone();
    let window_add_button_ref = window.clone();

    // Create a file saving button
    let create_button = gtk::Button::new_with_label("Save INI File").unwrap();
    create_button.connect_clicked(move |_| {
        let file_save_window = gtk::Window::new(gtk::WindowType::Toplevel).unwrap();
        let file_save_chooser = gtk::FileChooserDialog::new(
            "Save INI File",
            Some(&file_save_window),
            gtk::FileChooserAction::Save,
            [("Save", gtk::ResponseType::Accept),
             ("Cancel", gtk::ResponseType::Cancel)]
        );

        if file_save_chooser.run() != gtk::ResponseType::Accept as i32 {
            println!("Still creating new file");
        } else {
            let save_filename = file_save_chooser.get_filename().unwrap();
            println!("Saving to file: {}", save_filename);

            ini_data_save_ref.lock().unwrap().save(save_filename);
            window_save_ref.lock().unwrap().destroy();
        }

        file_save_chooser.destroy();
    });

    // Create a Box with a Label and ComboBoxText with all sections
    let section_label = gtk::Label::new("Section").unwrap();
    let section_list = gtk::ComboBoxText::new_with_entry().unwrap();
    let section_box_vert = gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap();
    section_box_vert.pack_start(&section_label, false, false, 0);
    section_box_vert.pack_start(&section_list, false, false, 0);
    let section_box_horiz = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
    section_box_horiz.pack_start(&section_box_vert, true, true, 10);

    // Create a Box with a Label and Entry for a new key
    let key_entry = gtk::Entry::new().unwrap();
    let key_label = gtk::Label::new("Key").unwrap();
    let key_box = gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap();
    key_box.pack_start(&key_label, false, false, 0);
    key_box.pack_start(&key_entry, false, false, 0);

    // Create a Box with a Label and Entry for a new value
    let value_entry = gtk::Entry::new().unwrap();
    let value_label = gtk::Label::new("Value").unwrap();
    let value_box = gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap();
    value_box.pack_start(&value_label, false, false, 0);
    value_box.pack_start(&value_entry, false, false, 0);

    // Create a Box with an invisible Label and Button to add the new section-key-value
    let invis_label = gtk::Label::new("").unwrap();
    let add_new_button = gtk::Button::new_with_label("Add Key-Value").unwrap();
    let add_new_box = gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap();
    add_new_box.pack_start(&invis_label, false, false, 0);

    // Connect a click event to the add-new button
    add_new_button.connect_clicked(move |_| {
        let section = section_list.get_active_text().unwrap();
        let key = key_entry.get_text().unwrap();
        let value = value_entry.get_text().unwrap();

        // Return if the section or key is empty
        if section == "" || key == "" { return; }

        // Use a mutable reference to add the section-key-value
        let mut ini_ref = ini_data_add_ref.lock().unwrap();
        ini_ref.add(section.clone(), key.clone(), value.clone());

        // Use a mutable reference to update the section list
        section_list.remove_all();
        section_list.append_text("");
        for section in ini_ref.section_vec.iter() {
            section_list.append_text(&section.name.clone());
        }

        // Reset the key and values when adding a new
        key_entry.set_text("");
        value_entry.set_text("");


        // Refresh the window
        window_add_button_ref.lock().unwrap().show_all();
    });
    add_new_box.pack_start(&add_new_button, false, false, 0);

    // Create a Box to contain the last three boxes
    let key_value_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
    key_value_box.pack_start(&key_box, false, false, 10);
    key_value_box.pack_start(&value_box, false, false, 10);
    key_value_box.pack_start(&add_new_box, false,  false, 10);

    // Add all the boxes to the display
    display.pack_start(&section_box_horiz, false, false, 0);
    display.pack_start(&key_value_box, false,  false, 0);
    display.pack_start(&create_button, false, false, 10);

    // Add the display and show
    window.lock().unwrap().add(&display);
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

    // Run the file chooser
    if file_chooser.run() != gtk::ResponseType::Accept as i32 {
        println!("Returning to main menu");
        file_chooser.destroy();
        return;
    }

    // Get the INI filename
    let filename = file_chooser.get_filename().unwrap();
    file_chooser.destroy();
    println!("{}", &filename);

    // Read the config (INI)
    let conf = Ini::load_from_file(&filename).unwrap();
    let mut ini_data = ini_data::IniData::new();
    ini_data.load(conf);
    let section_names = ini_data.section_vec.iter().map(|section| section.name.clone()).collect::<Vec<String>>();

    // Initialize the window display box
    let display = gtk::Box::new(gtk::Orientation::Vertical, 10).unwrap();

    // Pack the outer ini data box first
    let scrolled_window = gtk::ScrolledWindow::new(None, None).unwrap();
    scrolled_window.set_min_content_width(300);
    scrolled_window.set_min_content_height(300);
    scrolled_window.add(&ini_data.outer_box);
    display.pack_start(&scrolled_window, false, false, 0);

    // Create and clone an ARC to the IniData
    let ini_data_arc = Arc::new(Mutex::new(ini_data));
    let ini_data_save_ref = ini_data_arc.clone();
    let ini_data_add_ref = ini_data_arc.clone();

    // Create and clone an ARC to an editing window
    let window = Arc::new(Mutex::new(create_default_window("Editing Existing INI")));
    let window_save_ref = window.clone();
    let window_add_button_ref = window.clone();

    // Create a file saving button
    let create_button = gtk::Button::new_with_label("Save INI File").unwrap();
    create_button.connect_clicked(move |_| {
        let file_save_window = gtk::Window::new(gtk::WindowType::Toplevel).unwrap();
        let file_save_chooser = gtk::FileChooserDialog::new(
            "Save INI File",
            Some(&file_save_window),
            gtk::FileChooserAction::Save,
            [("Save", gtk::ResponseType::Accept),
             ("Cancel", gtk::ResponseType::Cancel)]
        );

        if file_save_chooser.run() != gtk::ResponseType::Accept as i32 {
            println!("Still editing {}", filename);
        } else {
            let save_filename = file_save_chooser.get_filename().unwrap();
            println!("Saving to file: {}", save_filename);

            ini_data_save_ref.lock().unwrap().save(save_filename);
            window_save_ref.lock().unwrap().destroy();
        }

        file_save_chooser.destroy();
    });

    // Create a Box with a Label and ComboBoxText with all sections
    let section_label = gtk::Label::new("Section").unwrap();
    let section_list = gtk::ComboBoxText::new_with_entry().unwrap();
    for name in section_names {
        section_list.append_text(&name);
    }

    let section_box_vert = gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap();
    section_box_vert.pack_start(&section_label, false, false, 0);
    section_box_vert.pack_start(&section_list, false, false, 0);
    let section_box_horiz = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
    section_box_horiz.pack_start(&section_box_vert, true, true, 10);

    // Create a Box with a Label and Entry for a new key
    let key_entry = gtk::Entry::new().unwrap();
    let key_label = gtk::Label::new("Key").unwrap();
    let key_box = gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap();
    key_box.pack_start(&key_label, false, false, 0);
    key_box.pack_start(&key_entry, false, false, 0);

    // Create a Box with a Label and Entry for a new value
    let value_entry = gtk::Entry::new().unwrap();
    let value_label = gtk::Label::new("Value").unwrap();
    let value_box = gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap();
    value_box.pack_start(&value_label, false, false, 0);
    value_box.pack_start(&value_entry, false, false, 0);

    // Create a Box with an invisible Label and Button to add the new section-key-value
    let invis_label = gtk::Label::new("").unwrap();
    let add_new_button = gtk::Button::new_with_label("Add Key-Value").unwrap();
    let add_new_box = gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap();
    add_new_box.pack_start(&invis_label, false, false, 0);

    // Connect a click event to the add-new button
    add_new_button.connect_clicked(move |_| {
        let section = section_list.get_active_text().unwrap();
        let key = key_entry.get_text().unwrap();
        let value = value_entry.get_text().unwrap();

        // Return if the section or key is empty
        if section == "" || key == "" { return; }

        // Use a mutable reference to add the section-key-value
        let mut ini_ref = ini_data_add_ref.lock().unwrap();
        ini_ref.add(section.clone(), key.clone(), value.clone());

        // Use a mutable reference to update the section list
        section_list.remove_all();
        section_list.append_text("");
        for section in ini_ref.section_vec.iter() {
            section_list.append_text(&section.name.clone());
        }

        // Reset the key and values when adding a new
        key_entry.set_text("");
        value_entry.set_text("");

        // Refresh the window
        window_add_button_ref.lock().unwrap().show_all();
    });
    add_new_box.pack_start(&add_new_button, false, false, 0);

    // Create a Box to contain the last three boxes
    let key_value_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
    key_value_box.pack_start(&key_box, false, false, 10);
    key_value_box.pack_start(&value_box, false, false, 10);
    key_value_box.pack_start(&add_new_box, false,  false, 10);

    // Add all the boxes to the display
    display.pack_start(&section_box_horiz, false, false, 0);
    display.pack_start(&key_value_box, false,  false, 0);
    display.pack_start(&create_button, false, false, 10);

    // Add the display and show
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
