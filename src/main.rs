mod ini;
mod app;

extern crate gtk;
use gtk::traits::*;
use gtk::signal::Inhibit;

fn main() {
    match run() {
        Ok(()) => (),
        Err(e) => panic!("app::Error - {:?}", e),
    }
}

fn run() -> Result<(), app::Error> {
    let mut app = try!(app::App::new());
    app.run()
}
