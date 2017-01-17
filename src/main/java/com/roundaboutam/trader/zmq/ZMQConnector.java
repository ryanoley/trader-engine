package com.roundaboutam.trader.zmq;

import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

public class ZMQConnector {
	
	ZMQ.Context listenerContext = null;
	ZMQ.Socket listenerSocket = null;
	ZMQ.Context publisherContext = null;
	ZMQ.Socket publisherSocket = null;

	public void startOrderListener() {
		new OrderListener().start();
	}

	public void disconnect() {
		try {
			if (listenerSocket != null)
				listenerSocket.close();
			if (listenerContext != null)
				listenerContext.term();

			if (publisherSocket != null)
				publisherSocket.close();
			if (publisherContext != null)
				publisherContext.term();
		} catch (Exception e) {

		}
	}

	private class OrderListener extends Thread {
		public void run() {
			listenerContext = ZMQ.context(1);
			listenerSocket = listenerContext.socket(ZMQ.PULL);
			listenerSocket.bind("tcp://*:5557");
			System.out.println("Trade listener started");
			String message = null;
			while (true) {
				try {
					message = new String(listenerSocket.recv(0)).trim();
				} catch (ZMQException e) {
					return;
				}
			}
		}
	}
}
