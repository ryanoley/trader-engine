package com.roundaboutam.trader.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.OrderBasket;


@SuppressWarnings("serial")
class BasketDetailTable extends JTable implements MouseListener {

    public BasketDetailTable(OrderBasket orderBasket) {
        super(new BasketTableModel(orderBasket));
        initColumnWidths();
        addMouseListener(this);
        this.setAutoCreateRowSorter(true);
    }
 
	private void initColumnWidths() {
		TableColumnModel model = this.getColumnModel();
        TableColumn column = model.getColumn(0);
        column.setPreferredWidth((int) (100));
        column = model.getColumn(1);
        column.setPreferredWidth((int) (100));
        column = model.getColumn(2);
        column.setPreferredWidth((int) (100));
        column = model.getColumn(3);
        column.setPreferredWidth((int) (100));
	}

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

        int modelIdx = convertRowIndexToModel(row);
    	//Order order = ((BasketTableModel) getModel()).getOrder(modelIdx);
        Component c = super.prepareRenderer(renderer, row, column);
        
        JComponent jc = (JComponent) c;
        if (isRowSelected(row)){
          c.setBackground(Color.white);
          c.setForeground(Color.black);
          int left = column == 0 ? 1:0;
          int right = column == getColumnCount() - 1 ? 1:0;
          jc.setBorder(new MatteBorder(1, left, 1, right, Color.blue)); 
        }
        else
          jc.setBorder(null);

        return c;
    }

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}
	
}


@SuppressWarnings("serial")
class BasketTableModel extends AbstractTableModel {

    private final static int ORDID = 0;
    private final static int SIDE = 1;
    private final static int SYMBOL = 2;
    private final static int TYPE = 3;
    private final static int LIMIT = 4;
    private final static int SHARES = 5;

    protected final HashMap<Integer, Order> rowToOrder;
    protected final HashMap<Integer, String> rowToOrderID;

    public final String[] headers = new String[] {"OrdID", "Side", "Sym", "Type", "Limit", "Shares"};

    public BasketTableModel(OrderBasket orderBasket) {
    	rowToOrder = new HashMap<Integer, Order>();
    	rowToOrderID = new HashMap<Integer, String>();
    	int i = 0;
    	for (Map.Entry<String, Order> entry : orderBasket.getOrderMap().entrySet()) {
    	 rowToOrder.put(i, entry.getValue());
    	 rowToOrderID.put(i, entry.getKey());
    	 i++;
    	}
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
  
    public Class<String> getColumnClass(int columnIndex) {
        return String.class;
    }

    public int getRowCount() {
    	return rowToOrder.size();
    }

    public int getColumnCount() {
        return headers.length;
    }

    public Order getOrder(int row) {
        return rowToOrder.get(row);
    }

    public String getColumnName(int columnIndex) {
        return headers[columnIndex];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
    	Order order = getOrder(rowIndex);
 
    	switch (columnIndex) {
        case SIDE:
        	return order.getOrderSide().toString();
        case SYMBOL:
        	return order.getSymbol();
        case ORDID:
        	return order.getOrderID();
        case TYPE:
        	return order.getOrderType().toString();
        case LIMIT:
        	return String.valueOf(order.getLimitPrice());
        case SHARES:
        	return String.valueOf(order.getQuantity());
        }
        return "#NA";
    }
}

