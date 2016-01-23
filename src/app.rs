extern crate gtk;
use gtk::traits::*;

#[derive(Debug)]
pub enum Error {
    CreateWindow,
    GtkInit,
}

pub struct App {
    window: gtk::Window,
}

impl App {
    pub fn new() -> Result<App, Error> {
        // Init GTK
        match gtk::init().is_err() {
            true => return Err(Error::GtkInit),
            false => (),
        }
        // Create Window
        let window_type = gtk::WindowType::Toplevel;
        let win = match gtk::Window::new(window_type) {
            Some(w) => w,
            None => return Err(Error::CreateWindow),
        };
        win.connect_delete_event(|_, _| {
            gtk::main_quit();
            gtk::signal::Inhibit(false)
        });
        //
        Ok(App {
            window: win,
        })
    }

    pub fn run(&mut self) -> Result<(), Error> {
        self.window.show_all();
        gtk::main();
        Ok(())
    }
}
