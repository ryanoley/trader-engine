package ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class MainFrame extends JFrame {
	
	private JTable liveOrdersTable, deadOrdersTable;
	private OrdersTableModel ordersTableModel = null;
	private DeadOrdersTableModel deadOrdersTableModel = null;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	
	public MainFrame() {
		
		init();
	}

	private void shutDown() {
		disconnect();
		System.exit(0);
	}
	
	private void disconnect() {
		// TODO disconnect FIX, clean up zmq
	}
	
	
	private void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setBounds(100, 100, 800, 400);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				int choice = JOptionPane.showOptionDialog(null,"Are you sure?", "EXIT?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (choice != JOptionPane.YES_OPTION)
					return;

				shutDown();
			}
		});

		ordersTableModel = new OrdersTableModel();
		deadOrdersTableModel = new DeadOrdersTableModel();
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.NORTH);
		
		JLabel lblStatusInformation = new JLabel("STATUS INFORMATION");
		panel_1.add(lblStatusInformation);

		liveOrdersTable = new JTable(ordersTableModel);
		JScrollPane scrollPane = new JScrollPane(liveOrdersTable);
		
		deadOrdersTable = new JTable(deadOrdersTableModel);
		JScrollPane scrollPane2 = new JScrollPane(deadOrdersTable);
		
		
		
		JPanel panel = new JPanel();
//		panel.setPreferredSize(new Dimension(150,0));
		
//		scrollPane2.setSize(new Dimension(0,100));
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add("Order", panel);
		
//		getContentPane().add(tabbedPane, BorderLayout.WEST);
		JSplitPane ordersSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, scrollPane2);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, ordersSplitPane);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		ordersSplitPane.setDividerLocation(200);
//		splitPane.setDividerLocation(150);
		
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(40dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(75dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(5dlu;default)"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblSymbol = new JLabel("Symbol:");
		panel.add(lblSymbol, "2, 2, right, default");
		
		textField = new JTextField();
		panel.add(textField, "4, 2, fill, default");
		textField.setColumns(5);
		
		JLabel lblSuffix = new JLabel("Suffix:");
		panel.add(lblSuffix, "2, 4, right, default");
		
		textField_5 = new JTextField();
		panel.add(textField_5, "4, 4, fill, default");
		textField_5.setColumns(5);
		
		JLabel lblSide = new JLabel("Side:");
		panel.add(lblSide, "2, 6, right, default");
		
		textField_1 = new JTextField();
		panel.add(textField_1, "4, 6, fill, default");
		textField_1.setColumns(5);
		
		JLabel lblSize = new JLabel("Size:");
		panel.add(lblSize, "2, 8, right, default");
		
		textField_2 = new JTextField();
		panel.add(textField_2, "4, 8, fill, default");
		textField_2.setColumns(5);
		
		JLabel lblType = new JLabel("Type:");
		panel.add(lblType, "2, 10, right, default");
		
		textField_3 = new JTextField();
		panel.add(textField_3, "4, 10, fill, default");
		textField_3.setColumns(5);
		
		JLabel lblLimit = new JLabel("Limit:");
		panel.add(lblLimit, "2, 12, right, default");
		
		textField_4 = new JTextField();
		panel.add(textField_4, "4, 12, fill, default");
		textField_4.setColumns(5);
		
		JLabel lblOrdid = new JLabel("OrdID");
		panel.add(lblOrdid, "2, 14, center, default");
		
		JButton btnSend = new JButton("SEND");
		panel.add(btnSend, "4, 14");
		
		JButton btnCancelAll = new JButton("Cancel ALL");
		panel.add(btnCancelAll, "2, 16");
		
		JButton btnCancel = new JButton("Cancel");
		panel.add(btnCancel, "4, 16");
		
		JButton btnMarketAll = new JButton("Market ALL");
		panel.add(btnMarketAll, "2, 18");
		
		JButton btnReplace = new JButton("Replace");
		panel.add(btnReplace, "4, 18");
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Control", null, panel_2, null);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 145, 0};
		gbl_panel_2.rowHeights = new int[]{23, 0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JCheckBox chckbxSendTrades = new JCheckBox("Send Trades");
		GridBagConstraints gbc_chckbxSendTrades = new GridBagConstraints();
		gbc_chckbxSendTrades.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxSendTrades.anchor = GridBagConstraints.NORTH;
		gbc_chckbxSendTrades.gridx = 1;
		gbc_chckbxSendTrades.gridy = 2;
		panel_2.add(chckbxSendTrades, gbc_chckbxSendTrades);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int choice = JOptionPane.showOptionDialog(null,"Are you sure?", "DISCONNECT?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (choice != JOptionPane.YES_OPTION)
					return;

				disconnect();
			}
		});
		GridBagConstraints gbc_btnDisconnect = new GridBagConstraints();
		gbc_btnDisconnect.anchor = GridBagConstraints.NORTH;
		gbc_btnDisconnect.gridx = 1;
		gbc_btnDisconnect.gridy = 3;
		panel_2.add(btnDisconnect, gbc_btnDisconnect);
		
	}
	

}
