extern crate gtk;
mod app;
mod ini;

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
