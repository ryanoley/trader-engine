
import zmq
from gearbox import read_csv


dpath = 'C:/Users/Mitchell/Desktop/test_orders.csv'
df = read_csv(dpath)

# Socket to send messages on
context = zmq.Context()
sender = context.socket(zmq.PUSH)
sender.connect("tcp://127.0.0.1:5557")

print("Press enter to send orders...")
order = raw_input()


for row in df.iterrows():
    order = row[1]['order']
    sender.send_string(order)


sender.close()
context.term()
