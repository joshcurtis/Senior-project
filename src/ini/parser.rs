use std::collections::HashMap;
use std::str::FromStr;

#[derive(Debug)]
pub enum Error {
    NotImplemented,
}

#[derive(Debug)]
pub struct Section {
    keys: Vec<String>,
    name: String,
    vals: HashMap<String, String>,
}

impl Section {
    pub fn new(name: &str) -> Result<Self, Error> {
        Ok(Section {
            keys: Vec::new(),
            name: name.to_string(),
            vals: HashMap::new(),
        })
    }
}

impl ToString for Section {
    fn to_string(&self) -> String {
        self.keys.iter().fold(self.name.clone(), |acc, k| {
            let v = self.vals.get(k).unwrap();
            format!("{}\n{}={}", acc, &k, &v)
        })
    }
}

#[derive(Debug)]
pub struct Ini {
    section_names: Vec<String>,
    sections: HashMap<String, Section>,
}

impl Ini {
    pub fn new() -> Result<Self, Error> {
        Ok(Ini {
            section_names: Vec::new(),
            sections: HashMap::new(),
        })
    }
}

impl ToString for Ini {
    fn to_string(&self) -> String {
        self.section_names.iter().fold(String::new(), |acc, s| {
            let new_content = self.sections.get(s).unwrap().to_string();
            match acc.len() {
                0 => new_content,
                _ => format!("{}\n{}", acc, new_content)
            }
        })
    }
}

impl FromStr for Ini {
    type Err = Error;
    fn from_str(s: &str) -> Result<Self, Self::Err> {
        return Err(Error::NotImplemented);
        let ini = try!(Ini::new());
        Ok(ini)
    }
}
