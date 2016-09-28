package network;

import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import org.zeromq.ZMQ;

public class ZMQTradeServer {
	
	public static void main (String[] args) {
		ZMQTradeServer server = new ZMQTradeServer();
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		server.startTradePublisher();
		server.startServer();
		
	}
	
	static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

	
	public void startServer () {		
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket socket = context.socket(ZMQ.PULL);
		
		socket.bind("tcp://*:5557");	

		while (true) {
            String message = new String(socket.recv(0)).trim();
            
            System.out.println("Got msg : \t" + message);
            queue.offer(message);
		}

	}
	
	public void startTradePublisher() {
		new TradePublisher().start();
	}
	
	private class TradePublisher extends Thread {
		public void run() {
			ZMQ.Context context = ZMQ.context(1);
			ZMQ.Socket socket = context.socket(ZMQ.PUB);
			
			socket.bind("tcp://*:5558");
			System.out.println("started");
			while (true) {
				try {
					String reply = "filled:\t" + queue.take();
					System.out.println(reply);
					socket.send(reply);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
