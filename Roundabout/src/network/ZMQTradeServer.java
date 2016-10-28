package network;

import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import fix.FixApplication;
import fix.TradeSender;
import ui.MainFrame;

public class ZMQTradeServer {

	public static void main (String[] args) {
		ZMQTradeServer server = ZMQTradeServer.getZMQTradeServer();

		JFrame frame = new MainFrame();
		frame.setVisible(true);

		server.startTradePublisher();
		server.startServer();

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
		try {
			if (tradeSocket != null)
				tradeSocket.close();
			if (tradeContext != null)
				tradeContext.term();

			if (pubSocket != null)
				pubSocket.close();
			if (pubContext != null)
				pubContext.term();
		} catch (Exception e) {

		}
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
		
		TradeSender sender = TradeSender.getTradeCreator();

		while (true) {
			try {
				message = new String(tradeSocket.recv(0)).trim();
			} catch (ZMQException e) {
				return;
			}

			System.out.println("Got msg : \t" + message);
//			queue.offer(message);
			// MSFT B 100 95.5 <ID> [DEST] [VWAP PARAMS: PERCVOLUME STARTTIME ENDTIME] if there's a startTime, endTime is needed
			String[] parts = message.split("\\s|\\,");
			
			System.out.println("parts:\t" + parts.length);
			
			if (parts.length < 4)
				continue;
			
			String ticker = parts[0];
			
			char sideChar = '1';
			if (parts[1].equalsIgnoreCase("S"))
				sideChar = '2';
			else 
				if (parts[1].equalsIgnoreCase("SS"))
					sideChar = '5';
			
			double size = Double.parseDouble(parts[2]);
			double limit = Double.parseDouble(parts[3]);
			char ordType = '1';				// negative limit = MOC, 0 = MKT
			if (limit > 0)
				ordType = '2';
			else if (limit < 0)
				ordType = '5';
			
			System.out.println("Sending");
			sender.createAndSendOrder(parts[0], sideChar, size, ordType, limit, null);
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

	public void notifyTrade(String ticker, int orderSize, double price, char side) {
		queue.offer(ticker + "," + side + "," + orderSize + "," + price);
	}
}
