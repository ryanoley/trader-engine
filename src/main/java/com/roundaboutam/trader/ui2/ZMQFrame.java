package com.roundaboutam.trader.ui2;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ZMQFrame {

	private static JFrame frame;
	private static JPanel panel;

	private static JTextField subPortField = new JTextField("5556");
	private static JRadioButton subButtonOn = new JRadioButton("On");
	private static JRadioButton subButtonOff = new JRadioButton("Off");
	private static ButtonGroup subButtonGroup = new ButtonGroup();

	private static JTextField pubPortField = new JTextField("5557");
	private static JRadioButton pubButtonOn = new JRadioButton("On");
	private static JRadioButton pubButtonOff = new JRadioButton("Off");
	private static ButtonGroup pubButtonGroup = new ButtonGroup();

	private static ZMQFrame instance = null;

	public static ZMQFrame getInstance() {
		if (instance == null) {
			instance = new ZMQFrame();
			return instance;
		}
		if (!frame.isVisible())
			frame.setVisible(true);
		return instance;
	}

	private ZMQFrame() {
		makeZMQFrame();
	}


	private void makeZMQFrame() {

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("ZMQ");
		frame.setSize(400, 600);
		frame.setResizable(false);

		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Subscriber:"), c);
		c.gridx = 1;
		c.gridy = 0;
		panel.add(subButtonOn, c);
		c.gridx = 2;
		c.gridy = 0;
		panel.add(subButtonOff, c);

		c.gridx = 0;
		c.gridy = 1;
		panel.add(new JLabel("Publisher:"), c);
		c.gridx = 1;
		c.gridy = 1;
		panel.add(pubButtonOn, c);
		c.gridx = 2;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 8, 0);
		panel.add(pubButtonOff, c);

		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(0, 0, 0, 0);
		panel.add(new JLabel("Subscribe Port:"), c);
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 2;
		panel.add(subPortField, c);

		c.gridx = 0;
		c.gridy = 3;
		panel.add(new JLabel("Publisher Port:"), c);
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 10, 0);
		panel.add(pubPortField, c);

		subButtonGroup.add(subButtonOn);
		subButtonGroup.add(subButtonOff);
		subButtonOff.setSelected(true);
		pubButtonGroup.add(pubButtonOn);
		pubButtonGroup.add(pubButtonOff);
		pubButtonOff.setSelected(true);

	    c.gridx = 0;
	    c.gridy = 4;
	    c.gridwidth = 3;
	    c.insets = new Insets(0, 0, 5, 0);
	    JButton btnSubmit = new JButton("Okay");
	    btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//makeTransmitOrder();
			}
		});
	    panel.add(btnSubmit, c);

	    c.gridx = 0;
	    c.gridy = 5;
	    JButton btnClose = new JButton("Close");
	    btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				instance = null;
				frame.dispose();
			}
		});
	    panel.add(btnClose, c);

	    frame.add(panel);	    
	    frame.setVisible(true);
	}

}
