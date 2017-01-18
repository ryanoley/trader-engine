package main;

import javax.swing.JFrame;

import network.ZMQTradeServer;
import ui.MainFrame;

public class Main {

	public static void main (String[] args) {
		ZMQTradeServer server = ZMQTradeServer.getZMQTradeServer();

		JFrame frame = new MainFrame();
		frame.setVisible(true);

		server.startTradePublisher();
		server.startServer();

	}

}
