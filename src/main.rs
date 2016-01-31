extern crate gtk;
pub mod app;
pub mod ini;
pub mod util;

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
