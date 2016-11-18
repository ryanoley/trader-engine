# Task ventilator
# Binds PUSH socket to tcp://localhost:5557
# Sends batch of tasks to workers via that socket

import zmq
import random
import time

try:
    raw_input
except NameError:
    # Python 3
    raw_input = input

context = zmq.Context()

# Socket to send messages on
sender = context.socket(zmq.PUSH)
sender.connect("tcp://192.168.0.101:5557")

print("Enter order to send : ")
order = input()
print("Sending : %s" %order)

# The first message is "0" and signals start of batch

# Initialize random number generator
# random.seed()

# Send 100 tasks
# total_msec = 0
while (order != "done"):
#for task_nbr in range(100):

    # Random workload from 1 to 100 msecs
 #   workload = random.randint(1, 100)
 #   total_msec += workload

    sender.send_string(order)
    order = raw_input()
#    time.sleep(1)

print("Quitting")

# Give 0MQ time to deliver
time.sleep(1)
