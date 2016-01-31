use std::io::{Read, Write};
use std::fs::File;
use std::str::FromStr;

use std::io;


/// Converts the contents of the file `fname` into an object
/// which implements the FromStr trait.
///
/// # Panics
/// Panics when there is an error opening or reading from the file.
///
fn from_file<T: FromStr>(fname: &str) -> Result<T, T::Err> {
    let s = {
        let mut f = File::open(fname).unwrap();
        let mut s = String::new();
        f.read_to_string(&mut s).unwrap();
        s
    };
    T::from_str(&s)
}

/// Writes the string representation of `obj` into a file.
///
/// # Failures
/// Returns an std::io::Error if there is an error creating/opening
/// the file or writing to it.
fn to_file<T: ToString>(obj: &T, fname: &str) -> io::Result<()> {
    let mut f = try!(File::create(fname));
    let s = obj.to_string();
    try!(f.write_all(s.as_bytes()));
    Ok(())
}
