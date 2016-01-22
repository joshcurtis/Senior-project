use std::collections::HashMap;
use std::str::FromStr;
use ini::section::Section;

pub struct Ini {
    section_names: Vec<String>,
    sections: HashMap<String, Section>,
}

impl Ini {
    pub fn new() -> Self {
        Ini {
            section_names: Vec::new(),
            sections: HashMap::new(),
        }
    }

    pub fn contains_section(&self, section: &str) -> bool {
        self.sections.contains_key(section)
    }
}

impl FromStr for Ini {
    type Err = String;
    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut s = s.clone();
        let mut ini = Ini::new();
        while s.len() > 0 {
            let end = s.find("\n[");
            {
                let content = match end {
                    Some(i) => &s[..i],
                    None    => s,
                }.trim();
                if content.len() > 0 {
                    let sec: Section;
                    match Section::from_str(content) {
                        Ok(section) => sec = section,
                        Err(err) => return Err(err),
                    }
                    if ini.contains_section(&sec.name()) {
                        return Err("Duplicate section found.".to_string());
                    }
                    ini.section_names.push(sec.name());
                    ini.sections.insert(sec.name(), sec);
                }
            }
            match end {
                Some(i) => s = &s[i..],
                None    => s = "",
            }
        }
        Ok(ini)
    }
}

impl ToString for Ini {
    fn to_string(&self) -> String {
        self.section_names
            .iter()
            .map(|s| self.sections.get(s).unwrap().to_string())
            .fold(String::new(), |acc, s| format!("{}\n{}", acc, s))
    }
}
