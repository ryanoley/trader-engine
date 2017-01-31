package com.roundaboutam.trader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.TraderEngine;

@SuppressWarnings("serial")
public class TraderFrame extends JFrame {

	private transient TraderApplication application = null;
	
	public TraderFrame(TraderApplication application) {
		super();
		this.application = application;
		setTitle("Trader Engine");
		setSize(900, 700);
		addWindowClosingHandler();
		addPanels();
		setResizable(true);
		setVisible(true);
	}

	private void addWindowClosingHandler() {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				callExitDialogueBoxAndShutdown();
			}
		});
	}

	private void callExitDialogueBoxAndShutdown() {
		int choice = JOptionPane.showOptionDialog(null,"Are you sure?", "EXIT?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (choice != JOptionPane.YES_OPTION)
			return;
		TraderEngine.get().shutdown();
	}

	public void addPanels() {
		getContentPane().add(makeBanner(), BorderLayout.NORTH);
		getContentPane().add(makeMainPanel(), BorderLayout.CENTER);
		FIXMonitor fixMonitor = new FIXMonitor(application);
		Thread t = new Thread(fixMonitor);
		t.start();
		getContentPane().add(fixMonitor, BorderLayout.SOUTH);
	}

	private JPanel makeBanner() {

		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		JTextArea area = new JTextArea("Roundabout AM  - ");
		area.setBackground(Color.LIGHT_GRAY);
		panel.add(area, BorderLayout.LINE_START);

		class DigitalClock implements Runnable {
			JTextArea area;
			public DigitalClock(JTextArea area) {
				this.area = area;
			}
			public void run() {
				try {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
					while (true) {
			            area.setText(formatter.format(new Date()) + " (UTC)");
			            Thread.sleep(1000);
			         }
			      }
			      catch (Exception e) { }
			}
		}
		JTextArea clockArea = new JTextArea();
		clockArea.setBackground(Color.LIGHT_GRAY);
		Thread t = new Thread(new DigitalClock(clockArea));
		t.start();
		panel.add(clockArea, BorderLayout.LINE_END);

		return panel;
	}

	private JPanel makeMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel buttonPanel = makeButtonPanel();
		panel.add(buttonPanel, BorderLayout.WEST);

		JPanel orderPanel = makeOrderTablePanel();
		panel.add(orderPanel,BorderLayout.CENTER);

		return panel;
	}

	private JPanel makeOrderTablePanel() {

		JPanel panel = new JPanel(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.add("Orders", new OrderTablePanel(this.application));
        tabbedPane.add("Messages", new MessageTablePanel(this.application));

        panel.add(tabbedPane);
		return panel;
	}

	private JButton btnFIX;

	private JPanel makeButtonPanel() {
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.LIGHT_GRAY);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.ipady = 12;

		btnFIX = new JButton("Start FIX");
		btnFIX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (TraderEngine.get().getInitiatorState()){
					TraderEngine.get().stopInitiator();
					btnFIX.setText("Start FIX");
				} else {
					TraderEngine.get().logon();
					btnFIX.setText("Stop FIX");
				}
			}
		});
		c.gridx = 0;
		c.gridy = 0;
		panel.add(btnFIX, c);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				callExitDialogueBoxAndShutdown();
			}
		});
		c.gridy = 1;
		c.insets = new Insets(20, 4, 40, 4);
		panel.add(btnExit, c);

		JButton btnOrderTicket = new JButton("Order Ticket");
		btnOrderTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OrderTicketFrame.getInstance(application);
			}
		});
		c.gridy = 2;
		c.insets = new Insets(20, 4, 0, 4);
		panel.add(btnOrderTicket, c);

		JButton btnImportBasket = new JButton("Import Basket");
		c.gridy = 3;
		panel.add(btnImportBasket, c);

		JButton btnZmq = new JButton("ZMQ");
		btnZmq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ZMQFrame.getInstance();
			}
		});
		c.gridy = 4;
		panel.add(btnZmq, c);

		JButton btnCancelAllTrades = new JButton("Cancel All Trades");
		btnCancelAllTrades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int choice = JOptionPane.showOptionDialog(null, 
						"Are you sure you want to cancel all open orders?", "CANCEL ALL ORDERS?", 
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (choice == JOptionPane.YES_OPTION)
					application.cancelAllOpenOrders();
			}
		});
		c.gridy = 5;
		c.insets = new Insets(60, 4, 00, 4);
		panel.add(btnCancelAllTrades, c);

		return panel;
	}

}
