use std::collections::HashMap;
use std::str::FromStr;

#[derive(Debug)]
pub enum Error {
    DuplicateKey,
    DuplicateSection,
    KeyNotFound,
    ParseError,
    SectionNotFound,
}

#[derive(Debug)]
#[derive(Clone)]
pub struct Ini {
    section_names: Vec<String>,
    sections: HashMap<String, Section>,
}

impl Ini {
    pub fn new() -> Ini {
        Ini {
            section_names: Vec::new(),
            sections: HashMap::new(),
        }
    }

    pub fn iter(&self) -> IniIterator {
        IniIterator {
            idx: 0,
            ini: &self,
            size: self.section_names.len(),
        }
    }

    pub fn section_names(&self) -> &Vec<String> {
        &self.section_names
    }

    pub fn contains_section(&self, name: &str) -> bool {
        self.sections.contains_key(name)
    }

    pub fn section(&self, name: &str) -> Result<&Section, Error> {
        if !self.contains_section(name) {
            return Err(Error::SectionNotFound);
        }
        Ok(self.sections.get(name).unwrap())
    }

    pub fn section_mut(&mut self, name: &str) -> Result<&mut Section, Error> {
        if !self.contains_section(name) {
            return Err(Error::SectionNotFound);
        }
        Ok(self.sections.get_mut(name).unwrap())
    }

    pub fn add_section(&mut self, name: &str) -> Result<(), Error> {
        if self.contains_section(name) {
            return Err(Error::DuplicateSection);
        }
        self.section_names.push(name.to_string());
        self.sections.insert(name.to_string(), Section::new(name));
        Ok(())
    }
}

impl FromStr for Ini {
    type Err = Error;
    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut ini = Ini::new();
        let mut current_section = "";
        for s in s.lines() {
            let s = s.trim();
            match line_type(s) {
                LineType::Empty    => (),
                LineType::Comment  => (),
                LineType::Section  => {
                    current_section = &s[1..s.len()-1];
                    try!(ini.add_section(current_section));
                },
                LineType::KeyValue => {
                    let equals_i = s.find('=').unwrap();
                    let (key, val) = s.split_at(equals_i);
                    let key = key.trim();
                    let val = val[1..].trim();
                    let sec = try!(ini.section_mut(current_section));
                    sec.add_key(key, val);
                },
                LineType::Error    => {
                    return Err(Error::ParseError);
                },
            }
        }
        Ok(ini)
    }
}

impl ToString for Ini {
    fn to_string(&self) -> String {
        self.iter().fold(String::new(), |acc, sec| {
            match acc.len() {
                0 => sec.to_string(),
                _ => format!("{}\n{}", acc, sec.to_string()),
            }
        })
    }
}

pub struct IniIterator<'a> {
    idx: usize,
    ini: &'a Ini,
    size: usize,
}

impl<'a> Iterator for IniIterator<'a> {
    type Item = &'a Section;

    fn next(&mut self) -> Option<Self::Item> {
        let i = self.idx;
        if self.size > 0 {
            self.idx += 1;
            self.size -= 1;
            let sec = self.ini.section(&self.ini.section_names()[i]).unwrap();
            Some(sec)
        } else {
            None
        }
    }

    fn size_hint(&self) -> (usize, Option<usize>) {
        (self.size, Some(self.size))
    }
}

#[derive(Debug)]
#[derive(Clone)]
pub struct Section {
    keys: Vec<String>,
    name: String,
    vals: HashMap<String, String>,
}

impl Section {
    pub fn new(name: &str) -> Section {
        Section {
            keys: Vec::new(),
            name: name.to_string(),
            vals: HashMap::new(),
        }
    }

    pub fn contains_key(&self, key: &str) -> bool {
        self.vals.contains_key(key)
    }

    pub fn get_value(&self, key: &str) -> Result<&str, Error> {
        match self.vals.get(key) {
            Some(v) => Ok(&v),
            None    => Err(Error::KeyNotFound),
        }
    }

    pub fn add_key(&mut self, key: &str, val: &str) -> Result<(), Error> {
        if self.contains_key(key) {
            return Err(Error::DuplicateKey);
        }
        self.keys.push(key.to_string());
        self.vals.insert(key.to_string(), val.to_string());
        Ok(())
    }

    pub fn update_value(&mut self, key: &str, val: &str) -> Result<(), Error> {
        if !self.contains_key(key) {
            return Err(Error::KeyNotFound);
        }
        let v = self.vals.get_mut(key).unwrap();
        v.clear();
        v.push_str(val);
        Ok(())
    }
}

impl ToString for Section {
    fn to_string(&self) -> String {
        let header = format!("[{}]\n", &self.name);
        self.keys.iter().fold(header, |acc, k| {
            let v = self.get_value(&k).unwrap();
            format!("{}{}={}\n", acc, k, v)
        })
    }
}

#[derive(Debug)]
enum LineType {
    Empty,
    Section,
    Comment,
    KeyValue,
    Error,
}

fn line_type(l: &str) -> LineType {
    let l = l.trim();
    if l.len() == 0 {
        return LineType::Empty;
    }
    if l.starts_with('[') && l.ends_with(']') {
        return LineType::Section;
    }
    if l.starts_with('#') || l.starts_with(';') {
        return LineType::Comment;
    }
    if l.contains('=') {
        return LineType::KeyValue;
    }
    return LineType::Error;
}

#[test]
fn test() {
    use std::fs::File;
    use std::io::Read;
    let s = {
        let mut f = File::open("test.ini").unwrap();
        let mut s = String::new();
        f.read_to_string(&mut s).unwrap();
        s
    };
    let ini = Ini::from_str(&s).unwrap();
    assert_eq!(&ini.to_string(), &s);
}
