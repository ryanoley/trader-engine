package com.roundaboutam.trader.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import com.roundaboutam.trader.order.CancelOrder;
import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.ReplaceOrder;
import com.roundaboutam.trader.rmp.PriceType;


public class OrderModificationFrame {

	private static JFrame frame;
	private static JPanel panel;

	private static OrderModificationFrame instance = null;
	private transient TraderApplication application = null;
	private static Order order = null;

	JTextField tickerField = new JTextField();
	JTextField limitPriceField = new JTextField("");
	JTextField quantityField = new JTextField("");
	
	public static OrderModificationFrame getInstance(TraderApplication application, Order order) {
		if (instance == null) {
			instance = new OrderModificationFrame(application, order);
		}
		if (!frame.isVisible())
			frame.setVisible(true);
		return instance;
	}

	private OrderModificationFrame(TraderApplication application, Order order) {
		this.application = application;
		this.order = order;
		makeOrderModiciationFrame();
	}

	private void makeOrderModiciationFrame() {

		frame = new JFrame();
		Point traderEngineLoc = TraderEngine.get().getTraderFrame().getLocationOnScreen();
		frame.setLocation(traderEngineLoc.x + 50, traderEngineLoc.y + 50);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Order Modification Ticket");
		frame.setSize(400, 600);
		frame.setResizable(false);

		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;

		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Ticker:"), c);
		c.gridx = 0;
		c.gridy = 1;
		tickerField.setText(order.getSymbol());
		tickerField.setEnabled(false);
		panel.add(tickerField, c);

	    c.gridx = 0;
	    c.gridy = 2;
	    panel.add(new JLabel("Quantity:"), c);
	    c.gridx = 0;
	    c.gridy = 3;
	    quantityField.setText(String.valueOf(order.getQuantity())); 
	    panel.add(quantityField, c);

	    c.gridx = 0;
	    c.gridy = 4;
	    panel.add(new JLabel("Limit Price:"), c);
	    c.gridx = 0;
	    c.gridy = 5;
	    if (order.getPriceType() == PriceType.LIMIT)
	    	limitPriceField.setText(String.valueOf(order.getLimitPrice()));
	    else
	    	enableTextField(limitPriceField, false);
	    panel.add(limitPriceField, c);

	    c.gridx = 0;
	    c.gridy = 6;
	    JButton btnModifySubmit = new JButton("Modify Order");
	    btnModifySubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				makeModifyTransmitOrder();
			}
		});
	    panel.add(btnModifySubmit, c);

	    c.gridx = 0;
	    c.gridy = 7;
	    JButton btnCancelSubmit = new JButton("Cancel Order");
	    btnCancelSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				makeCancelTransmitOrder();
			}
		});
	    panel.add(btnCancelSubmit, c);

	    c.gridx = 0;
	    c.gridy = 8;
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
	
	private void enableTextField(JTextField field, boolean enabled) {
        Color labelColor = enabled ? Color.black : Color.gray;
        Color bgColor = enabled ? Color.white : Color.gray;
        field.setEnabled(enabled);
        field.setBackground(bgColor);
        field.setForeground(labelColor);
    }

	private void makeCancelTransmitOrder() {
        application.cancel(new CancelOrder(order));
		instance = null;
        frame.dispose();
	}

	private void makeModifyTransmitOrder() {
		ReplaceOrder replaceOrder = new ReplaceOrder(order);
		replaceOrder.setQuantity(Integer.parseInt(quantityField.getText()));
        if (order.getPriceType() == PriceType.LIMIT) {
    		replaceOrder.setLimitPrice(Double.parseDouble(limitPriceField.getText()));
        }
        application.replace(replaceOrder);
        instance = null;
        frame.dispose();
	}
}
