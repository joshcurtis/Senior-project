extern crate gtk;
use gtk::traits::*;
use gtk::signal::Inhibit;

fn gui_main() {
    match gtk::init() {
        Ok(_) => (),
        Err(_) => {
            panic!("Unable to load GTK");
        }
    }
    let n = 5;
    let entries: Vec<gtk::Entry> = (0..n).map(|_| -> gtk::Entry {
        gtk::Entry::new().unwrap()
    })
        .collect();
    let labels: Vec<gtk::Label> = (0..n).map(|i| -> gtk::Label {
        let name = format!("paramter {}", i);
        gtk::Label::new(&name).unwrap()})
        .collect();
    let entry_boxes: Vec<gtk::Box> = (0..n).map(|i| {
        let entry_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
        entry_box.pack_start(&labels[i], false, false, 10);
        entry_box.pack_start(&entries[i], false, false, 0);
        entry_box
    })
        .collect();

    // Create the button
    let button = gtk::Button::new_with_label("Generate INI file").unwrap();
    button.connect_clicked(move |_| {
        for entry in &entries {
            let s = entry.get_text().unwrap();
            println!("{}",s)
        }
    });

    let file_button = gtk::Button::new_with_label("Find INI file").unwrap();
    file_button.connect_clicked(move |_| {
        let file_window = gtk::Window::new(gtk::WindowType::Toplevel).unwrap();
        let file_chooser =  gtk::FileChooserDialog::new("title",
                                                        Some(&file_window),
                                                        gtk::FileChooserAction::Open,
                                                        [("Ok", gtk::ResponseType::Accept), ("Cancel", gtk::ResponseType::Cancel)]);
        file_chooser.show_all();
        let response = file_chooser.run();
        match response {
            -3 => {
                let filename = file_chooser.get_filename().unwrap();
                println!("{}", filename);
            },
            _ => ()

        }
        file_chooser.hide();
    });


    let display = gtk::Box::new(gtk::Orientation::Vertical,10).unwrap();
    for i in 0..n {
        display.pack_start(&entry_boxes[i], false, false, 0);
    }
    display.pack_start(&button, false, false, 0);
    display.pack_start(&file_button, false, false, 0);


    let window = gtk::Window::new(gtk::WindowType::Toplevel).unwrap();
    window.set_title("3D printer ini generator");
    window.connect_delete_event(|_, _| {
        gtk::main_quit();
        Inhibit(false)
    });
    window.set_border_width(10);
    window.set_window_position(gtk::WindowPosition::Center);
    window.set_default_size(350, 400);
    window.add(&display);

    loop {
        window.show_all();
        gtk::main_iteration_do(true);
        std::thread::sleep_ms(10);
    }
}


fn main() {
    gui_main();
}
