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

import com.roundaboutam.trader.order.Order;

public class OrderTicket {

	private static String[] allowableOrderSides = {"BUY", "SELL", "SHORT"};
	private static String[] allowableOrderTypes = {"LIMIT", "VWAP", "MARKET"};

	private static JFrame frame;
	private static JPanel panel;

	JTextField tickerField = new JTextField();
	JTextField limitPriceField = new JTextField("");
	JTextField quantityField = new JTextField("");
	JTextField participationRateField = new JTextField("12");
	JTextField startTimeField = new JTextField("09:32:00");
	JTextField endTimeField = new JTextField("15:58:00");
	JComboBox<String> orderSideCombo = new JComboBox<String>(allowableOrderSides);
	JComboBox<String> orderTypesCombo = new JComboBox<String>(allowableOrderTypes);

	public OrderTicket() {

		frame = new JFrame();
		frame.setTitle("Order Ticket");
		frame.setSize(300, 400);

		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		//c.insets = new Insets(20, 4, 0, 4);
		//c.ipady = 8;

		c.gridy = 0;
		c.gridx = 0;
		panel.add(new JLabel("Ticker:"), c);
		c.gridy = 1;
		panel.add(tickerField);

	    orderTypesCombo.addItemListener(new OrderTypeListener());
		c.gridy = 2;
	    panel.add(new JLabel("Order Type:"));
		c.gridy = 3;
	    panel.add(orderTypesCombo);

		c.gridy = 4;
	    panel.add(new JLabel("Order Side:"));
		c.gridy = 5;
	    panel.add(orderSideCombo);

	    c.gridy = 6;
	    panel.add(new JLabel("Quantity:"));
	    c.gridy = 7;
	    panel.add(quantityField);

	    c.gridy = 8;
	    panel.add(new JLabel("Limit Price:"));
	    c.gridy = 9;
	    panel.add(limitPriceField);
	    
	    c.gridy = 10;
	    panel.add(new JLabel("Participation Rate:"));
	    c.gridy = 11;
	    panel.add(participationRateField);

	    c.gridy = 12;
	    panel.add(new JLabel("Start Time:"));
	    c.gridy = 13;
	    panel.add(startTimeField);

	    c.gridy = 14;
	    panel.add(new JLabel("End Time:"));
	    c.gridy = 15;
	    panel.add(endTimeField);

	    c.gridy = 16;
	    JButton btnOrderTicket = new JButton("Order Ticket");
		panel.add(btnOrderTicket, c);

	    frame.add(panel);

	    checkFields();

	}
		
	public Order makeOrder() {

		frame.setVisible(true);

		/*
		int result = JOptionPane.showOptionDialog(null, 
                panel, 
                "Order Ticket", 
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, 
                null,
                new String[]{"Submit", "Cancel"},
                "Cancel");

        if (result == JOptionPane.OK_OPTION) {
        	if (tickerField.getText().length() == 0) {
        		System.out.println("JUNK TICKER");
        	} else {
            	confirmSubmit();
            	// Make order here
            	System.out.println();
                System.out.println(orderTypesCombo.getSelectedItem());
                System.out.println(orderSideCombo.getSelectedItem());
                System.out.println(limitPriceField.getText());        		
        	}
        } else {
            System.out.println("Cancelled");
        }
        */
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
