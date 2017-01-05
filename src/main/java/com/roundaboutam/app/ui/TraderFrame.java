package com.roundaboutam.app.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.roundaboutam.app.ExecutionTableModel;
import com.roundaboutam.app.OrderTableModel;
import com.roundaboutam.app.TraderApplication;
import com.roundaboutam.app.TraderEngine;

@SuppressWarnings("serial")
public class TraderFrame extends JFrame {

	public TraderFrame(OrderTableModel orderTableModel, ExecutionTableModel executionTableModel,
            final TraderApplication application) {

		super();
		setTitle("Trader Engine");
		setSize(900, 700);

		createMenuBar(application);
		getContentPane().add(new TraderPanel(orderTableModel, executionTableModel, application),
	                BorderLayout.CENTER);
		
		addWindowClosingHandler();
		
		setVisible(true);
	}

	private void addWindowClosingHandler() {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				callExitDialogueBoxAndShutdown();
			}
		});
	}

	private void callExitDialogueBoxAndShutdown() {
		int choice = JOptionPane.showOptionDialog(null,"Are you sure?", "EXIT?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (choice != JOptionPane.YES_OPTION)
			return;
		TraderEngine.get().shutdown();
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

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callExitDialogueBoxAndShutdown();
			}
		});
        sessionMenu.add(exitItem);

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

        setJMenuBar(menubar);
    }

}
