extern crate gtk;
extern crate ini;

use ini::Ini;
use gtk::traits::*;
use gtk_helper;
use std::io;

pub struct IniKeyValue {
    pub key: String,
    pub entry: gtk::Entry,
    pub key_value_box: gtk::Box
}

impl IniKeyValue {
    pub fn new(key: String, value: String) -> IniKeyValue {
        let new_instance = IniKeyValue {
            key: key,
            entry: gtk::Entry::new().unwrap(),
            key_value_box: gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap()
        };

        // new_instance.entry.set_text(value.as_str());
        new_instance.entry.set_text("Convert library required?");
        new_instance
    }

    pub fn build_box(&mut self) -> &gtk::Box {
        let label = gtk::Label::new(&self.key).unwrap();

        self.key_value_box.pack_start(&label, false, false, 10);
        self.key_value_box.pack_start(&self.entry, false, false, 0);
        &self.key_value_box
    }
}

pub struct IniSection {
    pub section_name: String,
    pub pairs: Vec<IniKeyValue>,
    pub section_box: gtk::Box
}

impl IniSection {
    pub fn new(name: Option<String>) -> IniSection {
        let mut new_instance = IniSection {
            section_name: "".to_string(), pairs: Vec::new(),
            section_box: gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap()
        };

        if name.is_some() {
            new_instance.section_name = name.unwrap();
        }
        new_instance
    }

    pub fn build_box(&mut self) -> &gtk::Box {
        let label_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
        let label = gtk::Label::new(&self.section_name).unwrap();

        label_box.pack_start(&label, false, false, 10);
        self.section_box.pack_start(&label_box, false, false, 5);

        for pair in self.pairs.iter_mut() {
            let ref pair_box = *pair.build_box();
            self.section_box.pack_start(pair_box, false, false, 0);
        }
        &self.section_box
    }
}

/**
 * Contains shared data for manipulating INIs.
 **/
pub struct IniData {
    pub section_vec: Vec<IniSection>,
    pub generate_button: gtk::Button,
    pub open_button: gtk::Button
}

/**
 * Currently, only used for IniData::new() which creates a default IniData object.
 **/
impl IniData {
    pub fn new()-> IniData {
        IniData {
            section_vec: Vec::new(),
            generate_button: gtk::Button::new_with_label("Generate INI File").unwrap(),
            open_button: gtk::Button::new_with_label("Edit Existing INI File").unwrap()
        }
    }

    pub fn load(&mut self, config: Ini) {
        for (section, properties) in config.iter() {
            let mut ini_section = IniSection::new(section.clone());

            for (key, value) in properties.iter() {
                let ini_key_value = IniKeyValue::new(key.clone(), value.clone());
                ini_section.pairs.push(ini_key_value);
            }

            self.section_vec.push(ini_section);
        }
    }

    pub fn get_entry_boxes(&mut self) -> gtk::Box {
        let display = gtk::Box::new(gtk::Orientation::Vertical,10).unwrap();

        // For each section
        for i in 0..self.section_vec.len() {
            display.pack_start(self.section_vec[i].build_box(), false, false, 0);
        }
        return display;
    }

    pub fn save(&mut self, filename: String) {
        let mut conf = ini::Ini::new();

        for section in self.section_vec.iter_mut() {
            let section_name = Some(section.section_name.to_owned());

            for pair in section.pairs.iter_mut() {
                conf.set_to(section_name.clone(),
                    pair.key.clone(),
                    pair.entry.get_text().clone().unwrap());
            }
        }

        conf.write_to(&mut io::stdout()).unwrap();
        conf.write_to_file(&filename).unwrap();
    }
}
