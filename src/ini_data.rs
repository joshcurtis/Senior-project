extern crate gtk;
extern crate ini;

use ini::Ini;
use gtk::traits::*;
use std::io;

pub struct IniKeyValue {
    pub key: String,
    pub entry: gtk::Entry,
    pub label: gtk::Label
}

impl IniKeyValue {
    pub fn new(key: String, value: String) -> IniKeyValue {
        let new_instance = IniKeyValue {
            entry: gtk::Entry::new().unwrap(),
            label: gtk::Label::new(&key).unwrap(),
            key: key
        };

        new_instance.entry.set_text(&value);
        new_instance
    }

    pub fn build_box(&self) -> gtk::Box {
        let key_value_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
        key_value_box.pack_start(&self.label, false, false, 10);
        key_value_box.pack_start(&self.entry, false, false, 0);
        key_value_box
    }
}

pub struct IniSection {
    pub section_name: String,
    pub pairs: Vec<IniKeyValue>,
}

impl IniSection {
    pub fn new(name: Option<String>) -> IniSection {
        IniSection {
            section_name: if name.is_some() { name.unwrap() } else { "".to_string() },
            pairs: Vec::new()
        }
    }

    pub fn build_box(&self) -> gtk::Box {
        let section_box = gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap();

        {
            let label_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
            let label = gtk::Label::new(&self.section_name).unwrap();

            label_box.pack_start(&label, false, false, 10);
            section_box.pack_start(&label_box, false, false, 5);
        }

        for pair in self.pairs.iter() {
            let pair_box = pair.build_box();
            section_box.pack_start(&pair_box, false, false, 0);
        }
        section_box
    }
}

/**
 * Contains shared data for manipulating INIs.
 **/
pub struct IniData {
    pub section_vec: Vec<IniSection>,
}

/**
 * Currently, only used for IniData::new() which creates a default IniData object.
 **/
impl IniData {
    pub fn new()-> IniData {
        IniData {
            section_vec: Vec::new(),
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

    pub fn get_entry_boxes(&mut self) -> Vec<(String, gtk::Box)> {
        let mut v = Vec::new();
        for i in 0..self.section_vec.len() {
            v.push((self.section_vec[i].section_name.clone(), self.section_vec[i].build_box()));
        }
        return v;
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

    pub fn add(&mut self, section: String, key: String, value: String) {
        let kv = IniKeyValue::new(key, value);
        let i = self.get_index_from_section_name(&section).unwrap();
        self.section_vec[i].pairs.push(kv);
    }

    pub fn section_names(&self) -> Vec<String> {
        return self.section_vec.iter().map(|s| s.section_name.clone()).collect();
    }

    fn get_index_from_section_name(&self, name: &str) -> Option<usize> {
        let names = self.section_names();
        for i in (0..names.len()) {
            if name == names[i] {
                return Some(i);
            }
        }
        return None;
    }
}
