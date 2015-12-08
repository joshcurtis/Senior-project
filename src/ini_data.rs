extern crate gtk;
extern crate ini;

use ini::Ini;
use gtk::traits::*;
use std::io;

/**
 * Stores a key and the associated gtk::Label and gtk::Entry. The value is read and written
 * to the stored gtk::Entry.
 **/
pub struct IniKeyValue {
    pub key: String,
    pub entry: gtk::Entry,
    pub label: gtk::Label
}

/* Implementations for IniSection */
impl IniKeyValue {

    /**
     * A "constructor" for a default IniKeyValue.
     **/
    pub fn new(key: String, value: String) -> IniKeyValue {
        let new_instance = IniKeyValue {
            entry: gtk::Entry::new().unwrap(),
            label: gtk::Label::new(&key).unwrap(),
            key: key
        };

        new_instance.entry.set_text(&value);
        new_instance
    }

    /**
     * Builds a gtk::Box from the IniKeyValue.
     **/
    pub fn build_box(&self) -> gtk::Box {
        let key_value_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
        key_value_box.pack_start(&self.label, true, true, 10);
        key_value_box.pack_start(&self.entry, true, true, 0);
        key_value_box
    }
}

/**
 * Stores a section name and a vector of IniKeyValue.
 **/
pub struct IniSection {
    pub name: String,
    pub pairs: Vec<IniKeyValue>
}

/* Implementations for IniSection */
impl IniSection {

    /**
     * A "constructor" for a default IniSection.
     **/
    pub fn new(name: Option<String>) -> IniSection {
        IniSection {
            name: if name.is_some() { name.unwrap() } else { "".to_string() },
            pairs: Vec::new()
        }
    }

    /**
     * Builds a gtk::Box from an IniSection.
     **/
    pub fn build_box(&self) -> gtk::Box {
        let section_box = gtk::Box::new(gtk::Orientation::Vertical, 0).unwrap();
        let label_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
        let label = gtk::Label::new("").unwrap();
        label.set_markup(&format!("<big><b>{}</b></big>", &self.name));

        label_box.pack_start(&label, false, false, 10);
        section_box.pack_start(&label_box, false, false, 5);

        for pair in self.pairs.iter() {
            let pair_box = pair.build_box();
            section_box.pack_start(&pair_box, false, false, 0);
        }
        section_box
    }

    /**
     * Checks if a certain key exists in the IniSection. Returns and option with a reference
     * to the IniKeyValue.
     **/
    pub fn get_key_value(&self, key: String) -> Option<&IniKeyValue> {
        for pair in self.pairs.iter() {
            if pair.key == key { return Some(pair); }
        }
        None
    }
}

/**
 * Stores a vector of IniSections, a vector of gtk::Box, and a gtk::Box that contains all the
 * inner_boxes. The vector is only populated after a call to build_boxes().
 **/
pub struct IniData {
    pub section_vec: Vec<IniSection>,
    pub inner_boxes: Vec<gtk::Box>,
    pub outer_box: gtk::Box
}

/* Implementations for IniData */
impl IniData {

    /**
     * A "constructor" for a default IniData.
     **/
    pub fn new() -> IniData {
        IniData {
            section_vec: Vec::new(),
            inner_boxes: Vec::new(),
            outer_box: gtk::Box::new(gtk::Orientation::Vertical, 10).unwrap()
        }
    }

    /**
     * Uses the rust-ini crate to load INI sections, keys, and values.
     *
     * @param config Ini from the the rust-ini crate.
     **/
    pub fn load(&mut self, config: Ini) {

        // Iterate through all the sections
        for (section, properties) in config.iter() {
            let mut ini_section = IniSection::new(section.clone());

            // Iterate through all the properties
            for (key, value) in properties.iter() {

                // Construct and push a new IniKeyValue
                let ini_key_value = IniKeyValue::new(key.clone(), value.clone());
                ini_section.pairs.push(ini_key_value);
            }

            // Build a Box from the section
            let section_box = ini_section.build_box();

            // Pack the Box in the outer_box
            self.outer_box.pack_start(&section_box, false, false, 0);

            // Push the box and section into their appropriate vectors
            self.inner_boxes.push(section_box);
            self.section_vec.push(ini_section);
        }
    }

    /**
     * Uses the rust-ini crate to save the stored INI sections, keys, and values.
     *
     * @param filename Path to the file where the INI should be saved.
     **/
    pub fn save(&mut self, filename: String) {
        let mut conf = ini::Ini::new();

        // Iterate through all IniSections
        for section in self.section_vec.iter_mut() {
            let section_name = Some(section.name.to_owned());

            // Iterate through all IniKeyValues
            for pair in section.pairs.iter_mut() {

                // Copy the values into the INI configuration
                conf.set_to(
                    section_name.clone(),
                    pair.key.clone(),
                    pair.entry.get_text().clone().unwrap()
                );
            }
        }

        // Write the file
        conf.write_to(&mut io::stdout()).unwrap();
        conf.write_to_file(&filename).unwrap();
    }

    /**
     * Adds a section-key-value to the IniData. If the section does not exist, it is
     * created.
     **/
    pub fn add(&mut self, section: String, key: String, value: String) {
        let index_opt = self.get_index_of_section(&section);
        let new_key_value = IniKeyValue::new(key.clone(), value.clone());

        // If the section exists
        if index_opt.is_some() {
            let index = index_opt.unwrap();
            let mut key_not_found = true;

            // Start a block because one block borrows immutably while the other borrows mutabily
            {
                // Check if the section already contains the key
                let key_value_ref = self.section_vec[index].get_key_value(key.clone());
                if key_value_ref.is_some() {

                    // Update the value
                    key_value_ref.unwrap().entry.set_text(&value.clone());
                    key_not_found = false;
                }
            }

            // If the section does not contain the key
            if key_not_found {

                // Build a Box from the new IniKeyValue and add it to the inner box
                self.inner_boxes[index].pack_start(&new_key_value.build_box(), false, false, 0);

                // Push the actual IniKeyValue onto the IniSection pairs
                self.section_vec[index].pairs.push(new_key_value);

            }

        } else {
            // Build an IniSection from the section name
            let mut new_section = IniSection::new(Some(section.clone()));

            // Build a Box from the new IniKeyValue and pack it in the new section box
            let new_section_box = new_section.build_box();
            new_section_box.pack_start(&new_key_value.build_box(), false, false, 0);

            // Pack the new section in the outer box
            self.outer_box.pack_start(&new_section_box, false, false, 0);

            // Push the IniKeyValue, IniSection, and inner Box
            // Done last because push moves the value
            new_section.pairs.push(new_key_value);
            self.section_vec.push(new_section);
            self.inner_boxes.push(new_section_box);
        }
    }

    /**
     * Gets the index of the section with the same name as the parameter.
     *
     * @return Option< usize >
     **/
    fn get_index_of_section(&self, section_name: &String) -> Option<usize> {
        for index in 0..self.section_vec.len() {
            if *section_name == self.section_vec[index].name {
                return Some(index);
            }
        }
        None
    }
}


impl ToString for IniData {
    fn to_string(&self) -> String {
        let mut ini_str = "".to_string();
        for section in self.section_vec.iter() {
            let section_str = format!("[{}]\n", &section.name);
            let key_vals: Vec<String> = section.pairs
                .iter()
                .map(|x| {
                    format!(
                        "{}={}",
                        &x.key,
                        &x.entry.get_text().unwrap())
                })
                .map(|x| x.to_string())
                .collect();
            let key_vals_str = key_vals.join("\n");
            ini_str = format!("{}{}{}", &ini_str, section_str, key_vals_str);
        }
        ini_str
    }

}
