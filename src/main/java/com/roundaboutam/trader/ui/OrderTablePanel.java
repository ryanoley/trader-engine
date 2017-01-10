package com.roundaboutam.trader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

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
    private final static int QUANTITY = 1;
    private final static int OPEN = 2;
    private final static int EXECUTED = 3;
    private final static int SIDE = 4;
    private final static int TYPE = 5;
    private final static int LIMITPRICE = 6;
    private final static int STOPPRICE = 7;
    private final static int AVGPX = 8;
    private final static int MESSAGE = 9;

    private final HashMap<Integer, Order> rowToOrder;
    private final HashMap<String, Integer> idToRow;
    private final HashMap<String, Order> idToOrder;

    private final String[] headers = new String[] {"Symbol", "Quantity", "Open", 
    		"Executed", "Side", "Type", "Limit", "Stop", "AvgPx", "Message"};

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

    public String getColumnName(int columnIndex) {
        return headers[columnIndex];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
    	Order order = rowToOrder.get(rowIndex);
    	switch (columnIndex) {
        case SYMBOL:
            return order.getSymbol();
        case QUANTITY:
            return order.getQuantity();
        case OPEN:
            return order.getLeavesQty();
        case EXECUTED:
            return order.getCumQty();
        case SIDE:
            return order.getOrderSide();
        case TYPE:
            return order.getOrderType();
        case LIMITPRICE:
            return order.getLimitPrice();
        case STOPPRICE:
            return order.getStopPrice();
        case AVGPX:
            return order.getAvgPx();
        case MESSAGE:
        	return order.getMessage();
        }
        return "";
    }

	public void update(Observable o, Object arg) {
    	Order order = (Order) arg;
    	if (!idToOrder.containsKey(order.getPermanentID())) {
			addOrder(order);
    		return;
    	}
    	int row = idToRow.get(order.getPermanentID());
    	fireTableRowsInserted(row, row);
	}

}


@SuppressWarnings("serial")
class OrderTable extends JTable implements MouseListener {

	private transient TraderApplication application;

    public OrderTable(TraderApplication application) {
        super(new OrderTableModel(application));
        this.application = application;
        addMouseListener(this);
    }

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

    	Order order = ((OrderTableModel) dataModel).getOrder(row);

        int open = order.getLeavesQty();
        boolean rejected = order.isRejected();
        boolean canceled = order.isCanceled();
        boolean acknowledged = order.isAcknowledged();

        DefaultTableCellRenderer r = (DefaultTableCellRenderer) renderer;
        r.setForeground(Color.black);

        if (rejected)
            r.setBackground(Color.red);
        else if (canceled)
            r.setBackground(Color.white);
        else if (!acknowledged)
            r.setBackground(Color.yellow);
        else if (open > 0)
            r.setBackground(Color.green);
        else if (open == 0)
            r.setBackground(Color.white);

        return super.prepareRenderer(renderer, row, column);
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() != 2)
            return;
        int row = rowAtPoint(e.getPoint());
        Order order = ((OrderTableModel) dataModel).getOrder(row);
        OrderModificationFrame.getInstance(application, order);
        System.out.println("Row: " + row);
        // This should have pop-up menu that has populated ReplaceOrder ticket
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}
}
