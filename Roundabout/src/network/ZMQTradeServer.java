package network;

import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import fix.FixApplication;
import ui.MainFrame;

public class ZMQTradeServer {

	public static void main (String[] args) {
		ZMQTradeServer server = ZMQTradeServer.getZMQTradeServer();

		JFrame frame = new MainFrame();
		frame.setVisible(true);

		server.startTradePublisher();
		server.startServer();

		FixApplication.getFixApplication().connectToServer();		
	}



	static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();


	private static ZMQTradeServer server = null;

	public static ZMQTradeServer getZMQTradeServer() {
		if (server == null)
			server = new ZMQTradeServer();
		return server;
	}

	private ZMQTradeServer() {
	}

	public void disconnect() {
		if (tradeSocket != null)
			tradeSocket.close();
		if (tradeContext != null)
			tradeContext.term();

		if (pubSocket != null)
			pubSocket.close();
		if (pubContext != null)
			pubContext.term();
	}


	ZMQ.Context tradeContext = null;	
	ZMQ.Socket tradeSocket = null;
	ZMQ.Context pubContext = null;
	ZMQ.Socket pubSocket = null;

	public void startServer () {		
		tradeContext = ZMQ.context(1);
		tradeSocket = tradeContext.socket(ZMQ.PULL);

		tradeSocket.bind("tcp://*:5557");	
		System.out.println("trade listener started");
		String message = null;

		while (true) {
			try {
				message = new String(tradeSocket.recv(0)).trim();
			} catch (ZMQException e) {
				return;
			}

			System.out.println("Got msg : \t" + message);
			queue.offer(message);
		}

		//		System.out.println("ENDING TRADE LISTENER.");
	}

	public void startTradePublisher() {
		new TradePublisher().start();
	}

	private class TradePublisher extends Thread {
		public void run() {
			pubContext = ZMQ.context(1);
			pubSocket = pubContext.socket(ZMQ.PUB);

			pubSocket.bind("tcp://*:5558");
			System.out.println("trade pub started");
			while (true) {
				try {
					String reply = "filled:\t" + queue.take();
					System.out.println(reply);
					pubSocket.send(reply);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void notifyTrade(String ticker, int orderSize, double price,
			char side) {
		// TODO 

	}
}
