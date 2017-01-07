package com.roundaboutam.trader.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.TraderEngine;

@SuppressWarnings("serial")
public class TraderFrame extends JFrame {

	public TraderFrame(final TraderApplication application) {
		super();
		setTitle("Trader Engine");
		setSize(900, 700);
		createMenuBar();
		addWindowClosingHandler();
		getContentPane().add(new TraderPanel(application), BorderLayout.CENTER);
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

	private void createMenuBar() {

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

        JMenuItem stopInitiatorItem = new JMenuItem("Stop Initiator");
        stopInitiatorItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TraderEngine.get().stopInitiator();
			}
		});
        sessionMenu.add(stopInitiatorItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callExitDialogueBoxAndShutdown();
			}
		});
        sessionMenu.add(exitItem);

        setJMenuBar(menubar);
    }

}
