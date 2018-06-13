package com.roundaboutam.trader.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
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
import com.roundaboutam.trader.TraderEngine;
import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.ramfix.OrderOpenClose;
import com.roundaboutam.trader.ramfix.OrderTIF;
import com.roundaboutam.trader.rmp.OrderSide;
import com.roundaboutam.trader.rmp.PriceType;

import quickfix.SessionID;

public class OrderTicketFrame {

	private static String[] allowableOrderTypes = {"LIMIT", "VWAP", "MARKET", "MOC", "LOC"};

	private static JFrame frame;
	private static JPanel panel;

	private static OrderTicketFrame instance = null;
	private transient TraderApplication application = null;

	JTextField symbolField = new JTextField();
	JTextField limitPriceField = new JTextField("");
	JTextField quantityField = new JTextField("");
	JTextField participationRateField = new JTextField("12");
	JTextField startTimeField = new JTextField("09:32:00");
	JTextField endTimeField = new JTextField("15:58:00");
	JTextField customTagField = new JTextField("");
	JComboBox<OrderSide> orderSideCombo = new JComboBox(OrderSide.toArray());
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
		Point traderEngineLoc = TraderEngine.get().getTraderFrame().getLocationOnScreen();
		frame.setLocation(traderEngineLoc.x + 200, traderEngineLoc.y + 200);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Order Ticket");
		frame.setSize(400, 300);
		frame.setResizable(true);

		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Ticker:"), c);

		c.gridx = 0;
		c.gridy = 1;
		panel.add(symbolField, c);

	    c.insets = new Insets(0,5,0,0);
	    c.gridx = 1;
	    c.gridy = 0;
	    panel.add(new JLabel("Order Type:"), c);

	    orderTypesCombo.addItemListener(new OrderTypeListener());
	    c.insets = new Insets(0,5,0,0);
	    c.gridx = 1;
	    c.gridy = 1;
	    panel.add(orderTypesCombo, c);

	    c.insets = new Insets(0,5,0,0);
	    c.gridx = 2;
	    c.gridy = 0;
	    panel.add(new JLabel("Order Side:"), c);

	    c.insets = new Insets(0,5,0,0);
	    c.gridx = 2;
	    c.gridy = 1;
	    panel.add(orderSideCombo, c);

	    c.insets = new Insets(10,0,0,0);
	    c.gridx = 0;
	    c.gridy = 3;
	    panel.add(new JLabel("Quantity:"), c);

	    c.insets = new Insets(0,0,0,0);
	    c.gridx = 0;
	    c.gridy = 4;
	    panel.add(quantityField, c);

	    c.insets = new Insets(10,5,0,0);
	    c.gridx = 1;
	    c.gridy = 3;
	    panel.add(new JLabel("Limit Price:"), c);

	    c.insets = new Insets(0,5,0,0);
	    c.gridx = 1;
	    c.gridy = 4;
	    panel.add(limitPriceField, c);

	    c.insets = new Insets(10,0,0,0);
	    c.gridx = 0;
	    c.gridy = 5;
	    panel.add(new JLabel("Participation Rate:"), c);

	    c.insets = new Insets(0,0,0,0);
	    c.gridx = 0;
	    c.gridy = 6;
	    panel.add(participationRateField, c);

	    c.gridx = 1;
	    c.gridy = 5;
	    c.insets = new Insets(10,5,0,0);
	    panel.add(new JLabel("Start Time:"), c);
	    
	    c.gridx = 1;
	    c.gridy = 6;
	    c.insets = new Insets(0,5,0,0);
	    panel.add(startTimeField, c);

	    c.gridx = 2;
	    c.gridy = 5;
	    c.insets = new Insets(10,5,0,0);
	    panel.add(new JLabel("End Time:"), c);
	    
	    c.gridx = 2;
	    c.gridy = 6;
	    c.insets = new Insets(0,5,0,0);
	    panel.add(endTimeField, c);
	    
	    c.gridx = 0;
	    c.gridy = 7;
	    c.insets = new Insets(10,0,0,0);
	    panel.add(new JLabel("Custom Tag:"), c);

	    c.gridx = 0;
	    c.gridy = 8;
	    c.insets = new Insets(0,0,0,0);
	    panel.add(customTagField, c);

	    c.gridx = 1;
	    c.gridy = 7;
	    c.gridwidth = 2;
	    c.insets = new Insets(10,5,0,0);
	    panel.add(new JLabel("Session ID:"), c);
	    
	    c.gridx = 1;
	    c.gridy = 8;
	    c.insets = new Insets(0,5,0,0);
	    panel.add(sessionIDCombo, c);

	    c.gridx = 0;
	    c.gridy = 9;
	    c.ipady = 10;
	    c.gridwidth = 1;
	    c.insets = new Insets(10,0,0,0);
	    submitButton = new JButton("Submit");
	    submitButton.setEnabled(false);
	    submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				makeTransmitOrder();
			}
		});
	    panel.add(submitButton, c);

	    c.gridx = 1;
	    c.gridy = 9;
	    c.ipady = 10;
	    c.insets = new Insets(10,5,0,0);
	    JButton btnClose = new JButton("Close");
	    btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				instance = null;
				frame.dispose();
			}
		});
	    panel.add(btnClose, c);
	    
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
		sessionIDCombo.addItem(application.getSessionID());
		// Enable certain fields dependent on OrderType
        String item = (String) orderTypesCombo.getSelectedItem();
        if (item == "MARKET" | item == "MOC") {
            enableTextField(limitPriceField, false);
            enableTextField(participationRateField, false);
            enableTextField(startTimeField, false);
            enableTextField(endTimeField, false);
        } else if (item == "LIMIT" | item == "LOC") {
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

    private void activateSubmit() {
        sessionEntered = sessionIDCombo.getSelectedItem() != null;
        String priceType = (String) orderTypesCombo.getSelectedItem();
        boolean activate = symbolEntered && quantityEntered && sessionEntered;
        if (priceType == "MARKET" | priceType == "MOC")
            submitButton.setEnabled(activate);
        else if (priceType == "LIMIT" | priceType == "LOC")
            submitButton.setEnabled(activate && limitEntered);
        else if (priceType == "VWAP")
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

        OrderSide orderSide = (OrderSide) orderSideCombo.getSelectedItem();
        OrderOpenClose orderOpenClose = null;
		if (orderSide == OrderSide.BUY | orderSide == OrderSide.SHORT_SELL) {
			orderOpenClose = OrderOpenClose.OPEN;
		} else if (orderSide == OrderSide.BUY_TO_COVER) {
			orderSide = OrderSide.BUY;
			orderOpenClose = OrderOpenClose.CLOSE;
		} else if (orderSide == OrderSide.SELL) {
			orderOpenClose = OrderOpenClose.CLOSE;		
		}

        String orderTypeText = (String) orderTypesCombo.getSelectedItem();
        Order order = new Order();

        if (orderTypeText.equals("VWAP")) {
            order.setVwapFlag(true);
            order.setStartTime(startTimeField.getText());
            order.setEndTime(endTimeField.getText());
            order.setParticipationRate(Integer.parseInt(participationRateField.getText()));
            order.setPriceType(PriceType.VWAP);
        } else if (orderTypeText.equals("LIMIT")) {
            order.setLimitPrice(Double.parseDouble(limitPriceField.getText()));
            order.setPriceType(PriceType.LIMIT);
        } else if (orderTypeText.equals("LOC")) {
            order.setLimitPrice(Double.parseDouble(limitPriceField.getText()));
            order.setPriceType(PriceType.LIMIT_ON_CLOSE);
        } else if (orderTypeText.equals("MARKET")) {
            order.setPriceType(PriceType.MARKET);
        } else if (orderTypeText.equals("MOC")) {
            order.setPriceType(PriceType.MARKET_ON_CLOSE);
        }
        order.setSymbol(ticker);
        order.setQuantity(quantity);
        order.setOrderSide(orderSide);
        order.setCustomTag(customTag);
        order.setOrderTIF(OrderTIF.DAY);
        order.setSessionID(sessionID);
        order.setOrderOpenClose(orderOpenClose);
        confirmAndSubmit(order);
	}

	private void confirmAndSubmit(Order order) {
		StringJoiner joiner = new StringJoiner(" ");
		joiner.add(order.getOrderSide().toString());
		joiner.add("(" + order.getOrderOpenClose().toString() + ")");
		joiner.add("(" + order.getPriceType().toString() + ")");
		joiner.add(Integer.toString(order.getQuantity()));
		joiner.add(order.getSymbol() +"?");
		int choice = JOptionPane.showOptionDialog(frame, joiner.toString(), "Confirm?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (choice == JOptionPane.YES_OPTION) {
            application.sendNewOrder(order);
			instance = null;
			frame.dispose();
		}
	}

}
