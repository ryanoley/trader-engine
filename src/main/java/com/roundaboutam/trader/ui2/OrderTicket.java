package com.roundaboutam.trader.ui2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.order.Order;

public class OrderTicket {

	private static String[] allowableOrderSides = {"BUY", "SELL", "SHORT"};
	private static String[] allowableOrderTypes = {"LIMIT", "VWAP", "MARKET"};

	private static JFrame frame;
	private static JPanel panel;

	private static OrderTicket instance = null;
	private transient TraderApplication application = null;

	JTextField tickerField = new JTextField();
	JTextField limitPriceField = new JTextField("");
	JTextField quantityField = new JTextField("");
	JTextField participationRateField = new JTextField("12");
	JTextField startTimeField = new JTextField("09:32:00");
	JTextField endTimeField = new JTextField("15:58:00");
	JComboBox<String> orderSideCombo = new JComboBox<String>(allowableOrderSides);
	JComboBox<String> orderTypesCombo = new JComboBox<String>(allowableOrderTypes);

	public static OrderTicket getInstance(TraderApplication application) {
		if (instance == null) {
			System.out.println("MAKING NEW ORDER TICKET");
			instance = new OrderTicket(application);
			return instance;
		}
		System.out.println("Returning instance");
		if (!frame.isVisible())
			frame.setVisible(true);
		return instance;
	}

	private OrderTicket(TraderApplication application) {
		this.application = application;
		makeOrderTicketFrame();
	}

	private void makeOrderTicketFrame() {

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Order Ticket");
		frame.setSize(400, 600);
		frame.setResizable(false);

		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		//c.insets = new Insets(20, 4, 0, 4);
		//c.ipady = 8;

		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Ticker:"), c);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(tickerField, c);

	    orderTypesCombo.addItemListener(new OrderTypeListener());
	    c.gridx = 0;
	    c.gridy = 2;
	    panel.add(new JLabel("Order Type:"), c);
	    c.gridx = 0;
	    c.gridy = 3;
	    panel.add(orderTypesCombo, c);

	    c.gridx = 0;
	    c.gridy = 4;
	    panel.add(new JLabel("Order Side:"), c);
	    c.gridx = 0;
	    c.gridy = 5;
	    panel.add(orderSideCombo, c);

	    c.gridx = 0;
	    c.gridy = 6;
	    panel.add(new JLabel("Quantity:"), c);
	    c.gridx = 0;
	    c.gridy = 7;
	    panel.add(quantityField, c);

	    c.gridx = 0;
	    c.gridy = 8;
	    panel.add(new JLabel("Limit Price:"), c);
	    c.gridx = 0;
	    c.gridy = 9;
	    panel.add(limitPriceField, c);
	    
	    c.gridx = 0;
	    c.gridy = 10;
	    panel.add(new JLabel("Participation Rate:"), c);
	    c.gridx = 0;
	    c.gridy = 11;
	    panel.add(participationRateField, c);

	    c.gridx = 0;
	    c.gridy = 12;
	    panel.add(new JLabel("Start Time:"), c);
	    c.gridx = 0;
	    c.gridy = 13;
	    panel.add(startTimeField, c);

	    c.gridx = 0;
	    c.gridy = 14;
	    panel.add(new JLabel("End Time:"), c);
	    c.gridx = 0;
	    c.gridy = 15;
	    panel.add(endTimeField, c);

	    c.gridx = 0;
	    c.gridy = 16;
	    JButton btnOrderTicket = new JButton("Order Ticket");
	    btnOrderTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				makeTransmitOrder();
			}
		});
	    panel.add(btnOrderTicket, c);

	    checkFields();

	    frame.add(panel);	    
	    frame.setVisible(true);
	}
		
	public Order makeTransmitOrder() {

        String ticker = tickerField.getText();
        String orderType = (String) orderTypesCombo.getSelectedItem();
        String orderSide = (String) orderSideCombo.getSelectedItem();
        String limitPrice = limitPriceField.getText();

        //application.send(order);
		return null;
	}

	private class OrderTypeListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
        	checkFields();
        }
	}
	
	private void checkFields() {
        String item = (String) orderTypesCombo.getSelectedItem();
        if (item == "MARKET") {
            enableTextField(limitPriceField, false);
            enableTextField(participationRateField, false);
            enableTextField(startTimeField, false);
            enableTextField(endTimeField, false);
        } else if (item == "LIMIT") {
            enableTextField(limitPriceField, true);
            enableTextField(participationRateField, false);
            enableTextField(startTimeField, false);
            enableTextField(endTimeField, false);
        } else {
        	// VWAP
            enableTextField(limitPriceField, false);
            enableTextField(participationRateField, true);
            enableTextField(startTimeField, true);
            enableTextField(endTimeField, true);
        }
	}

	private void enableTextField(JTextField field, boolean enabled) {
        Color labelColor = enabled ? Color.black : Color.gray;
        Color bgColor = enabled ? Color.white : Color.gray;
        field.setEnabled(enabled);
        field.setBackground(bgColor);
        field.setForeground(labelColor);
    }

	private void confirmSubmit() {
		int choice = JOptionPane.showOptionDialog(null,"Press enter to send", "Confirm?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (choice != JOptionPane.YES_OPTION)
			return;
	}

}
