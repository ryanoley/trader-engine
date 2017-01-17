package com.roundaboutam.trader.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.StringJoiner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.OrderSide;
import com.roundaboutam.trader.order.OrderTIF;
import com.roundaboutam.trader.order.OrderType;
import com.roundaboutam.trader.order.VwapOrder;

import quickfix.SessionID;

public class OrderTicketFrame {

	private static String[] allowableOrderSides = {"BUY", "SELL", "SHORT"};
	private static String[] allowableOrderTypes = {"LIMIT", "VWAP", "MARKET"};

	private static JFrame frame;
	private static JPanel panel;

	private static OrderTicketFrame instance = null;
	private transient TraderApplication application = null;

	JTextField symbolField = new JTextField();
	JTextField limitPriceField = new JTextField("");
	JTextField quantityField = new JTextField("");
	JTextField participationRateField = new JTextField("12");
	JTextField startTimeField = new JTextField("14:32:00");
	JTextField endTimeField = new JTextField("20:58:00");
	JTextField customTagField = new JTextField("");
	JComboBox<String> orderSideCombo = new JComboBox<String>(allowableOrderSides);
	JComboBox<String> orderTypesCombo = new JComboBox<String>(allowableOrderTypes);
	JComboBox<SessionID> sessionIDCombo = new JComboBox<SessionID>();

    private boolean symbolEntered = false;
    private boolean quantityEntered = false;
    private boolean limitEntered = false;
    private boolean sessionEntered = false;
    
    JButton submitButton;

	public static OrderTicketFrame getInstance(TraderApplication application) {
		if (instance == null) {
			instance = new OrderTicketFrame(application);
			return instance;
		}
		if (!frame.isVisible())
			frame.setVisible(true);
		return instance;
	}

	private OrderTicketFrame(TraderApplication application) {
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

		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Ticker:"), c);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(symbolField, c);

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
	    panel.add(new JLabel("Custom Tag:"), c);
	    c.gridx = 0;
	    c.gridy = 17;
	    panel.add(customTagField, c);

	    c.gridx = 0;
	    c.gridy = 18;
	    submitButton = new JButton("Submit");
	    submitButton.setEnabled(false);
	    submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				makeTransmitOrder();
			}
		});
	    panel.add(submitButton, c);

	    c.gridx = 0;
	    c.gridy = 19;
	    JButton btnClose = new JButton("Close");
	    btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				instance = null;
				frame.dispose();
			}
		});
	    panel.add(btnClose, c);

	    c.gridx = 0;
	    c.gridy = 20;
	    panel.add(new JLabel("Session ID:"), c);
	    c.gridx = 0;
	    c.gridy = 21;
	    panel.add(sessionIDCombo, c);

	    checkFields();

        SubmitActivator activator = new SubmitActivator();
        symbolField.addKeyListener(activator);
        quantityField.addKeyListener(activator);
        limitPriceField.addKeyListener(activator);
        sessionIDCombo.addItemListener(activator);

        frame.add(panel);	    
	    frame.setVisible(true);

	}

	private class OrderTypeListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
        	checkFields();
        	activateSubmit();
        }
	}

	private void checkFields() {
		// Populate SessionID
		sessionIDCombo.removeAllItems();
		for (SessionID s : application.getSessionIDs())
			sessionIDCombo.addItem(s);
		// Enable certain fields dependent on OrderType
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

	private void confirmAndSubmit(Order order) {
		StringJoiner joiner = new StringJoiner(" ");
		joiner.add(order.getOrderSide().toString().toUpperCase()).add(
				"("+order.getOrderType().toString().toUpperCase()+")").add(
				Integer.toString(order.getQuantity())).add("units of").add(
				order.getSymbol()).add("?");
		int choice = JOptionPane.showOptionDialog(null, joiner.toString(), "Confirm?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (choice == JOptionPane.YES_OPTION) {
            application.send(order);
			instance = null;
			frame.dispose();
		}
	}

    private void activateSubmit() {
        sessionEntered = sessionIDCombo.getSelectedItem() != null;
        String orderType = (String) orderTypesCombo.getSelectedItem();
        boolean activate = symbolEntered && quantityEntered && sessionEntered;
        if (orderType == "MARKET")
            submitButton.setEnabled(activate);
        else if (orderType == "LIMIT")
            submitButton.setEnabled(activate && limitEntered);
        else if (orderType == "VWAP")
            submitButton.setEnabled(activate);
    }
	
    private class SubmitActivator implements KeyListener, ItemListener {
        public void keyReleased(KeyEvent e) {
            Object obj = e.getSource();
            if (obj == symbolField) {
                symbolEntered = testField(obj);
            } else if (obj == quantityField) {
                quantityEntered = testField(obj);
            } else if (obj == limitPriceField) {
                limitEntered = testField(obj);
            }
            activateSubmit();
        }

        public void itemStateChanged(ItemEvent e) {
            activateSubmit();
        }

        private boolean testField(Object o) {
            String value = ((JTextField) o).getText();
            value = value.trim();
            return value.length() > 0;
        }

        public void keyTyped(KeyEvent e) {}

        public void keyPressed(KeyEvent e) {}
    }

	private void makeTransmitOrder() {

		SessionID sessionID = (SessionID) sessionIDCombo.getSelectedItem();
        String ticker = symbolField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        String customTag = customTagField.getText();

        String orderSideText = (String) orderSideCombo.getSelectedItem();
        OrderSide orderSide = null;
        if (orderSideText == "SHORT") {
        	orderSide = OrderSide.SHORT_SELL;
        } else if (orderSideText == "SELL") {
        	orderSide = OrderSide.SELL;
        } else {
        	orderSide = OrderSide.BUY;
        }

        String orderTypeText = (String) orderTypesCombo.getSelectedItem();

        if (orderTypeText == "VWAP") {

        	VwapOrder order = new VwapOrder();

            order.setSymbol(ticker);
            order.setQuantity(quantity);
            order.setOrderType(OrderType.MARKET);
            order.setOrderSide(orderSide);
            order.setStartTime(startTimeField.getText());
            order.setEndTime(endTimeField.getText());
            order.setParticipationRate(Integer.parseInt(participationRateField.getText()));
            order.setCustomTag(customTag);
            order.setOrderTIF(OrderTIF.DAY);
            order.setSessionID(sessionID);
            confirmAndSubmit(order);

        } else if (orderTypeText == "LIMIT") {

        	Order order = new Order();

            order.setSymbol(ticker);
            order.setQuantity(quantity);
            order.setLimitPrice(Double.parseDouble(limitPriceField.getText()));
            order.setOrderType(OrderType.LIMIT);
            order.setOrderSide(orderSide);
            order.setCustomTag(customTag);
            order.setOrderTIF(OrderTIF.DAY);
            order.setSessionID(sessionID);
            confirmAndSubmit(order);

        } else if (orderTypeText == "MARKET") {

        	Order order = new Order();

            order.setSymbol(ticker);
            order.setQuantity(quantity);
            order.setOrderType(OrderType.MARKET);
            order.setOrderSide(orderSide);
            order.setCustomTag(customTag);
            order.setOrderTIF(OrderTIF.DAY);
            order.setSessionID(sessionID);
            confirmAndSubmit(order);

        }
        
	}

}