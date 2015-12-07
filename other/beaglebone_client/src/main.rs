extern crate hyper;
extern crate rustc_serialize;
use rustc_serialize::json;
use std::io::Read;

struct BeagleBoneClient {
    url: String,
}

#[derive(RustcDecodable, RustcEncodable)]
struct FilesRes {
    files: Vec<String>
}

#[derive(RustcDecodable, RustcEncodable)]
struct FileContentsRes {
    file: String,
    data: String,
}

#[derive(RustcDecodable, RustcEncodable)]
struct ServicesRes {
    services_are_running: bool,
    services: Vec<String>,
}

impl BeagleBoneClient {
    fn new(url: &str) -> BeagleBoneClient {
        BeagleBoneClient {
            url: url.to_string()
        }
    }

    fn services_are_running(&self) -> bool {
        let url = format!("{}/services", self.url);
        let mut res = hyper::Client::new().get(&url).send().unwrap();
        assert_eq!(res.status, hyper::status::StatusCode::Ok);
        let mut data = "".to_string();
        res.read_to_string(&mut data).ok().expect("Failed to read data from response.");
        let services_res: ServicesRes = json::decode(&data).unwrap();
        services_res.services_are_running
    }

    fn start_services(&self) {
        let url = format!("{}/services/start", self.url);
        let res = hyper::Client::new().put(&url).send().unwrap();
        assert_eq!(res.status, hyper::status::StatusCode::Ok);
    }

    fn stop_services(&self) {
        let url = format!("{}/services/stop", self.url);
        let res = hyper::Client::new().put(&url).send().unwrap();
        assert_eq!(res.status, hyper::status::StatusCode::Ok);
    }

    fn reset_services(&self) {
        let url = format!("{}/services/reset", self.url);
        let res = hyper::Client::new().put(&url).send().unwrap();
        assert_eq!(res.status, hyper::status::StatusCode::Ok);
    }

    fn get_services(&self) -> Vec<String> {
        let url = format!("{}/services", self.url);
        let mut res = hyper::Client::new().get(&url).send().unwrap();
        assert_eq!(res.status, hyper::status::StatusCode::Ok);
        let mut data = "".to_string();
        res.read_to_string(&mut data).ok().expect("Failed to read data from response.");
        let services_res: ServicesRes = json::decode(&data).unwrap();
        services_res.services
    }

    fn get_files(&self) -> Vec<String> {
        let url = format!("{}/files", self.url);
        let mut res = hyper::Client::new().get(&url).send().unwrap();
        assert_eq!(res.status, hyper::status::StatusCode::Ok);
        let mut data = "".to_string();
        res.read_to_string(&mut data).ok().expect("Failed to read data from response.");
        let file_res: FilesRes = json::decode(&data).unwrap();
        file_res.files
    }

    fn get_file_contents(&self, filename: &str) -> String {
        let url = format!("{}/files/{}", self.url, filename);
        let mut res = hyper::Client::new().get(&url).send().unwrap();
        assert_eq!(res.status, hyper::status::StatusCode::Ok);
        let mut data = "".to_string();
        res.read_to_string(&mut data).ok().expect("failed to read data from response.");
        let file_contents_res: FileContentsRes = json::decode(&data).unwrap();
        file_contents_res.data
    }

    fn write_file_contents(&self, filename: &str, data: &str) {
        let url = format!("{}/files/{}", self.url, filename);
        let res = hyper::Client::new().put(&url).body(data).send().unwrap();
        assert_eq!(res.status, hyper::status::StatusCode::Ok);
    }

}

const SERVER_URL: &'static str = "http://127.0.0.1:5000";

fn main() {
    let bb = BeagleBoneClient::new(SERVER_URL);
    let files = bb.get_files();
    let contents = bb.get_file_contents(&files[1]);
    println!("{}", contents);
}
