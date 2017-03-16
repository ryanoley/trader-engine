package com.roundaboutam.trader.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.JTableHeader;

import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.TraderEngine;
import com.roundaboutam.trader.order.OrderBasket;


public class BasketInfoFrame {

	private transient TraderApplication application = null;
	private static JFrame frame;
	private static JPanel panel;
	private static BasketInfoFrame instance = null;
	private OrderBasket orderBasket;
    JButton submitButton;
    JButton cancelAllButton;

	public static BasketInfoFrame getInstance(OrderBasket orderBasket, TraderApplication application) {
		if (instance == null) {
			instance = new BasketInfoFrame(orderBasket, application);
		}
		if (!frame.isVisible())
			frame.setVisible(true);
		return instance;
	}

	private BasketInfoFrame(OrderBasket orderBasket, TraderApplication application) {
		this.orderBasket = orderBasket;
		this.application = application;
		makeOrderBasketFrame();
		activateButtons();
	}

	private void makeOrderBasketFrame() {
		frame = new JFrame();
		Point traderEngineLoc = TraderEngine.get().getTraderFrame().getLocationOnScreen();
		frame.setLocation(traderEngineLoc.x + 50, traderEngineLoc.y + 50);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Order Basket Summary");
		frame.setSize(600, 400);
		panel = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		c.gridwidth = 3;
		c.weighty = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		BasketDetailTable basketTable = new BasketDetailTable(orderBasket);
		panel.add(new JScrollPane(basketTable), c);

	    c.gridy = 1;
		c.weighty = .25;
	    JTable summaryTable = basketSummaryPanel(orderBasket);
	    panel.add(new JScrollPane(summaryTable), c);

	    c = new GridBagConstraints();
	    c.gridy = 2;
		c.weighty = .1;
		c.weightx = 1;
	    submitButton = getSubmitButton();
	    panel.add(submitButton, c);

	    c.gridx = 1;
	    JButton closeButton = getCloseButton();
	    panel.add(closeButton, c);

	    c.gridx = 2;
	    cancelAllButton = getCancelAllButton();
	    panel.add(cancelAllButton, c);

	    frame.add(panel);
	    frame.setVisible(true);
	}
	
	private void activateButtons() {
		if (orderBasket.isFilled()) {
			submitButton.setEnabled(false);
			cancelAllButton.setEnabled(false);
		} else if (orderBasket.isLive()) {
			submitButton.setEnabled(false);
			cancelAllButton.setEnabled(true);
		} else if (orderBasket.isStaged()) {
			submitButton.setEnabled(true);
			cancelAllButton.setEnabled(false);
		} else {
			submitButton.setEnabled(false);
			cancelAllButton.setEnabled(false);
		}
	}

	private JTable basketSummaryPanel(OrderBasket orderBasket) {
		String[] columns = new String[] {"BasketSummary", "BY", "SS", "BTC", "SL"};

		Object[][] data = new Object[][] {
			{"Orders", orderBasket.nBY, orderBasket.nSS, orderBasket.nBTC, orderBasket.nSL },
			{"Shares", orderBasket.shrBY, orderBasket.shrSS, orderBasket.shrBTC, orderBasket.shrSL },
			{"ExecShares", orderBasket.shrBYExec, orderBasket.shrSSExec, orderBasket.shrBTCExec, orderBasket.shrSLExec },
			};
		JTable table = new JTable(data, columns);
		JTableHeader header = table.getTableHeader();
		header.setOpaque(false);
		header.setBackground(Color.lightGray);
		header.setBorder(new MatteBorder(0, 0, 1, 0, Color.black));
		return table;
	}
	
	private JButton getCloseButton() {
	    JButton btnClose = new JButton("Close");
	    btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				instance = null;
				frame.dispose();
			}
		});
	    return btnClose;
	}
	
	private JButton getSubmitButton() {
		submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				confirmAndSubmit();
			}
		});
	    return submitButton;
	}

	private void confirmAndSubmit() {
		int choice = JOptionPane.showOptionDialog(frame, "Submit basket to exchange?", "Confirm?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (choice == JOptionPane.YES_OPTION) {
            application.sendBasket(orderBasket);
			orderBasket.setStaged(false);
			orderBasket.setLive(true);
			instance = null;
			frame.dispose();
		}
	}
	
	private JButton getCancelAllButton() {
		cancelAllButton = new JButton("Cancel All Trades");
		cancelAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancelAllTrades();
			}
		});
	    return cancelAllButton;
	}
	
	private void cancelAllTrades() {
		int choice = JOptionPane.showOptionDialog(frame, "Cancel All Orders in Basket", "Confirm?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (choice == JOptionPane.YES_OPTION) {
			application.cancelBasket(orderBasket);
			instance = null;
			frame.dispose();
		}
	}
	
}


