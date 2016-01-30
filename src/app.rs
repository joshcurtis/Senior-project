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
    window_layout: gtk::Box,
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
        let window_layout = try!(gtk::Box::new(gtk::Orientation::Vertical, 4).ok_or(Error::CreateWidget));
        win.add(&window_layout);
        // Menubar
        let menubar = try!(App::gen_menubar());
        window_layout.add(&menubar);
        // Contents
        let contents = try!(gtk::Box::new(gtk::Orientation::Vertical, 4).ok_or(Error::CreateWidget));
        try!(App::fill_default_contents(&contents));
        window_layout.add(&contents);
        //
        Ok(App {
            contents: contents,
            window: win,
            window_layout: window_layout,
        })
    }

    pub fn run(&mut self) -> Result<(), Error> {
        self.window.show_all();
        gtk::main();
        Ok(())
    }

    pub fn gen_menubar() -> Result<gtk::Box, Error> {
        let menubar = try!(gtk::Box::new(gtk::Orientation::Horizontal, 8).ok_or(Error::CreateWidget));
        let size = 5; // GTK_ICON_SIZE_DND
        let new_file = try!(gtk::Image::new_from_icon_name("document-new", size)
                            .ok_or(Error::CreateWidget));
        new_file.set_tooltip_text("New");
        let open_file = try!(gtk::Image::new_from_icon_name("document-open", size)
                             .ok_or(Error::CreateWidget));
        open_file.set_tooltip_text("Open");
        let save_file = try!(gtk::Image::new_from_icon_name("document-save", size)
                             .ok_or(Error::CreateWidget));
        save_file.set_tooltip_text("Save");
        menubar.add(&new_file);
        menubar.add(&open_file);
        menubar.add(&save_file);
        //
        Ok(menubar)
    }

    pub fn fill_default_contents(contents: &gtk::Box) -> Result<(), Error> {
        contents.destroy();
        let text = try!(gtk::Label::new("Nothing Loaded").ok_or(Error::CreateWidget));
        contents.add(&text);
        Ok(())
    }
}
