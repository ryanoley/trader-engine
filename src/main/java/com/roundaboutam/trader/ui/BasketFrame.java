package com.roundaboutam.trader.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.OrderBasket;


public class BasketFrame {

	private static JFrame frame;
	private static JPanel panel;
	private static BasketFrame instance = null;
	private static OrderBasket orderBasket = null;

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
		frame.setTitle("Order Basket Summary Ticket");
		frame.setSize(800, 600);
		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		
		BasketTablePanel basketTablePanel = new BasketTablePanel(orderBasket);
		panel.add(basketTablePanel);
		

		
	    JPanel summaryPanel = basketSummary(orderBasket);
	    
	    
	    
	    
	    
	    
		c.gridx = 0;
	    c.gridy = 1;
	    JButton btnClose = new JButton("Close");
	    btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				instance = null;
				frame.dispose();
			}
		});
	    panel.add(btnClose, c);
	    frame.add(panel);	    
	    frame.setVisible(true);
	}
	
	private JPanel basketSummary(OrderBasket orderBasket) {
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.LIGHT_GRAY);
		
		int nB = 0;
		int nS = 0;
		int sharesB = 0;
		int sharesS = 0;
		double dollarsB = 0.0;
		double dollarsS = 0.0;

		
		
		return panel;
	}

}






