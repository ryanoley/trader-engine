package com.roundaboutam.trader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.roundaboutam.trader.order.OrderBasket;



@SuppressWarnings("serial")
public class BasketTablePanel extends JPanel {

	public BasketTablePanel(TraderApplication application) {
		setLayout(new BorderLayout());
		BasketTable basketTable = new BasketTable(application);
		add(new JScrollPane(basketTable));
    }

}


@SuppressWarnings("serial")
class BasketTableModel extends AbstractTableModel implements Observer  {

    private final static int ID = 0;
    private final static int NAME = 1;
    private final static int TOTALORDERS = 2;
    private final static int TOTALSHARES = 3;
    private final static int OPENSHARES = 4;
    private final static int EXECSHARES = 5;
    private final static int STATUS = 6;
    private final static int TIME = 7;
    protected final HashMap<Integer, Date> rowToTimeStamp;
    protected final HashMap<Integer, OrderBasket> rowToOrderBasket;
    private final HashMap<String, Integer> idToRow;
    private final HashMap<String, OrderBasket> idToOrderBasket;

    public final String[] headers = new String[] {"BasketID", "Name", "Total Orders", 
    		"Total Shares", "Open Shares", "Exec Shares", "Status", "CreateTime"};

    public BasketTableModel(TraderApplication application) {
    	application.addOrderBasketObserver(this);
    	rowToOrderBasket = new HashMap<Integer, OrderBasket>();
    	idToRow = new HashMap<String, Integer>();
    	idToOrderBasket = new HashMap<String, OrderBasket>();
    	rowToTimeStamp = new HashMap<Integer, Date>();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    private void addBasketToTable(OrderBasket orderBasket) {
    	int row = getRowCount();
    	rowToOrderBasket.put(row, orderBasket);
    	rowToTimeStamp.put(row, new Date(System.currentTimeMillis()));
    	idToOrderBasket.put(orderBasket.getBasketId(), orderBasket);
    	idToRow.put(orderBasket.getBasketId(), row);
        fireTableRowsInserted(row, row);
    }
 
    private void removeBasketFromTable(OrderBasket orderBasket) {
    	int row = idToRow.get(orderBasket.getBasketId());
    	rowToOrderBasket.remove(row);
    	rowToTimeStamp.remove(row);
    	idToOrderBasket.remove(orderBasket.getBasketId());
    	idToRow.remove(orderBasket.getBasketId());
        fireTableRowsDeleted(row, row);
    }
  
    public Class<String> getColumnClass(int columnIndex) {
        return String.class;
    }

    public int getRowCount() {
    	return rowToOrderBasket.size();
    }

    public int getColumnCount() {
        return headers.length;
    }

    public OrderBasket getOrderBasket(int row) {
        return rowToOrderBasket.get(row);
    }
    
    public String getColumnName(int columnIndex) {
        return headers[columnIndex];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
    	OrderBasket orderBasket = getOrderBasket(rowIndex);
    	Date timeStamp = rowToTimeStamp.get(rowIndex);
    	switch (columnIndex) {
        case ID:
        	return orderBasket.getBasketId();
        case NAME:
        	return replaceNull(orderBasket.getBasketName());
        case TOTALORDERS:
        	return orderBasket.getAllOrders().size();
        case TOTALSHARES:
        	return orderBasket.getTotalShares();
        case OPENSHARES:
        	return orderBasket.getOpenShares();
        case EXECSHARES:
        	return orderBasket.getExecShares();
        case STATUS:
        	if (orderBasket.isFilled())
        		return "FILLED";
        	else if (orderBasket.isLive())
        		return "LIVE";
        	else if (orderBasket.isStaged())
        		return "STAGED";
        case TIME:
        	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.S");  
        	return sdf.format(timeStamp);
        }
        return "#NA";
    }

    private static String replaceNull(String input) {
    	if (input == null)
    		return "-";
    	else if (input.isEmpty())
    		return "-";
    	else
    		return input.equals("null") ? "-" : input;
    	}

	@Override
	public void update(Observable arg0, Object arg) {
		OrderBasket orderBasket = (OrderBasket) arg;
		orderBasket.getSummary();

    	if (!idToOrderBasket.containsKey(orderBasket.getBasketId())) {
			addBasketToTable(orderBasket);
    	} else if (orderBasket.isDeleted()) {
    		removeBasketFromTable(orderBasket);
    	} else {
        	int row = idToRow.get(orderBasket.getBasketId());
        	fireTableRowsUpdated(row, row);    		
    	}
	}
}


@SuppressWarnings("serial")
class BasketTable extends JTable implements MouseListener {

	private transient TraderApplication application;
	
    public BasketTable(TraderApplication application) {
        super(new BasketTableModel(application));
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
        column.setPreferredWidth((int) (40));
        column = model.getColumn(3);
        column.setPreferredWidth((int) (40));
        column = model.getColumn(4);
        column.setPreferredWidth((int) (40));
        column = model.getColumn(5);
        column.setPreferredWidth((int) (40));
        column = model.getColumn(6);
        column.setPreferredWidth((int) (50));
        column = model.getColumn(7);
        column.setPreferredWidth((int) (50));
	}

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

        int modelIdx = convertRowIndexToModel(row);
    	OrderBasket orderBasket = ((BasketTableModel) getModel()).getOrderBasket(modelIdx);
        Component c = super.prepareRenderer(renderer, row, column); 
        c.setForeground(Color.black);
        c.setBackground(Color.white);

		if (orderBasket.isFilled())
			c.setBackground(Color.lightGray);
		else if (orderBasket.isLive())
            c.setForeground(Color.blue);

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
    
	@Override
    public void mouseClicked(MouseEvent e) {        
    	if (e.getClickCount() == 2) {
		    int row = rowAtPoint(e.getPoint());
		    int modelIdx = convertRowIndexToModel(row);
		    OrderBasket orderBasket = ((BasketTableModel) dataModel).getOrderBasket(modelIdx);
		    orderBasket.getSummary();
		    BasketDetailFrame.getInstance(orderBasket, application);
	    }
    	
    }

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

}
