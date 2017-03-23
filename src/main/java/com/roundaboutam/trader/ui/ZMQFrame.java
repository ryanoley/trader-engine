package com.roundaboutam.trader.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.TraderEngine;

public class ZMQFrame {

	private static JFrame frame;
	private static JPanel panel;
	private static JTextField portField = new JTextField("5555");
	private static ZMQFrame instance = null;
	private transient TraderApplication application = null;
	private JButton zmqButton= null;

	
	public static ZMQFrame getInstance(JButton zmqButton, TraderApplication application) {
		if (instance == null) {
			instance = new ZMQFrame(zmqButton, application);
			return instance;
		}
		if (!frame.isVisible())
			frame.setVisible(true);
		return instance;
	}

	private ZMQFrame(JButton zmqButton, TraderApplication application) {
		this.application = application;
		this.zmqButton = zmqButton;
		makeZMQFrame();
	}


	private void makeZMQFrame() {
		frame = new JFrame();
		Point traderEngineLoc = TraderEngine.get().getTraderFrame().getLocationOnScreen();
		frame.setLocation(traderEngineLoc.x + 250, traderEngineLoc.y + 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("ZMQ");
		frame.setSize(200, 200);
		frame.setResizable(false);

		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;

		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Port:"), c);
	    c.insets = new Insets(0, 5, 0, 0);
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		panel.add(portField, c);

	    c.gridx = 0;
	    c.gridy = 3;
	    c.gridwidth = 3;
	    c.insets = new Insets(20, 0, 5, 0);
	    JButton btnStart = new JButton("Start Server");
	    btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int port = Integer.parseInt(portField.getText());
				startZMQ(port);
			}
		});
	    panel.add(btnStart, c);

	    c.insets = new Insets(10, 0, 0, 0);
	    c.gridx = 0;
	    c.gridy = 5;
	    JButton btnStop = new JButton("Stop Server");
	    btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stopZMQ();
			}
		});
	    panel.add(btnStop, c);
	    frame.add(panel);
	    if (application.getZMQServerStatus())
	    	btnStart.setEnabled(false);
	    else
	    	btnStop.setEnabled(false);
	    frame.setVisible(true);
	}
	
	private void startZMQ(Integer port){
		application.startZMQServer(port);
		zmqButton.setText("Stop ZMQ");
		instance = null;
		frame.dispose();
	}
	
	private void stopZMQ(){
		application.stopZMQServer();
		zmqButton.setText("Start ZMQ");
		instance = null;
		frame.dispose();
	}
}
