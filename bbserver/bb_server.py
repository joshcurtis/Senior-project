#!/usr/bin/env python2
from flask import Flask, send_from_directory
import edn_format as edn

from glob import glob
import datetime, os, subprocess, zmq, time, random

from machinetalk.protobuf.message_pb2 import Container
from machinetalk.protobuf.types_pb2 import MT_PING


app = Flask(__name__)
app.debug = True

login_username = 'machinekit'
login_password = 'kit'

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
          "mk_running": False,
          "resolving_services": False}

config_root = os.path.expanduser("~/machinekit/configs")
service_log_path = os.path.expanduser("~/Desktop/services.log")
service_log = open(service_log_path, 'a')
mklauncher = None
configserver = None
linuxcnc = None
resolver = None

def clear_services_log():
    global service_log_path
    open(service_log_path, 'w').write('cleared\n')
clear_services_log()

@app.route("/status", methods=['GET'])
@crossdomain(origin="*")
def route_status():
    global status
    return edn.dumps(status)

@app.route("/login", methods=['POST', 'OPTIONS'])
@crossdomain(origin="*")
def route_login():
    global status
    if request.method == 'OPTIONS':
        return edn.dumps({})
    if request.method == 'POST':
        if request.form.get('password') == login_password:
            return '{:authenticated true :username ' + login_username + '}'
        else:
            return '{:authenticated false}'

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

@app.route("/config", methods=['GET', 'PUT', 'DELETE', 'OPTIONS'])
@crossdomain(origin="*")
def route_configs_file():
    global config_root
    path = "{}/{}".format(config_root, request.args.get('path'))
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

@app.route("/resolve", methods=['GET'])
@crossdomain(origin="*")
def route_resolve():
    global service_log
    global resolver
    cmd = 'python /home/machinekit/Desktop/resolve.py'.split()
    stop_process(resolver)
    resolver = subprocess.Popen(cmd, stdout=service_log, stderr=service_log)
    if not status['resolving_services']:
        status['resolving_services'] = True
    return edn.dumps(status)

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
    run_mk()
    if not status["mk_running"]:
        status["mk_running"] = True
    return edn.dumps(status)

def run_mk():
    global mklauncher
    global configserver
    global linuxcnc
    cmd1 = 'configserver /home/machinekit/Desktop -d'.split()
    configserver = subprocess.Popen(cmd1)
    return edn.dumps(status)
    # cmd2 = 'linuxcnc /home/machinekit/machinekit/configs/ARM.BeagleBone.CRAMPS/CRAMPS_REMOTE.ini -d'.split()
    # linuxcnc = subprocess.Popen(cmd2)

@app.route("/stop_mk", methods=['GET'])
@crossdomain(origin="*")
def route_stop_mk():
    global status
    global mklauncher
    global configserver
    global linuxcnc
    stop_process(mklauncher)
    stop_process(configserver)
    stop_process(linuxcnc)
    stop_process(resolver)
    if status["mk_running"]:
        status["mk_running"] = False
    return edn.dumps(status)

def stop_process(process):
    if process != None:
        if process.returncode == None:
            process.terminate()

@app.route("/ping/<port>", methods=['GET'])
@crossdomain(origin="*")
def ping(port):
    send_data(port, MT_PING)
    return edn.dumps(status)

def send_data(port, msg_type):
    msg = Container()
    msg.type = msg_type
    context = zmq.Context()
    dealer = context.socket(zmq.DEALER)
    dealer.identity = 'batman'
    hostname = 'tcp://localhost:' + str(port)

    dealer.connect(hostname)
    dealer.send(msg.SerializeToString())
    return

@app.route("/running", methods=['GET'])
@crossdomain(origin="*")
def running():
    status['mk_running'] = (configserver != None and configserver.poll() == None)
    return edn.dumps(status['mk_running'])

# TODO - "t", gives a large value, currently overwritten in client to improve
# usefulness
sim_pos = 0.0
@app.route("/measure", methods=['GET'])
@crossdomain(origin="*")
def route_measure():
    global sim_pos
    t = time.time() / 1000.0
    sim_pos += 0.1
    if sim_pos > 1.0:
        sim_pos = 0.0
    return edn.dumps({"t": t,
                      "Ext-0": random.random(),
                      "Ext-1": random.random() + 1.0,
                      "Ext-2": random.random() + 2.0,
                      "Axis-0-x": sim_pos,
                      "Axis-0-y": 0.0,
                      "Axis-0-z": 0.0,
                      "Axis-0-a": 0.0,
                      "Axis-1-x": -sim_pos,
                      "Axis-1-y": 0.0,
                      "Axis-1-z": 0.0,
                      "Axis-1-a": 0.0,
                      "Axis-2-x": 0.0,
                      "Axis-2-y": sim_pos,
                      "Axis-2-z": 0.0,
                      "Axis-2-a": 0.0,
                      "Axis-3-x": 0.2*(random.random() - 0.5),
                      "Axis-3-y": 0.2*(random.random() - 0.5),
                      "Axis-3-z": max(0.0, 0.2*(random.random() - 0.5)),
                      "Axis-3-a": 0.0})

@app.route("/js/<path:path>")
def route_js(path):
    return send_from_directory("./../resources/public/js", path)

@app.route("/css/<path:path>")
def route_css(path):
    return send_from_directory("./../resources/public/css", path)

@app.route ("/icons/<path:path>")
def route_icons(path):
    return send_from_directory("./../resources/public/icons", path)

@app.route("/favicon.ico")
def route_favicon():
    return send_from_directory("./../resources/public", "favicon.ico")

@app.route("/")
def route_index():
    return send_from_directory("./../resources/public", "index.html")

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=3001)
