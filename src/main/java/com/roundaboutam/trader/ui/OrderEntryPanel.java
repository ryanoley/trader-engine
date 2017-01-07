package com.roundaboutam.trader.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.roundaboutam.trader.DoubleNumberTextField;
import com.roundaboutam.trader.IntegerNumberTextField;
import com.roundaboutam.trader.LogonEvent;
import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.OrderSide;
import com.roundaboutam.trader.order.OrderTIF;
import com.roundaboutam.trader.order.OrderType;

import quickfix.SessionID;

@SuppressWarnings("serial")
public class OrderEntryPanel extends JPanel implements Observer {

	private boolean symbolEntered = false;
    private boolean quantityEntered = false;
    private boolean limitEntered = false;
    private boolean sessionEntered = false;

    private final JTextField symbolTextField = new JTextField();
    private final IntegerNumberTextField quantityTextField = new IntegerNumberTextField();

    private final JComboBox sideComboBox = new JComboBox(OrderSide.toArray());
    private final JComboBox typeComboBox = new JComboBox(OrderType.toArray());
    private final JComboBox tifComboBox = new JComboBox(OrderTIF.toArray());

    private final DoubleNumberTextField limitPriceTextField = new DoubleNumberTextField();

    private final JComboBox<SessionID> sessionComboBox = new JComboBox<SessionID>();

    private final JLabel limitPriceLabel = new JLabel("Limit");
    private final JLabel tifLabel = new JLabel("TIF");

    private final JLabel messageLabel = new JLabel(" ");
    private final JButton submitButton = new JButton("Submit");

    private transient TraderApplication application = null;

    private final GridBagConstraints constraints = new GridBagConstraints();

    public OrderEntryPanel(final TraderApplication application) {

    	setName("OrderEntryPanel");

    	this.application = application;
        application.addLogonObserver(this);

        SubmitActivator activator = new SubmitActivator();
        symbolTextField.addKeyListener(activator);
        quantityTextField.addKeyListener(activator);
        limitPriceTextField.addKeyListener(activator);
        sessionComboBox.addItemListener(activator);

        setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        setLayout(new GridBagLayout());
        createComponents();
    }

    public void addActionListener(ActionListener listener) {
        submitButton.addActionListener(listener);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
        if (message == null || message.equals(""))
            messageLabel.setText(" ");
    }

    public void clearMessage() {
        setMessage(null);
    }

    private void createComponents() {
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;

        int x = 0;
        int y = 0;

        add(new JLabel("Symbol"), x, y);
        add(new JLabel("Quantity"), ++x, y);
        add(new JLabel("Side"), ++x, y);
        add(new JLabel("Type"), ++x, y);
        constraints.ipadx = 30;
        add(limitPriceLabel, ++x, y);
        constraints.ipadx = 0;
        add(tifLabel, ++x, y);
        constraints.ipadx = 30;

        symbolTextField.setName("SymbolTextField");
        add(symbolTextField, x = 0, ++y);
        constraints.ipadx = 0;
        quantityTextField.setName("QuantityTextField");
        add(quantityTextField, ++x, y);
        sideComboBox.setName("SideComboBox");
        add(sideComboBox, ++x, y);
        typeComboBox.setName("TypeComboBox");
        add(typeComboBox, ++x, y);
        limitPriceTextField.setName("LimitPriceTextField");
        add(limitPriceTextField, ++x, y);
        tifComboBox.setName("TifComboBox");
        add(tifComboBox, ++x, y);

        constraints.insets = new Insets(3, 0, 0, 0);
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        sessionComboBox.setName("SessionComboBox");
        sessionComboBox.addItem(new SessionID("DEV", "DEV", "DEV"));
        add(sessionComboBox, 0, ++y);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        submitButton.setName("SubmitButton");
        add(submitButton, x, y);
        constraints.gridwidth = 0;
        add(messageLabel, 0, ++y);

        typeComboBox.addItemListener(new PriceListener());
        typeComboBox.setSelectedItem(OrderType.MARKET);

        Font font = new Font(messageLabel.getFont().getFontName(), Font.BOLD, 12);
        messageLabel.setFont(font);
        messageLabel.setForeground(Color.red);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        submitButton.setEnabled(false);
        submitButton.addActionListener(new SubmitListener());
        activateSubmit();
    }

    private JComponent add(JComponent component, int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
        add(component, constraints);
        return component;
    }

    private void activateSubmit() {
    	OrderType type = (OrderType) typeComboBox.getSelectedItem();
    	boolean activate = symbolEntered && quantityEntered && sessionEntered;
        if (type == OrderType.MARKET)
        	submitButton.setEnabled(activate);
        else if (type == OrderType.LIMIT)
            submitButton.setEnabled(activate && limitEntered);
    }

    private class PriceListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            OrderType item = (OrderType) typeComboBox.getSelectedItem();
            if (item == OrderType.MARKET) {
                enableLimitPrice(false);
                enableTIF(true);
            } else if (item == OrderType.LIMIT) {
                enableLimitPrice(true);
                enableTIF(true);
            } else {
                enableLimitPrice(true);
                enableTIF(true);
            } 
            activateSubmit();
        }

        private void enableLimitPrice(boolean enabled) {
            Color labelColor = enabled ? Color.black : Color.gray;
            Color bgColor = enabled ? Color.white : Color.gray;
            limitPriceTextField.setEnabled(enabled);
            limitPriceTextField.setBackground(bgColor);
            limitPriceLabel.setForeground(labelColor);
        }

        private void enableTIF(boolean enabled) {
            Color labelColor = enabled ? Color.black : Color.gray;
            Color bgColor = enabled ? Color.white : Color.gray;
            // Always set to DAY as default
            tifComboBox.setSelectedIndex(0);
            tifComboBox.setEnabled(enabled);
            tifComboBox.setBackground(bgColor);
            tifLabel.setForeground(labelColor);
        }
    }

    public void update(Observable o, Object arg) {
        LogonEvent logonEvent = (LogonEvent) arg;
        if (logonEvent.isLoggedOn()) {
        	sessionComboBox.addItem(logonEvent.getSessionID());
        } else {
            sessionComboBox.removeItem(logonEvent.getSessionID());
        }
    }

    private class SubmitListener implements ActionListener {

    	public void actionPerformed(ActionEvent e) {

    		Order order = new Order();
            order.setOrderSide((OrderSide) sideComboBox.getSelectedItem());
            order.setOrderType((OrderType) typeComboBox.getSelectedItem());
            order.setOrderTIF((OrderTIF) tifComboBox.getSelectedItem());

            order.setSymbol(symbolTextField.getText());

            order.setQuantity(Integer.parseInt(quantityTextField.getText()));

            OrderType type = order.getOrderType();
            if (type == OrderType.LIMIT)
                order.setLimitPrice(Double.parseDouble(limitPriceTextField.getText()));

            order.setSessionID((SessionID) sessionComboBox.getSelectedItem());

            application.send(order);
        }
    }

    private class SubmitActivator implements KeyListener, ItemListener {
        public void keyReleased(KeyEvent e) {
            Object obj = e.getSource();
            if (obj == symbolTextField) {
                symbolEntered = testField(obj);
            } else if (obj == quantityTextField) {
                quantityEntered = testField(obj);
            } else if (obj == limitPriceTextField) {
                limitEntered = testField(obj);
            }
            activateSubmit();
        }

        public void itemStateChanged(ItemEvent e) {
        	sessionEntered = sessionComboBox.getSelectedItem() != null;
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
}