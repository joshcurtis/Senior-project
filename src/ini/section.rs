use std::collections::HashMap;
use std::str::FromStr;

pub struct Section {
    keys: Vec<String>,
    name: String,
    vals: HashMap<String, String>,
}

impl Section {
    pub fn new(name: &str) -> Self {
        Section {
            keys: Vec::new(),
            name: name.to_string(),
            vals: HashMap::new(),
        }
    }

    pub fn name(&self) -> String {
        self.name.clone()
    }

    pub fn rename(&mut self, name: &str) {
        self.name = name.to_string();
    }

    pub fn contains_key(&self, key: &str) -> bool {
        self.vals.contains_key(key)
    }

    pub fn get(&self, key: &str) -> Option<String> {
        match self.vals.get(key) {
            Some(v) => Some(v.clone()),
            None => None,
        }
    }

    pub fn add_key(&mut self, key: &str, val: &str) -> Result<(), String> {
        if self.contains_key(key) {
            Err("Section already has key.".to_string())
        } else {
            self.keys.push(key.to_string());
            self.vals.insert(key.to_string(), val.to_string());
            Ok(())
        }
    }

    pub fn set_key(&mut self, key: &str, val: &str) -> Result<(), String> {
        if self.contains_key(key) {
            self.vals.insert(key.to_string(), val.to_string());
            Ok(())
        } else {
            Err("Section does not contain the key.".to_string())
        }
    }
}

impl FromStr for Section {
    type Err = String;
    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut lines = s.lines().map(|s| s.trim()).filter(|s| s.len() > 0);
        let heading: String;
        match lines.next() {
            Some(s) => heading = s.to_string(),
            None    => return Err("There is no data to parse.".to_string()),
        }
        if !(heading.starts_with('[') && heading.ends_with(']')) {
            return Err("Heading is not in the right format.".to_string());
        }
        let mut section = Section::new(&heading[1..heading.len()-1].to_string());
        for l in lines {
            let equals_loc = l.find('=');
            let equals_i: usize;
            match equals_loc {
                Some(i) => equals_i = i,
                None    => return Err("No key value pair found.".to_string()),
            };
            let k = &l[..equals_i].trim();
            let v = &l[equals_i+1..].trim();
            match section.add_key(k, v) {
                Ok(_) => (),
                Err(_) => return Err("Duplicate key found.".to_string()),
            }
        }
        Ok(section)
    }
}

impl ToString for Section {
    fn to_string(&self) -> String {
        self.keys
            .iter()
            .map(|k| format!("{}={}", k, self.vals.get(k).unwrap()))
            .fold(format!("[{}]\n", &self.name), |acc, s| format!("{}{}\n", acc, s))
    }
}

#[test]
fn str_testing() {
    let s = "[heading]\nk=v\na=12\n";
    let t = "[heading]\n  \n\nk  = v\n  a=12    ";
    let sec = Section::from_str(t).unwrap();
    assert!(s == sec.to_string());
}
