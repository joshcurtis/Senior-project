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

struct HAL {
    commands: Vec<String>
}

impl HAL {

    fn new(hal_program: &str) -> HAL {
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

#[test]
fn comments() {
    assert_eq!(preprocess_line("# This is a comment"), "");
    assert_eq!(preprocess_line("This is code # Comment"), "This is code")
}

// #[test]
// fn from_file() {
//     let mut file = File::open("test.hal").ok().expect("Failed to open test.hal.");
//     let mut file_contents = "".to_string();
//     file.read_to_string(&mut file_contents);
//     let hal = HAL::new(&file_contents);
//     for el in hal.commands.iter() {
//         println!("{}", el);
//     }
// }
