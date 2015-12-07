extern crate gtk;

use gtk::traits::*;
use gtk_helper;

/**
 * Contains shared data for manipulating INIs.
 **/
pub struct IniData {
    pub section_values_vec: Vec<(String, Vec<(String, String)>)>,
    pub entries_vec: Vec<Vec<gtk::Entry>>,
    pub generate_button: gtk::Button,
    pub open_button: gtk::Button
}

/**
 * Currently, only used for IniData::new() which creates a default IniData object.
 **/
impl IniData {
    pub fn new(initial_data: Vec<(String, Vec<(String, String)>)> )-> IniData {
        IniData {
            section_values_vec: initial_data,
            entries_vec: Vec::new(),
            generate_button: gtk::Button::new_with_label("Generate INI File").unwrap(),
            open_button: gtk::Button::new_with_label("Load INI File").unwrap()
        }
    }

    pub fn get_entry_boxes(&mut self) -> gtk::Box {
        let display = gtk::Box::new(gtk::Orientation::Vertical,10).unwrap();

        // For each section
        for i in 0..self.section_values_vec.len() {
            let names = self.section_values_vec[i].1.iter().map(|e| {e.0.clone()}).collect();
            let (ret_entries, ret_boxes) = gtk_helper::build_entry_boxes(names);
            self.entries_vec.push(ret_entries);

            let section_label = gtk::Label::new(&self.section_values_vec[i].0).unwrap();
            let section_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
            section_box.pack_start(&section_label, false, false, 10);
            display.pack_start(&section_box, false, false, 0);

            for j in (0..ret_boxes.len()) {
                display.pack_start(&ret_boxes[j], false, false, 0);
            }
        }
        return display;
    }
}
