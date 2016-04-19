from flask import Flask
import edn_format as edn

from glob import glob
import os.path

app = Flask(__name__)

status = {"ok?": True,
          "mk_is_running?": False}

@app.route("/status")
def route_status():
    return edn.dumps(status)

@app.route("/configs")
def route_configs():
    config_root = os.path.expanduser("~/machinekit/configs")
    config_dirs = glob("{}/*/".format(config_root))
    files = map(lambda d: glob("{}*".format(d)), config_dirs)
    m = dict(zip(config_dirs, files))
    return edn.dumps(m)

@app.route("/run_mk")
def route_run_mk():
    status["mk_is_running?"] = True
    return edn.dumps(status)

if __name__ == '__main__':
    app.run(host='0.0.0.0')
