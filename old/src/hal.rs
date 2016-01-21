use std::fs::File;
use std::io::Read;

fn preprocess_line(line: &str) -> String {
    let trimmed = line.trim();
    let comment_start = trimmed.find('#');
    let preprocessed = match comment_start {
        Some(i) => &trimmed[0..i],
        None => trimmed,
    };
    preprocessed.trim().to_string()
}

pub struct HAL {
    commands: Vec<String>
}

impl HAL {
    pub fn new(hal_program: &str) -> HAL {
        let commands: Vec<String> = hal_program
            .lines()
            .map(|x| preprocess_line(x))
            .filter(|x| x.len()>0)
            .collect();
        HAL {
            commands: commands,
        }
    }

}

impl ToString for HAL {
    fn to_string(&self) -> String {
        self.commands.join("\n")
    }
}
