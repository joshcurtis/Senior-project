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

    // Create an entry box with a label
    let entry = gtk::Entry::new().unwrap();
    let label = gtk::Label::new("Parameter").unwrap();
    let entry_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
    entry_box.pack_start(&label, false, false, 10);
    entry_box.pack_start(&entry, false, false, 0);


    // Create the button
    let button = gtk::Button::new_with_label("Generate INI file").unwrap();

    button.connect_clicked(move |_| {
        let s = entry.get_text().unwrap();
        println!("{}",s)
    });
    let display = gtk::Box::new(gtk::Orientation::Vertical, 50).unwrap();
    display.pack_start(&entry_box, false, false, 0);
    display.pack_start(&button, false, false, 0);



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
