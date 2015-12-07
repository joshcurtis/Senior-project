extern crate gtk;
use gtk::traits::*;

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
}
