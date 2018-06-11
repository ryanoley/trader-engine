package com.roundaboutam.trader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.order.Order;


@SuppressWarnings("serial")
public class OrderTablePanel extends JPanel {

	public OrderTablePanel(TraderApplication application) {
		setLayout(new BorderLayout());
		add(new JScrollPane(new OrderTable(application)));
    }
	
}


@SuppressWarnings("serial")
class OrderTableModel extends AbstractTableModel implements Observer {

    private final static int SYMBOL = 0;
    private final static int SIDE = 1;
    private final static int QUANTITY = 2;
    private final static int PARENTBASKET = 3;
    private final static int OPEN = 4;
    private final static int EXECUTED = 5;
    private final static int TYPE = 6;
    private final static int LIMITPRICE = 7;
    private final static int AVGPX = 8;
    private final static int MESSAGE = 9;

    private final HashMap<Integer, Order> rowToOrder;
    private final HashMap<String, Integer> idToRow;
    private final HashMap<String, Order> idToOrder;

    private final String[] headers = new String[] {"Symbol", "Side", "Quantity", 
    		"Basket", "Open", "Executed", "Type", "Limit", "AvgPx", "Message"};

    public OrderTableModel(TraderApplication application) {
    	application.addOrderObserver(this);
    	rowToOrder = new HashMap<Integer, Order>();
        idToRow = new HashMap<String, Integer>();
        idToOrder = new HashMap<String, Order>();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    private void addOrder(Order order) {
    	int row = rowToOrder.size();
        rowToOrder.put(row, order);
        idToRow.put(order.getPermanentID(), row);
        idToOrder.put(order.getPermanentID(), order);
        fireTableRowsInserted(row, row);
    }

    public Order getOrder(String id) {
        return idToOrder.get(id);
    }

    public Order getOrder(int row) {
        return rowToOrder.get(row);
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) { }

    public Class<String> getColumnClass(int columnIndex) {
        return String.class;
    }

    public int getRowCount() {
        return rowToOrder.size();
    }

    public int getColumnCount() {
        return headers.length;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return headers[columnIndex];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
    	Order order = rowToOrder.get(rowIndex);
    	switch (columnIndex) {
        case SYMBOL:
        	String display_symbol = replaceNull(order.getSymbol());
        	String display_suffix = replaceNull(order.getSuffix());
        	return display_suffix.equals("-") ?  display_symbol : display_symbol + "." + display_suffix;
        case SIDE:
        	return replaceNull(order.getOrderSide().toString());
        case QUANTITY:
        	return replaceNull(String.valueOf(order.getQuantity()));
        case PARENTBASKET:
        	return replaceNull(order.getOrderBasketName());
        case OPEN:
        	return replaceNull(String.valueOf(order.getLeavesQty()));
        case EXECUTED:
        	return replaceNull(String.valueOf(order.getCumQty()));
        case TYPE:
        	return replaceNull(order.getPriceType().toString());
        case LIMITPRICE:
        	return replaceNull(String.valueOf(order.getLimitPrice()));
        case AVGPX:
        	return replaceNull(String.valueOf(order.getAvgPx()));
        case MESSAGE:
        	return replaceNull(order.getMessage());
        }
    	return "-";
    }

    private static String replaceNull(String input) {
    	if (input == null)
    		return "-";
    	else if (input.isEmpty())
    		return "-";
    	else
    		return input.equals("null") ? "-" : input;
    	}
    
	public void update(Observable o, Object arg) {
    	Order order = (Order) arg;
    	if (!idToOrder.containsKey(order.getPermanentID())) {
			addOrder(order);
    		return;
    	}
    	int row = idToRow.get(order.getPermanentID());
    	fireTableRowsUpdated(row, row);
	}

}


@SuppressWarnings("serial")
class OrderTable extends JTable implements MouseListener {
	
	private transient TraderApplication application;

    public OrderTable(TraderApplication application) {
        super(new OrderTableModel(application));
        this.application = application;
        initColumnWidths();
        addMouseListener(this);
        this.setAutoCreateRowSorter(true);
    }

	private void initColumnWidths() {
		TableColumnModel model = this.getColumnModel();
        TableColumn column = model.getColumn(0);
        column.setPreferredWidth((int) (50));
        column = model.getColumn(1);
        column.setPreferredWidth((int) (50));
        column = model.getColumn(2);
        column.setPreferredWidth((int) (50));
        column = model.getColumn(3);
        column.setPreferredWidth((int) (70));
        column = model.getColumn(4);
        column.setPreferredWidth((int) (50));
        column = model.getColumn(5);
        column.setPreferredWidth((int) (50));
        column = model.getColumn(6);
        column.setPreferredWidth((int) (50));
        column = model.getColumn(7);
        column.setPreferredWidth((int) (50));
        column = model.getColumn(8);
        column.setPreferredWidth((int) (50));
        column = model.getColumn(9);
        column.setPreferredWidth((int) (70));
	}

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

        int modelIdx = convertRowIndexToModel(row);
        
    	Order order = ((OrderTableModel) dataModel).getOrder(modelIdx);
        int open = order.getLeavesQty();
        boolean rejected = order.isRejected();
        boolean canceled = order.isCanceled();
        boolean acknowledged = order.isAcknowledged();

        Component c = super.prepareRenderer(renderer, row, column); 
        c.setForeground(Color.black);
        if (rejected)
            c.setBackground(Color.red);
        else if (canceled)
            c.setBackground(Color.lightGray);
        else if (!acknowledged)
            c.setBackground(Color.yellow);
        else if (open > 0)
            c.setBackground(Color.white);
        else if (open == 0)
            c.setBackground(Color.lightGray);

        JComponent jc = (JComponent) c;
        if (isRowSelected(row)){
          int left = column == 0 ? 1:0;
          int right = column == getColumnCount() - 1 ? 1:0;
          jc.setBorder(new MatteBorder(1, left, 1, right, Color.blue)); 
        }
        else
          jc.setBorder(null);

        return c;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() != 2)
            return;
        int row = rowAtPoint(e.getPoint());
        int modelIdx = convertRowIndexToModel(row);
        Order order = ((OrderTableModel) dataModel).getOrder(modelIdx);
        OrderModificationFrame.getInstance(application, order);
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}
}
