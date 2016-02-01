extern crate gtk;
use gtk::traits::*;

use ini::parser as parser;

#[derive(Debug)]
pub enum Error {
    IniError(parser::Error),
}

pub struct IniGui {
    contents: gtk::Box,
    ini: parser::Ini,
}

impl IniGui {
    pub fn from(ini: parser::Ini) -> Result<Self, Error> {
        let contents = gtk::Box::new(gtk::Orientation::Vertical, 4).unwrap();
        for section in ini.iter() {
            contents.add(&gtk::Label::new(&section.name()).unwrap());
            for (k, v) in section.iter() {
                let kv = gtk::Box::new(gtk::Orientation::Horizontal, 4).unwrap();
                kv.add(&gtk::Label::new(k).unwrap());
                kv.add(&gtk::Label::new(" = ").unwrap());
                kv.add(&gtk::Label::new(v).unwrap());
                contents.add(&kv);
            }
            contents.add(&gtk::Label::new("").unwrap());
        }
        Ok(IniGui {
            contents: contents,
            ini: ini,
        })
    }

    pub fn contents(&self) -> &gtk::Box {
        &self.contents
    }
}
