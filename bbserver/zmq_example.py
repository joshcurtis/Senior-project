import zmq
from machinetalk.protobuf.message_pb2 import Container
from machinetalk.protobuf.types_pb2 import MT_PING

IDENTITY = 'batman'
PING = Container()
PING.type = MT_PING

def send_ping(port):
    global ping
    global IDENTITY

    context = zmq.Context()
    dealer = context.socket(zmq.DEALER)
    dealer.identity = IDENTITY
    hostname = 'tcp://localhost:' + str(port)
    dealer.connect(hostname)
    dealer.send(PING.SerializeToString())

def test_send_ping():
    context = zmq.Context()
    dealer = context.socket(zmq.DEALER)
    port = dealer.bind_to_random_port('tcp://127.0.0.1')
    send_ping(port)
    buf = dealer.recv()
    msg = Container()
    msg.ParseFromString(buf)
    assert msg == PING

if __name__ == '__main__':
    test_send_ping()
