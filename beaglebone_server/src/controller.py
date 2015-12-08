from copy import copy
from os.path import isfile
from subprocess import Popen

class Controller:
    def __init__(self, fnames, root='.', services_commands=[]):
        self.services_running = False
        self.root = root
        self.filenames = copy(fnames)
        self.services_commands = copy(services_commands)
        self.services = []
        # Correctness checks
        for fname in self.filenames:
            fname = '{}/{}'.format(root, fname)
            assert(isfile(fname))

    def is_running_services(self):
        return self.services_running

    def get_services(self):
        return copy(self.services_commands)

    # Starts services for BeBoPr and stuff
    def start_services(self):
        assert(not self.services_running)
        print('Services not implemented.')
        for service_command in self.services_commands:
            self.services.append(Popen(service_command))
        self.services_running = True

    # Stops services started by Controller.stop_services
    def stop_services(self):
        assert(self.services_running)
        for service in self.services:
            service.terminate()
        for service in self.services:
            service.wait()
        self.services = []
        self.services_running = False

    # Resets the services that have been started
    def reset_services(self):
        self.stop_services()
        return self.start_services()

    # return: str[] - List of filenames available for reading and writing
    def get_filenames(self):
        return copy(self.filenames)

    # fname: str - Filename to read contents from
    # return: str - Contents of file
    def get_file_contents(self, fname):
        if fname not in self.filenames:
            raise Exception('File {} not found'.format(fname))
        fname = '{}/{}'.format(self.root, fname)
        contents = open(fname, 'r').read()
        return contents

    # fname: str - Filename to overwrite data to
    # data: str - Contents to write to file
    # return: int - Number of characters written to file
    def set_file_contents(self, fname, data):
        if fname not in self.filenames:
            raise Exception('File {} not found'.format(fname))
        fname = '{}/{}'.format(self.root, fname)
        fp = open(fname, 'w')
        return fp.write(data)
