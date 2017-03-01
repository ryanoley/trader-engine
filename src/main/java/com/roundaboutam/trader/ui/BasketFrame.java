package com.roundaboutam.trader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.roundaboutam.trader.order.OrderBasket;


public class BasketFrame {

	private static JFrame frame;
	private static JPanel panel;
	private static BasketFrame instance = null;
	private OrderBasket orderBasket = null;

	public static BasketFrame getInstance(OrderBasket orderBasket) {
		if (instance == null) {
			instance = new BasketFrame(orderBasket);
		}
		if (!frame.isVisible())
			frame.setVisible(true);
		return instance;
	}

	private BasketFrame(OrderBasket orderBasket) {
		this.orderBasket = orderBasket;
		makeOrderBasketFrame();
	}

	private void makeOrderBasketFrame() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Order Basket Summary");
		frame.setSize(800, 600);
		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx=0;
		c.gridy=0;
		c.gridwidth = 2;
		c.weighty = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		BasketTable basketTable = new BasketTable(orderBasket);
		panel.add(new JScrollPane(basketTable), c);

	    c.gridy = 1;
		c.weighty = .25;
	    JTable summaryTable = basketSummaryPanel(orderBasket);
	    panel.add(new JScrollPane(summaryTable), c);
	    
	    c = new GridBagConstraints();
	    c.gridy = 2;
		c.weighty = .1;
		c.weightx = 1;
	    JButton submitButton = getSubmitButton();
	    panel.add(submitButton, c);

	    c.gridx = 1;
	    JButton closeButton = getCloseButton();
	    panel.add(closeButton, c);
	    
	    frame.add(panel);
	    frame.setVisible(true);
	}

	private JTable basketSummaryPanel(OrderBasket orderBasket) {
		String[] columns = new String[] {"BasketSummary", "BY", "SS", "BTC", "SL"};
		
		Object[][] data = new Object[][] {
			{"nOrders", orderBasket.nBY, orderBasket.nSS, orderBasket.nBTC, orderBasket.nSL },
			{"nShares", orderBasket.shrBY, orderBasket.shrSS, orderBasket.shrBTC, orderBasket.shrSL },
			};
			
		JTable table = new JTable(data, columns);
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
	    JButton btnSubmit = new JButton("Submit");
	    btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				confirmAndSubmit();
			}
		});
	    return btnSubmit;
	}

	private void confirmAndSubmit() {
		int choice = JOptionPane.showOptionDialog(null, "Submit basket to exchange?", "Confirm?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (choice == JOptionPane.YES_OPTION) {
            orderBasket.sendOrders();
			instance = null;
			frame.dispose();
		}
	}


}


