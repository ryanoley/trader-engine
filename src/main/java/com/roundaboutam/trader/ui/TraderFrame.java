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
	private FIXMonitor fixMonitor;
	private JButton btnFIX;
	private JButton btnZMQ;
	private JButton btnExport;
	
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


	public void addPanels() {
		getContentPane().add(makeBanner(), BorderLayout.NORTH);
		getContentPane().add(makeMainPanel(), BorderLayout.CENTER);
		fixMonitor = new FIXMonitor(application);
		Thread t = new Thread(fixMonitor);
		t.start();
		getContentPane().add(fixMonitor, BorderLayout.SOUTH);
	}

	private JPanel makeBanner() {

		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);

		class DigitalClock implements Runnable {
			JTextArea area;
			public DigitalClock(JTextArea area) {
				this.area = area;
			}
			public void run() {
				try {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
					while (true) {
			            area.setText(formatter.format(new Date()) + " (New York Local Time)");
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
		panel.add(clockArea, BorderLayout.LINE_START);

		return panel;
	}

	private JPanel makeMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel buttonPanel = makeButtonPanel();
		panel.add(buttonPanel, BorderLayout.WEST);

		JPanel orderPanel = makeTabbedPanel();
		panel.add(orderPanel, BorderLayout.CENTER);

		return panel;
	}

	private JPanel makeTabbedPanel() {

		JPanel panel = new JPanel(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.add("Orders", new OrderTablePanel(this.application));
		tabbedPane.add("Baskets", new BasketTablePanel(this.application));
        tabbedPane.add("Messages", new MessageTablePanel(this.application));

        panel.add(tabbedPane);
		return panel;
	}

	private JPanel makeButtonPanel() {
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.LIGHT_GRAY);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 12;
		c.gridx = 0;

		JButton btnFIX = getFixButton();
		c.gridy = 0;
		c.insets = new Insets(0, 4, 30, 4);
		panel.add(btnFIX, c);

		JButton btnZmq = getZMQButton();
		c.gridy = 1;
		panel.add(btnZmq, c);
		
		JButton btnCancelAllTrades = getCancelAllTradesButton();
		c.gridy = 2;
		panel.add(btnCancelAllTrades, c);

		JButton btnOrderTicket = getOrderTicketButton();
		c.gridy = 3;
		panel.add(btnOrderTicket, c);

		JButton btnExport = getExportTradesButton();
		c.gridy = 4;
		panel.add(btnExport, c);

		JButton btnExit = getExitButton();
		c.gridy = 5;
		panel.add(btnExit, c);
		
		return panel;
	}
	
	private JButton getFixButton() {
		btnFIX = new JButton("Start FIX");
		btnFIX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (TraderEngine.get().getInitiatorState()){
					TraderEngine.get().stopInitiator();
					btnFIX.setText("Start FIX");
					fixMonitor.setSessionID("NOT CONNECTED");
				} else {
					TraderEngine.get().logon();
					btnFIX.setText("Stop FIX");
				}
			}
		});
		return btnFIX;
	}
	
	private JButton getZMQButton() {
		btnZMQ = new JButton("Start ZMQ");
		btnZMQ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ZMQFrame.getInstance(application);
			}
		});
		return btnZMQ;
	}
	
	private JButton getCancelAllTradesButton() {
		JButton btnCancelAllTrades = new JButton("Cancel All Trades");
		btnCancelAllTrades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int choice = JOptionPane.showOptionDialog(TraderEngine.get().getTraderFrame(), 
						"Cancel all open orders?", "CANCEL ALL ORDERS?", JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (choice == JOptionPane.YES_OPTION)
					application.cancelAllOpenOrders();
			}
		});
		return btnCancelAllTrades;
	}
	
	private JButton getOrderTicketButton() {
		JButton btnOrderTicket = new JButton("Order Ticket");
		btnOrderTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OrderTicketFrame.getInstance(application);
			}
		});
		return btnOrderTicket;
	}

	private JButton getExportTradesButton() {
		btnExport = new JButton("Export Trades");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
				String outputLocation = JOptionPane.showInputDialog(TraderEngine.get().getTraderFrame(), 
																	"Export Location", 
																	"\\\\qadirect\\MarketQA\\QUANT\\data\\trader-engine\\trade_reports\\" + timeStamp + "_execs.csv");
				if (outputLocation != null)
					application.writeOrderBookToCSV(outputLocation);
			}
		});
		return btnExport;
	}
	
	private JButton getExitButton() {
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				callExitDialogueBoxAndShutdown();
			}
		});
		return btnExit;
	}

	private void callExitDialogueBoxAndShutdown() {
		int choice = JOptionPane.showOptionDialog(TraderEngine.get().getTraderFrame(), "Are you sure?", "EXIT?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (choice != JOptionPane.YES_OPTION)
			return;
		TraderEngine.get().shutdown();
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

	public void setFixButtonText(String text) {
		btnFIX.setText(text);
	}

	public void setZmqButtonText(String text) {
		btnZMQ.setText(text);
	}
}
