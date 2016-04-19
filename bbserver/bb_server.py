#!/usr/bin/env python2
from flask import Flask
import edn_format as edn

from glob import glob
import os.path

app = Flask(__name__)

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

@app.route("/status")
@crossdomain(origin="*")
def route_status():
    return edn.dumps(status)

@app.route("/configs", methods=['GET'])
@crossdomain(origin="*")
def route_configs():
    config_root = os.path.expanduser("~/machinekit/configs")
    config_dirs = glob("{}/*/".format(config_root))
    files = map(lambda d: glob("{}*".format(d)), config_dirs)
    m = dict(zip(config_dirs, files))
    return edn.dumps(m)

@app.route("/run_mk", methods=['GET'])
@crossdomain(origin="*")
def route_run_mk():
    if not status["mk_is_running?"]:
        status["mk_is_running?"] = True
    return edn.dumps(status)

@app.route("/stop_mk", methods=['GET'])
@crossdomain(origin="*")
def route_stop_mk():
    if status["mk_is_running?"]:
        status["mk_is_running?"] = False
    return edn.dumps(status)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=3001)
