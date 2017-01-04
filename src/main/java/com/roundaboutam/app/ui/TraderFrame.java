package com.roundaboutam.app.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.roundaboutam.app.ExecutionTableModel;
import com.roundaboutam.app.OrderTableModel;
import com.roundaboutam.app.TraderApplication;
import com.roundaboutam.app.TraderEngine;

public class TraderFrame extends JFrame {

	public TraderFrame(OrderTableModel orderTableModel, ExecutionTableModel executionTableModel,
            final TraderApplication application) {
		super();
		setTitle("Trader Engine");
		setSize(900, 700);
	
		createMenuBar(application);
		getContentPane().add(new TraderPanel(orderTableModel, executionTableModel, application),
	                BorderLayout.CENTER);
		setVisible(true);
	}
	
	private void createMenuBar(final TraderApplication application) {
        JMenuBar menubar = new JMenuBar();

        JMenu sessionMenu = new JMenu("Session");
        menubar.add(sessionMenu);

        JMenuItem logonItem = new JMenuItem("Logon");
        logonItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TraderEngine.get().logon();
			}
		});
        sessionMenu.add(logonItem);

        JMenuItem logoffItem = new JMenuItem("Logoff");
        logoffItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TraderEngine.get().logout();
			}
		});
        sessionMenu.add(logoffItem);

        JMenu appMenu = new JMenu("Application");
        menubar.add(appMenu);

        JMenuItem appAvailableItem = new JCheckBoxMenuItem("Available");
        appAvailableItem.setSelected(application.isAvailable());
        appAvailableItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				application.setAvailable(((JCheckBoxMenuItem) e.getSource()).isSelected());
			}
		});
        appMenu.add(appAvailableItem);

        JMenuItem sendMissingFieldRejectItem = new JCheckBoxMenuItem("Send Missing Field Reject");
        sendMissingFieldRejectItem.setSelected(application.isMissingField());
        sendMissingFieldRejectItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				application.setMissingField(((JCheckBoxMenuItem) e.getSource()).isSelected());
			}
		});
        appMenu.add(sendMissingFieldRejectItem);

        setJMenuBar(menubar);
    }

	
}
