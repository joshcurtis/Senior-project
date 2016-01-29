extern crate gtk;
use gtk::traits::*;

#[derive(Debug)]
pub enum Error {
    CreateWidget,
    GtkInit,
}

pub struct App {
    contents: gtk::Box,
    window: gtk::Window,
}

impl App {
    pub fn new() -> Result<App, Error> {
        // Init GTK
        if gtk::init().is_err() {
            return Err(Error::GtkInit);
        }
        // Create Window
        let window_type = gtk::WindowType::Toplevel;
        let win = try!(gtk::Window::new(window_type).ok_or(Error::CreateWidget));
        win.connect_delete_event(|_, _| {
            gtk::main_quit();
            gtk::signal::Inhibit(false)
        });
        let contents = try!(gtk::Box::new(gtk::Orientation::Vertical, 4).ok_or(Error::CreateWidget));
        win.add(&contents);
        // Menubar
        let menubar = try!(App::gen_menubar());
        contents.add(&menubar);
        //
        Ok(App {
            contents: contents,
            window: win,
        })
    }

    pub fn run(&mut self) -> Result<(), Error> {
        self.window.show_all();
        gtk::main();
        Ok(())
    }

    pub fn gen_menubar() -> Result<gtk::Box, Error> {
        let menubar = try!(gtk::Box::new(gtk::Orientation::Horizontal, 8).ok_or(Error::CreateWidget));
        // File Menu
        let file_label = try!(gtk::Label::new("File").ok_or(Error::CreateWidget));
        menubar.pack_start(&file_label, false, false, 4);
        // Help Menu
        let help_label = try!(gtk::Label::new("Help").ok_or(Error::CreateWidget));
        menubar.pack_start(&help_label, false, false, 4);
        //
        Ok(menubar)
    }
}
