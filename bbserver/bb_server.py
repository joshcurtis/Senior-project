from flask import Flask
import edn_format as edn

from glob import glob

@app.route("/status")
def route_ok():
    return edn.dumps({"ok?": True,})

@app.route("/configs")
def route_configs():
    config_root = "machinekit/configs"
    config_dirs = glob("{}/*/".format(config_root))
    files = map(lambda d: glob("{}*".format(d)), config_dirs)
    m = dict(zip(config_dirs, files))
    return edn.dumps(m)

if __name__ == '__main__':
    app.run(host='0.0.0.0')
