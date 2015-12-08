from flask import Flask, jsonify, make_response, request
from controller import Controller

app = Flask(__name__)
controller = Controller(
    fnames = ['alphabet.txt', 'test.ini', 'testfile.txt', 'testfile_copy.txt'],
    root = './test_files',
    services_commands = ['python ./test_files/greeter.py']
)

@app.route('/')
def homepage():
    return 'Hello World!'


@app.route('/files', methods=['GET'])
def get_files():
    global controller
    fnames = controller.get_filenames()
    return make_response(jsonify({'files': fnames}), 200)


@app.route('/files/<string:fname>', methods=['GET', 'PUT'])
def get_file(fname):
    global controller
    if request.method == 'GET':
        data = controller.get_file_contents(fname)
        return make_response(jsonify({
            'file': fname,
            'data': data
        }), 200)
    elif request.method == 'PUT':
        contents = request.data.decode('ascii')
        bytes_written = controller.set_file_contents(fname, contents)
        return make_response(jsonify({
            'bytes_written': bytes_written
        }), 200)

@app.route('/services', methods=['GET'])
def get_services():
    global controller
    services_are_running = controller.is_running_services()
    services = controller.get_services()
    return make_response(jsonify({
        'services_are_running': services_are_running,
        'services': services
    }), 200)

@app.route('/services/start')
def start_services():
    global controller
    if not controller.is_running_services():
        controller.start_services()
    return get_services()

@app.route('/services/stop')
def stop_services():
    global controller
    if controller.is_running_services():
        controller.stop_services()
    return get_services()

@app.route('/services/reset')
def reset_services():
    global controller
    if controller.is_running_services():
        controller.reset_services()
    else:
        controller.start_services()
    return get_services()

if __name__ == '__main__':
    app.run(debug=True)
