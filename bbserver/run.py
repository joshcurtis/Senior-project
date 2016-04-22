#!/usr/bin/python

import sys
import os
import subprocess
import importlib
from machinekit import launcher
from time import *


launcher.register_exit_handler()
launcher.set_debug_level(5)
os.chdir(os.path.dirname(os.path.realpath(__file__)))

try: 
	launcher.register_exit_handler()
	launcher.cleanup_session()
	launcher.start_process('configserver -d')
	launcher.start_process('linuxcnc ~/machinekit/configs/ARM.BeagleBone.CRAMPS/CRAMPS_REMOTE.ini -d')
except:
	launcher.end_session()
	sys.exit(1)

while True:
	sleep(1)
	sys.stdout.flush()
	launcher.check_processes()
