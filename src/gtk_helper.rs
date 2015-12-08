extern crate gtk;

use gtk::signal::Inhibit;
use gtk::traits::*;

pub fn create_default_window(title: &str) -> gtk::Window {
    let window = gtk::Window::new(gtk::WindowType::Toplevel).unwrap();
    window.set_window_position(gtk::WindowPosition::Center);
    window.set_title(title);
    window.connect_delete_event(|window, _| {
        window.destroy();
        return Inhibit(true);
    });

    return window;
}

pub fn create_dropdown(items: Vec<String>) -> gtk::ComboBoxText {
    let dropdown = gtk::ComboBoxText::new_with_entry().unwrap();
    for s in items.iter() {
        dropdown.append_text(&s);
    }
    dropdown
}
