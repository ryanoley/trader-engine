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
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.TraderEngine;
import com.roundaboutam.trader.order.OrderBasket;


public class BasketDetailFrame {

	private transient TraderApplication application = null;
	private static JFrame frame;
	private static JPanel panel;
	private static BasketDetailFrame instance = null;
	private OrderBasket orderBasket;
	private JButton submitBasketButton;
	private JButton cancelAllButton;
	private JButton deleteButton;

	public static BasketDetailFrame getInstance(OrderBasket orderBasket, TraderApplication application) {
		if (instance == null) {
			instance = new BasketDetailFrame(orderBasket, application);
		}
		if (!frame.isVisible())
			frame.setVisible(true);
		return instance;
	}

	private BasketDetailFrame(OrderBasket orderBasket, TraderApplication application) {
		this.orderBasket = orderBasket;
		this.application = application;
		makeBasketDetailFrame();
	}

	private void makeBasketDetailFrame() {
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
		c.gridwidth = 4;
		c.weighty = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		BasketDetailTable basketTable = new BasketDetailTable(orderBasket);
		panel.add(new JScrollPane(basketTable), c);

	    c.gridy = 1;
		c.weighty = .25;
	    JTable summaryTable = basketSummaryPanel(orderBasket);
	    panel.add(new JScrollPane(summaryTable), c);
	    
		activateButtons();
		
	    c = new GridBagConstraints();
	    c.gridy = 2;
		c.weighty = .1;
		c.weightx = 1;  
	    panel.add(submitBasketButton, c);

	    c.gridx = 1;
	    panel.add(cancelAllButton, c);

	    c.gridx = 2;
	    panel.add(deleteButton, c);

	    c.gridx = 3;
	    JButton closeButton = getCloseButton();
	    panel.add(closeButton, c);
	    
	    frame.add(panel);
	    frame.setVisible(true);
	}
	
	private void activateButtons() {
		submitBasketButton = getSubmitBasketButton();
	    cancelAllButton = getCancelAllButton();
	    deleteButton = getDeleteButton();
		
		if (orderBasket.isStaged()) {
			deleteButton.setEnabled(true);
			submitBasketButton.setEnabled(true);
			cancelAllButton.setEnabled(false);
		}
		else if (orderBasket.isLive()) {
			submitBasketButton.setEnabled(false);	
			deleteButton.setEnabled(false);
			cancelAllButton.setEnabled(true);
		}
		else {
			deleteButton.setEnabled(false);
			submitBasketButton.setEnabled(false);
			cancelAllButton.setEnabled(false);
		}
	}

	private JTable basketSummaryPanel(OrderBasket orderBasket) {
		String[] columns = new String[] {"Basket Summary", "BY", "SS", "BTC", "SL", "Total"};

		Object[][] data = new Object[][] {
			{"Orders", orderBasket.nOrdersBY, orderBasket.nOrdersSS, 
						orderBasket.nOrdersBTC, orderBasket.nOrdersSL, 
						orderBasket.getOrderCount()},
	
			{"Traded Dollars (Net)", orderBasket.execDollarsBY, -orderBasket.execDollarsSS, 
									orderBasket.execDollarsBTC, -orderBasket.execDollarsSL, 
									orderBasket.getExecDollarsNet()},

			{"Open Shares", orderBasket.getOpenSharesBY(), orderBasket.getOpenSharesSS(), 
									orderBasket.getOpenSharesBTC(), orderBasket.getOpenSharesSL(), 
									orderBasket.getOpenShares()}		
			};

		JTable table = new JTable(data, columns);
		JTableHeader header = table.getTableHeader();
		header.setOpaque(false);
		header.setBackground(Color.lightGray);
		header.setBorder(new MatteBorder(0, 0, 1, 0, Color.black));
		
		TableColumnModel model = table.getColumnModel();
        TableColumn column = model.getColumn(0);
        column.setPreferredWidth((int) (150));
        column = model.getColumn(1);
        column.setPreferredWidth((int) (100));
        column = model.getColumn(2);
        column.setPreferredWidth((int) (100));
        column = model.getColumn(3);
        column.setPreferredWidth((int) (100));
        column = model.getColumn(4);
        column.setPreferredWidth((int) (100));

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
	
	private JButton getSubmitBasketButton() {
		submitBasketButton = new JButton("Submit");
		submitBasketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				confirmAndSubmit();
			}
		});
	    return submitBasketButton;
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

	private JButton getDeleteButton() {
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteBasket();
			}
		});
	    return deleteButton;
	}
	
	private void confirmAndSubmit() {
		int choice = JOptionPane.showOptionDialog(frame, "Submit basket to exchange?", "Confirm?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (choice == JOptionPane.YES_OPTION) {
            application.sendBasket(orderBasket);
			instance = null;
			frame.dispose();
		}
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

	private void deleteBasket() {
		int choice = JOptionPane.showOptionDialog(frame, "Delete this Basket", "Confirm?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (!orderBasket.isStaged()){
			throw new IllegalStateException(orderBasket.getBasketName() + " is not Staged, cannot delete.");
		}
		else if (choice == JOptionPane.YES_OPTION) {
			application.deleteBasket(orderBasket);
		}

		instance = null;
		frame.dispose();
	}
	
}


