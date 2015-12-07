extern crate gtk;
use gtk::traits::*;
use gtk::signal::Inhibit;

/**
 * Builds entry boxes with a label and input field for every string label.
 *
 * @param names Strings for each box
 *
 * @return Tuple of (vec<Entry>, vec<Label>, vec<Box>)
 **/
fn build_entry_boxes(names: &Vec<&str>) -> (Vec<gtk::Box>, Vec<gtk::Entry>, Vec<gtk::Label>) {
    let n = names.len();
    let entries: Vec<gtk::Entry> = (0..n).map(|_| {
        gtk::Entry::new().unwrap()
    }).collect();

    let labels: Vec<gtk::Label> = (0..n).map(|i| {
        let name = format!("{}", names[i]);
        gtk::Label::new(&name).unwrap()
    }).collect();

    let entry_boxes: Vec<gtk::Box> = (0..n).map(|i| {
        let entry_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
        entry_box.pack_start(&labels[i], false, false, 10);
        entry_box.pack_start(&entries[i], false, false, 0);
        entry_box
    }).collect();

    return (entry_boxes, entries, labels);
}

fn gui_main() {
    // Make sure GTK loads
    gtk::init().ok().expect("Unable to load GTK");

    // Initialize some example INI information
    let sections = vec![
        ("Alpha", vec!["Temperature", "Operating System"]),
        ("Beta",  vec!["Height", "Width"])
    ];

    let mut labels: Vec<Vec<gtk::Label>> = Vec::new();
    let mut entries: Vec<Vec<gtk::Entry>> = Vec::new();
    let mut boxes: Vec<Vec<gtk::Box>> = Vec::new();

    // Build entry boxes for every section
    for i in (0..sections.len()) {
        let (ret_boxes, ret_entries, ret_labels) = build_entry_boxes(&sections[i].1);
        labels.push(ret_labels);
        entries.push(ret_entries);
        boxes.push(ret_boxes);
    }

    // Set up the button for generating the INI file
    let num_sections = sections.len();
    let section_names = sections.iter().map(|i| i.0 ).collect::<Vec<_>>();
    let button = gtk::Button::new_with_label("Generate INI file").unwrap();

    button.connect_clicked(move |_| {
        // Debugging info for now
        for i in (0..num_sections) {
            println!("[{}]", section_names[i]);
            for j in (0..labels[i].len()) {
                let label = labels[i][j].get_label().unwrap();
                let input = entries[i][j].get_text().unwrap();
                println!("{}={}", label, input);
            }
            println!("");
        }
    });

    // Create a button and associate it with a file chooser dialog
    let file_button = gtk::Button::new_with_label("Find INI file").unwrap();
    let file_window = gtk::Window::new(gtk::WindowType::Toplevel).unwrap();
    let file_chooser =  gtk::FileChooserDialog::new("Load INI file",
        Some(&file_window), gtk::FileChooserAction::Open,
        [("Ok", gtk::ResponseType::Accept), ("Cancel", gtk::ResponseType::Cancel)]);

    file_button.connect_clicked(move |_| {
        file_chooser.show_all();
        if file_chooser.run() == gtk::ResponseType::Accept as i32 {
            let filename = file_chooser.get_filename().unwrap();
            println!("{}", filename);
        }
        file_chooser.hide();
    });

    // Add all the boxes to the main display
    let display = gtk::Box::new(gtk::Orientation::Vertical,10).unwrap();

    for i in (0..sections.len()) {
        let section_label = gtk::Label::new(sections[i].0).unwrap();
        let section_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();

        section_box.pack_start(&section_label, false, false, 10);
        display.pack_start(&section_box, false, false, 0);
        for j in (0..sections[i].1.len()) {
            display.pack_start(&boxes[i][j], false, false, 0);
        }
    }
    display.pack_start(&button, false, false, 0);
    display.pack_start(&file_button, false, false, 0);

    // Create and set up the main window
    let window = gtk::Window::new(gtk::WindowType::Toplevel).unwrap();
    window.set_title("3D Printer INI Generator");
    window.connect_delete_event(|_, _| {
        gtk::main_quit();
        Inhibit(false)
    });
    window.set_border_width(10);
    window.set_window_position(gtk::WindowPosition::Center);
    window.set_default_size(350, 400);
    window.add(&display);

    // Open the window and run the GTK main
    window.show_all();
    gtk::main();
}


fn main() {
    gui_main();
}
