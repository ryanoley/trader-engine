#
#   Weather update client
#   Connects SUB socket to tcp://localhost:5556
#   Collects weather updates and finds avg temp in zipcode
#

import sys
import zmq

#  Socket to talk to server
context = zmq.Context()
socket = context.socket(zmq.SUB)

socket.connect("tcp://192.168.0.101:5558")

socket.setsockopt_string(zmq.SUBSCRIBE, "")

# Process 5 updates
total_temp = 0
count = 0
while True:
    string = socket.recv_string()
    print(string)
    count = count + 1
    print(count)

