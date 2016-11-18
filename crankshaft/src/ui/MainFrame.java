package ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.OrdersContainer;
import network.ZMQTradeServer;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.ClOrdID;
import quickfix.field.OrdType;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import fix.FixApplication;
import fix.TradeSender;

public class MainFrame extends JFrame implements IListenForUIChanges {
	
	private FixApplication app = FixApplication.getFixApplication();
	
	private JTable liveOrdersTable, deadOrdersTable;
	
	private OrdersTableModel ordersTableModel = new OrdersTableModel();
	private DeadOrdersTableModel deadOrdersTableModel = new DeadOrdersTableModel();
	
	private JTextField textField_Symbol;
	private JTextField textField_Side;
	private JTextField textField_Size;
	private JTextField textField_Type;
	private JTextField textField_Limit;
	private JLabel lblStatusInformation = new JLabel("STATUS INFORMATION");
	private JLabel lblOrdid = new JLabel("OrdID");

	private TradeSender tradeSender = null;
	
	
	public MainFrame() {
		init();
		
		tradeSender = TradeSender.getTradeCreator();
		tradeSender.addUIListener(this);
		
		app.connectToServer(this);
	}

	private void shutDown() {
		disconnect();
		System.exit(0);
	}
	
	private void disconnect() {
		// disconnect FIX, clean up zmq
		FixApplication.getFixApplication().disconnectFromServer();
		ZMQTradeServer.getZMQTradeServer().disconnect();
	}
	
	
	private void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setBounds(100, 100, 800, 400);
		setTitle("TradeServer");
		
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

		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.NORTH);
		
		panel_1.add(lblStatusInformation);

		liveOrdersTable = new JTable(ordersTableModel);
		liveOrdersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting())
					return;
				displaySelectedOrder();
			}
		});
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
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblSymbol = new JLabel("Symbol:");
		panel.add(lblSymbol, "2, 2, right, default");
		
		textField_Symbol = new JTextField();
		panel.add(textField_Symbol, "4, 2, fill, default");
		textField_Symbol.setColumns(5);
		
		JLabel lblSide = new JLabel("Side: 1=B,2=S,5=SS");
		panel.add(lblSide, "2, 4, right, default");
		
		textField_Side = new JTextField();
		panel.add(textField_Side, "4, 4, fill, default");
		textField_Side.setColumns(5);
		
		JLabel lblSize = new JLabel("Size:");
		panel.add(lblSize, "2, 6, right, default");
		
		textField_Size = new JTextField();
		panel.add(textField_Size, "4, 6, fill, default");
		textField_Size.setColumns(5);
		
		JLabel lblType = new JLabel("Type: 1=Mkt,2=Lmt");
		panel.add(lblType, "2, 8, right, default");
		
		textField_Type = new JTextField();
		panel.add(textField_Type, "4, 8, fill, default");
		textField_Type.setColumns(5);
		
		JLabel lblLimit = new JLabel("Limit:");
		panel.add(lblLimit, "2, 10, right, default");
		
		textField_Limit = new JTextField();
		panel.add(textField_Limit, "4, 10, fill, default");
		textField_Limit.setColumns(5);
		
		panel.add(lblOrdid, "2, 12, center, default");
		
		JButton btnSend = new JButton("SEND");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendOrder();
//				ordersTableModel.fireTableDataChanged();
			}
		});
		panel.add(btnSend, "4, 12");
		
		JButton btnCancelAll = new JButton("Cancel ALL");
		panel.add(btnCancelAll, "2, 14");
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancelOrder();
			}
		});
		panel.add(btnCancel, "4, 14");
		
		JButton btnMarketAll = new JButton("Market ALL");
		panel.add(btnMarketAll, "2, 16");
		
		JButton btnReplace = new JButton("Replace");
		btnReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				replaceOrder();
			}
		});
		panel.add(btnReplace, "4, 16");
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Control", null, panel_2, null);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 145, 0};
		gbl_panel_2.rowHeights = new int[]{23, 0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JCheckBox chckbxSendTrades = new JCheckBox("Send Trades", true);
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
	

	private void sendOrder() {
		String ticker = textField_Symbol.getText().trim().toUpperCase();
//		public void createAndSendOrder (String ticker, char sideChar, int size, char ordType, double limit, String dest, String suffix, int traderNum, boolean useIceberg) {

		String sideStr = textField_Side.getText();
		if (sideStr.length() != 1) {
			System.err.println("\nNot entered, side is blank or too long.");
			return;
		}

		String typeStr = textField_Type.getText();
		if (typeStr.length() != 1) {
			System.err.println("\nNot entered, type is blank or too long.");
			return;
		}

		String destStr = null;
		// TODO set dest
//		if (textField_Symbol.getText().length() > 0)
//			destStr = textField_Symbol.getText().trim();

		double size = Double.parseDouble(textField_Size.getText());

		double limit = 0;
		if (typeStr.equals("2"))
			limit = Double.parseDouble(textField_Limit.getText());
		
		tradeSender.createAndSendOrder(ticker, sideStr.charAt(0), size, typeStr.charAt(0), limit, destStr);
	}
	
	private void replaceOrder() {
		if (displayedOrderID == null)
			return;
		double size = Double.parseDouble(textField_Size.getText());		
		String typeStr = textField_Type.getText();
		if (typeStr.length() != 1) {
			System.err.println("\nNot entered, type is blank or too long.");
			return;
		}
		double limit = 0;
		if (typeStr.equals("2"))
			limit = Double.parseDouble(textField_Limit.getText());

		// can only change size / limit
		tradeSender.replaceOrder(displayedOrderID, size, limit);
	}
	
	private void cancelOrder() {
		if (displayedOrderID == null)
			return;
		tradeSender.cancelOrder(displayedOrderID);		
	}
	
	String displayedOrderID = null;
	
	private void displaySelectedOrder() {
		int row = liveOrdersTable.getSelectedRow();
		if (row < 0) {
//			lblOrdid.setText("");
//			displayedOrderID = null;
			return;
		}
//		
//		ArrayList<Message> msgs = OrdersContainer.ordIDtoMessagesOpen.get(row);
//		if (msgs == null || msgs.size() == 0)
//			return;
//		
//		Message msg = msgs.get(msgs.size()-1);

		// gets msg selected		
		Message msg = (Message) ordersTableModel.getValueAt(row, -1);
		
		
		try {
			displayedOrderID = msg.getString(ClOrdID.FIELD); 
			lblOrdid.setText(displayedOrderID);
			
			textField_Type.setText(msg.getString(OrdType.FIELD));
		} catch (FieldNotFound e) {
			e.printStackTrace();
		}
		
		//{"Ticker","Type","Shares","Limit","Filled","AvgPrice","oTime","lTime"};

		textField_Symbol.setText(liveOrdersTable.getValueAt(row, 0).toString());
		String side = liveOrdersTable.getValueAt(row, 1).toString();
		if (side.equalsIgnoreCase("B"))
			textField_Side.setText("1");
		else if (side.equalsIgnoreCase("S"))
			textField_Side.setText("2");
		else textField_Side.setText("5");
		
		Object limit = liveOrdersTable.getValueAt(row, 3);
		if (limit != null)
			textField_Limit.setText(limit.toString());
		else textField_Limit.setText("");
		
		textField_Size.setText(liveOrdersTable.getValueAt(row, 2).toString());
	}

	@Override
	public void notifyOpenOrdersChanged() {
//		int sel = liveOrdersTable.getSelectedRow();
		ordersTableModel.fireTableDataChanged();
//		ordersTableModel.fireTableRowsUpdated(0, 0);
//		liveOrdersTable.getSelectionModel().setSelectionInterval(sel, sel);
	}

	@Override
	public void notifyClosedOrdersChanged() {
		deadOrdersTableModel.fireTableDataChanged();
	}

	@Override
	public void updateStats() {
		// TODO 
		
	}

	@Override
	public void updateConnection(boolean connected) {
		// TODO 
		if (connected)
			lblStatusInformation.setText("Connected");
		else lblStatusInformation.setText("NOT Connected");
	}
}
