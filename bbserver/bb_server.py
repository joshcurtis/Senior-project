#!/usr/bin/env python2
from flask import Flask
import edn_format as edn

from glob import glob
import os.path

app = Flask(__name__)
app.debug = True

# stolen code for crossdomain
from datetime import timedelta
from flask import make_response, request, current_app
from functools import update_wrapper

def crossdomain(origin=None, methods=None, headers=None,
                max_age=21600, attach_to_all=True,
                automatic_options=True):
    if methods is not None:
        methods = ', '.join(sorted(x.upper() for x in methods))
    if headers is not None and not isinstance(headers, basestring):
        headers = ', '.join(x.upper() for x in headers)
    if not isinstance(origin, basestring):
        origin = ', '.join(origin)
    if isinstance(max_age, timedelta):
        max_age = max_age.total_seconds()

    def get_methods():
        if methods is not None:
            return methods

        options_resp = current_app.make_default_options_response()
        return options_resp.headers['allow']

    def decorator(f):
        def wrapped_function(*args, **kwargs):
            if automatic_options and request.method == 'OPTIONS':
                resp = current_app.make_default_options_response()
            else:
                resp = make_response(f(*args, **kwargs))
            if not attach_to_all and request.method != 'OPTIONS':
                return resp

            h = resp.headers

            h['Access-Control-Allow-Origin'] = origin
            h['Access-Control-Allow-Methods'] = get_methods()
            h['Access-Control-Max-Age'] = str(max_age)
            if headers is not None:
                h['Access-Control-Allow-Headers'] = headers
            return resp

        f.provide_automatic_options = False
        return update_wrapper(wrapped_function, f)
    return decorator

status = {"ok?": True,
          "mk_is_running?": False}
config_root = os.path.expanduser("~/machinekit/configs")
service_log_path = os.path.expanduser("~/Desktop/services.log")

def clear_services_log():
    global service_log_path
    open(service_log_path, 'w').write('')
clear_services_log()

@app.route("/status")
@crossdomain(origin="*")
def route_status():
    global status
    return edn.dumps(status)

@app.route("/configs", methods=['GET'])
@crossdomain(origin="*")
def route_configs():
    global config_root
    config_dirs = glob("{}/*/".format(config_root))
    files = map(lambda d: glob("{}*".format(d)), config_dirs)
    get_f = lambda s: s.split('/')[-1]
    m_keys = map(lambda p: p.split('/')[-2]+'/', config_dirs)
    m_vals = map(lambda fs: map(get_f, fs), files)
    m = dict(zip(m_keys, m_vals))
    return edn.dumps(m)

@app.route("/configs/<config>/<filename>", methods=['GET', 'PUT', 'DELETE', 'OPTIONS'])
@crossdomain(origin="*")
def route_configs_file(config, filename):
    global config_root
    path = "{}/{}/{}".format(config_root, config, filename)
    if request.method == 'GET':
        txt = open(path).read()
        return edn.dumps({"contents": txt})
    elif request.method == 'PUT':
        fp = open(path, 'w')
        fp.write(request.form.get('contents'))
        return edn.dumps({})
    elif request.method == 'OPTIONS': # http/delete calls options then delete
        return edn.dumps({})
    elif request.method == 'DELETE':
        os.remove(path)
        return edn.dumps({})

@app.route("/services_log", methods=['GET'])
@crossdomain(origin="*")
def route_services_log():
    global service_log_path
    text = open(service_log_path).read()
    return edn.dumps({"log": text})

@app.route("/run_mk", methods=['GET'])
@crossdomain(origin="*")
def route_run_mk():
    global status
    if not status["mk_is_running?"]:
        status["mk_is_running?"] = True
    return edn.dumps(status)

@app.route("/stop_mk", methods=['GET'])
@crossdomain(origin="*")
def route_stop_mk():
    global status
    if status["mk_is_running?"]:
        status["mk_is_running?"] = False
    return edn.dumps(status)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=3001)
