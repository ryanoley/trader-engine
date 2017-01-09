package com.roundaboutam.trader.ui2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;

import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.TraderEngine;
import com.roundaboutam.trader.order.Order;

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
		getContentPane().add(makeControlBanner(), BorderLayout.SOUTH);
	}

	private JPanel makeBanner() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		JTextArea area = new JTextArea("Roundabout AM");
		area.setBackground(Color.LIGHT_GRAY);
		panel.add(area);
		return panel;
	}

	private JPanel makeControlBanner() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		JTextArea area = new JTextArea("Show ZMQ, FIX status");
		area.setBackground(Color.LIGHT_GRAY);
		panel.add(area);
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
		
		JPanel messagesPanel = new JPanel();

		tabbedPane.add("Orders", new OrderTablePanel(this.application));
        tabbedPane.add("Messages", messagesPanel);

        panel.add(tabbedPane);
		return panel;
	}

	private JPanel makeButtonPanel() {
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.LIGHT_GRAY);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(20, 4, 0, 4);
		c.ipady = 12;

		JButton btnOrderTicket = new JButton("Order Ticket");
		btnOrderTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OrderTicket.getInstance(application);
			}
		});
		c.gridx = 0;
		c.gridy = 0;
		panel.add(btnOrderTicket, c);

		JButton btnImportBasket = new JButton("Import Basket");
		c.gridx = 0;
		c.gridy = 1;
		panel.add(btnImportBasket, c);

		JButton btnZmqListener = new JButton("ZMQ Listener");
		c.gridx = 0;
		c.gridy = 2;
		panel.add(btnZmqListener, c);

		JButton btnFIX = new JButton("FIX");
		c.gridx = 0;
		c.gridy = 3;
		panel.add(btnFIX, c);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				callExitDialogueBoxAndShutdown();
			}
		});
		c.gridx = 0;
		c.gridy = 4;
		panel.add(btnExit, c);

		JButton btnCancelAllTrades = new JButton("Cancel All Trades");
		c.gridx = 0;
		c.gridy = 5;
		panel.add(btnCancelAllTrades, c);

		return panel;
	}


}
