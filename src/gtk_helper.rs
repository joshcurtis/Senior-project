extern crate gtk;
use gtk::signal::Inhibit;
use gtk::traits::*;

pub fn build_entry_and_label(name: String) -> (gtk::Entry, gtk::Label) {
    let entry = gtk::Entry::new().unwrap();
    let label = gtk::Label::new(&name).unwrap();
    return (entry, label);
}


pub fn build_entries_and_labels(labels: Vec<String>) -> (Vec<gtk::Entry>, Vec<gtk::Label>) {
    let defaults = (0..labels.len()).into_iter().map(|_| "".to_string()).collect();
    return build_entries_and_labels_with_defaults(labels, defaults);
}

pub fn build_entries_and_labels_with_defaults(labels: Vec<String>, defaults: Vec<String>) -> (Vec<gtk::Entry>, Vec<gtk::Label>) {
    let n = labels.len();
    let entries: Vec<gtk::Entry> = (0..n).map(|i| {
        let entry = gtk::Entry::new().unwrap();
        entry.set_text(&defaults[i]);
        entry
    }).collect();

    let gtk_labels: Vec<gtk::Label> = (0..n).map(|i| {
        let name = format!("{}", labels[i]);
        gtk::Label::new(&name).unwrap()
    }).collect();

    return (entries, gtk_labels);
}

/**
 * Builds entry boxes with a label and input field for every string label.
 *
 * @param The entry boxes and the labels of each box
 *
 * @return Tuple of (vec<Entry>, vec<Box>)
 **/
pub fn build_entry_boxes(names: Vec<String>) -> (Vec<gtk::Entry>, Vec<gtk::Box>) {
    let defaults = (0..names.len()).into_iter().map(|_| "".to_string()).collect();
    return build_entry_boxes_with_defaults(names, defaults);
}

pub fn build_entry_boxes_with_defaults(names: Vec<String>, defaults: Vec<String>) -> (Vec<gtk::Entry>, Vec<gtk::Box>) {
    let n = names.len();
    let (entries, labels) = build_entries_and_labels_with_defaults(names, defaults);

    let boxes: Vec<gtk::Box> = (0..n).map(|i| {
        let entry_box = gtk::Box::new(gtk::Orientation::Horizontal, 0).unwrap();
        entry_box.pack_start(&labels[i], false, false, 10);
        entry_box.pack_start(&entries[i], false, false, 0);
        entry_box
    }).collect();

    return (entries, boxes);
}
