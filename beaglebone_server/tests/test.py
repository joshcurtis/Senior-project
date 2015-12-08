import requests, json, string

url = 'http://127.0.0.1:5000'
fnames = ['alphabet.txt', 'test.ini','testfile.txt', 'testfile_copy.txt']

r = requests.get(url + '/files')
assert(fnames == json.loads(r.text)['files'])


r = requests.get(url + '/files/alphabet.txt')
assert(string.ascii_lowercase+'\n' == json.loads(r.text)['data'])


r = requests.get(url + '/files/testfile.txt')
testfile = json.loads(r.text)['data']

r = requests.get(url + '/files/testfile_copy.txt')
testfile_copy = json.loads(r.text)['data']

assert(testfile == testfile_copy)

content = 'Hello World!'
requests.put(url + '/files/testfile.txt', data=content).text
r = requests.get(url + '/files/testfile.txt')
assert(content == json.loads(r.text)['data'])

content = testfile_copy
requests.put(url + '/files/testfile.txt', data=content).text
r = requests.get(url + '/files/testfile.txt')
assert(content == json.loads(r.text)['data'])
