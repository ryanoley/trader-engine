package com.roundaboutam.trader.ui;

import javax.swing.*;

import com.roundaboutam.trader.DoubleNumberTextField;
import com.roundaboutam.trader.IntegerNumberTextField;
import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.order.CancelOrder;
import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.OrderType;
import com.roundaboutam.trader.order.ReplaceOrder;

import java.awt.*;
import java.awt.event.*;

public class CancelReplacePanel extends JPanel {

	private final JLabel quantityLabel = new JLabel("Quantity");
    private final JLabel limitPriceLabel = new JLabel("Limit");
    private final IntegerNumberTextField quantityTextField = new IntegerNumberTextField();
    private final DoubleNumberTextField limitPriceTextField = new DoubleNumberTextField();
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton replaceButton = new JButton("Replace");
    private Order order = null;

    private final GridBagConstraints constraints = new GridBagConstraints();

    private final TraderApplication application;

    public CancelReplacePanel(final TraderApplication application) {
        this.application = application;
        cancelButton.addActionListener(new CancelListener());
        replaceButton.addActionListener(new ReplaceListener());

        setLayout(new GridBagLayout());
        createComponents();
    }

    public void addActionListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
        replaceButton.addActionListener(listener);
    }

    private void createComponents() {
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;

        int x = 0;
        int y = 0;

        constraints.insets = new Insets(0, 0, 5, 5);
        add(cancelButton, x, y);
        add(replaceButton, ++x, y);
        constraints.weightx = 0;
        add(quantityLabel, ++x, y);
        constraints.weightx = 5;
        add(quantityTextField, ++x, y);
        constraints.weightx = 0;
        add(limitPriceLabel, ++x, y);
        constraints.weightx = 5;
        add(limitPriceTextField, ++x, y);

        // Disable fields on load
		cancelButton.setEnabled(false);
        replaceButton.setEnabled(false);
        quantityTextField.setEnabled(false);
        limitPriceTextField.setEnabled(false);
    }

    private void orderSpecificFieldEnabler() {
    	if (order.getOpen() > 0) {
    		enableCancelReplaceButtons(true);
    		enableQuantityField(true);
    		if (order.getOrderType() == OrderType.LIMIT) {
    			enableLimitPriceField(true);
    		} else {
    			enableLimitPriceField(false);
    		}
    	} else {
    		enableCancelReplaceButtons(false);
    		enableQuantityField(false);
    		enableLimitPriceField(false);
    	}
    }

    private void enableCancelReplaceButtons(boolean enabled) {
		cancelButton.setEnabled(enabled);
        replaceButton.setEnabled(enabled);
    }

    private void enableQuantityField(boolean enabled) {
        Color labelColor = enabled ? Color.black : Color.gray;
        Color bgColor = enabled ? Color.white : Color.gray;
        quantityTextField.setEnabled(enabled);
        quantityTextField.setBackground(bgColor);
        quantityLabel.setForeground(labelColor);
    }
    
    private void enableLimitPriceField(boolean enabled) {
        Color labelColor = enabled ? Color.black : Color.gray;
        Color bgColor = enabled ? Color.white : Color.gray;
        limitPriceTextField.setEnabled(enabled);
        limitPriceTextField.setBackground(bgColor);
        limitPriceLabel.setForeground(labelColor);
    }

    public void update() {
        setOrder(this.order);
    }

    public void setOrder(Order order) {
        if (order == null)
            return;
        this.order = order;
        quantityTextField.setText
        (Integer.toString(order.getOpen()));

        Double limit = order.getLimitPrice();
        if (limit != null)
            limitPriceTextField.setText(order.getLimitPrice().toString());
        orderSpecificFieldEnabler();
    }

    private JComponent add(JComponent component, int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
        add(component, constraints);
        return component;
    }

    private class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            CancelOrder cancelOrder = new CancelOrder(order);
        	application.cancel(cancelOrder);
        }
    }

    private class ReplaceListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	ReplaceOrder replaceOrder = new ReplaceOrder(order);
        	replaceOrder.setQuantity(Integer.parseInt(quantityTextField.getText()));
        	replaceOrder.setLimitPrice(Double.parseDouble(limitPriceTextField.getText()));
            application.replace(replaceOrder);
        }
    }
}
