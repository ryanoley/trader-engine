package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class MainFrame extends JFrame {
	
	private JTable table;
	private OrdersTableModel ordersTableModel = null;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	
	public MainFrame() {
		ordersTableModel = new OrdersTableModel();
		
		init();
	}
	
	
	private void init() {
		
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(200,0));
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add("Order", panel);
		
		getContentPane().add(tabbedPane, BorderLayout.WEST);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(40dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(75dlu;default):grow"),},
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
		textField_5.setColumns(10);
		
		JLabel lblSide = new JLabel("Side:");
		panel.add(lblSide, "2, 6, right, default");
		
		textField_1 = new JTextField();
		panel.add(textField_1, "4, 6, fill, default");
		textField_1.setColumns(5);
		
		JLabel lblSize = new JLabel("Size:");
		panel.add(lblSize, "2, 8, right, default");
		
		textField_2 = new JTextField();
		panel.add(textField_2, "4, 8, fill, default");
		textField_2.setColumns(10);
		
		JLabel lblType = new JLabel("Type:");
		panel.add(lblType, "2, 10, right, default");
		
		textField_3 = new JTextField();
		panel.add(textField_3, "4, 10, fill, default");
		textField_3.setColumns(10);
		
		JLabel lblLimit = new JLabel("Limit:");
		panel.add(lblLimit, "2, 12, right, default");
		
		textField_4 = new JTextField();
		panel.add(textField_4, "4, 12, fill, default");
		textField_4.setColumns(10);
		
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
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.NORTH);

		table = new JTable(ordersTableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
	}
	

}
