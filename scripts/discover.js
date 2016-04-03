var mdns = require('mdns') // npm install mdns

var outputService = function(service) {
    var record = service.txtRecord;
    var keys = Object.keys(record);
    var s = "{";
    console.log("{");
    for (i = 0; i < keys.length; i++) {
	var k = keys[i];
	var v = record[k];
	s += "\n:" + k + " \"" + v + "\"";
    }
    s += "}";
    console.log(s);
}

// create browser
var machinekitTcp = mdns.makeServiceType('machinekit', 'tcp');
var browser = mdns.createBrowser(machinekitTcp);
browser.on('serviceUp', outputService);

// start
setTimeout(process.exit, 4000);
browser.start();
