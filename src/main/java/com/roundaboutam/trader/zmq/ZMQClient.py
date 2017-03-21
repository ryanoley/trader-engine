#
#   Connects REQ socket to tcp://localhost:5555
#


def getZMQSocket(ZmqContext, addr="tcp://localhost", port=5555):
    """
    Returns a smq socket connected to the Trader Endinge application
    that can send/recieve messages.
    """
    print("Connecting to Trader Engine") 
    socket = ZmqContext.socket(zmq.REQ)
    socket.connect(addr + ":" + str(port))
    print("Sending Connect Request")
    connect_str = b"1=RMP|3=ORC|4=PYSENDER|5=TRADERENGINE";
    message = sendSocketMessage(socket, connect_str)
    if (message ==  b"1=RMP|3=ORC|4=TRADERENGINE|5=PYSENDER"):
        print("Succefull Connection Established")
    return socket


def sendSocketMessage(socket, msgString):
    socket.send(msgString)
    message = socket.recv()
    print message
    return message

def sendTestStrings():
    newBasketString = "1=RMP|2=20170313-13:54:44|3=NB|4=PYSENDER|5=TRADERENGINE|6=ParseBasket"
    sendSocketMessage(socket, newBasketString)

    newOrderString = "1=RMP|2=20170313-14:54:44|3=NO|4=PYSENDER|5=TRADERENGINE|6=ParseBasket|7=IBM|8=T|9=BY|10=100|11=M|12=115.20"
    sendSocketMessage(socket, newOrderString)

    newLimitOrderString = "1=RMP|2=20170313-15:54:44|3=NO|4=PYSENDER|5=TRADERENGINE|6=ParseBasket|7=GLD|8=T|9=SL|10=175|11=L|12=117.75";
    sendSocketMessage(socket, newLimitOrderString);
    
    newVWapOrderString = "1=RMP|2=20170313-16:54:44|3=NO|4=PYSENDER|5=TRADERENGINE|6=ParseBasket|7=SPY|8=T|9=BY|10=4000|11=V";
    sendSocketMessage(socket, newVWapOrderString);
    
    newVWapOrder2String = "1=RMP|2=20170313-16:33:44|3=NO|4=PYSENDER|5=TRADERENGINE|6=ParseBasket|7=IWM|8=T|9=SL|10=500|11=V|13=11:15:00|14=11:30:00|15=20";
    sendSocketMessage(socket, newVWapOrder2String);
    
    newVWapOrder3String = "1=RMP|2=20170313-16:33:44|3=NO|4=PYSENDER|5=TRADERENGINE|6=ParseBasket|7=BAC|8=T|9=BY|10=600|11=V|13=11:17:00|14=11:25:00|15=20";
    sendSocketMessage(socket, newVWapOrder3String);
    
    newVWapOrder4String = "1=RMP|2=20170313-16:33:44|3=NO|4=PYSENDER|5=TRADERENGINE|6=ParseBasket|7=FB|8=T|9=SL|10=700|11=V|13=11:12:00|14=11:20:00|15=20";
    sendSocketMessage(socket, newVWapOrder4String);
    
    newToConsoleString = "1=RMP|2=20170313-17:54:44|3=TC|4=PYSENDER|5=TRADERENGINE";
    sendSocketMessage(socket, newToConsoleString);
    return


def main():
    import zmq
    ZmqContext = zmq.Context()

    socket = getZMQSocket(ZmqContext)

    #sendTestStrings()

    socket.close()
    ZmqContext.term()


if __name__ == '__main__':
    main()


