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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.MessageContainer;

import quickfix.Message;




@SuppressWarnings("serial")
public class MessageTablePanel extends JPanel {

	public MessageTablePanel(TraderApplication application) {
		setLayout(new BorderLayout());
		MessageTable messageTable = new MessageTable(application);
		add(new JScrollPane(messageTable));
    }

}


@SuppressWarnings("serial")
class MessageTableModel extends AbstractTableModel implements Observer {

    private final static int TIME = 0;
    private final static int MSGTYPE = 1;
    private final static int ORDID = 2;
    private final static int SYMBOL = 3;
    private final static int STATUS = 4;
    private final static int ORDQTY = 5;
    private final static int TEXT = 6;
    private final static int MESSAGE = 7;
    protected final HashMap<Integer, MessageContainer> rowToMessage;
    protected final HashMap<Integer, Date> rowToTimeStamp;

    public final String[] headers = new String[] {"Time", "Type","OrdID", 
    		"Sym", "Status", "OrdQty", "Text",  "Message"};

    public MessageTableModel(TraderApplication application) {
    	application.addMessageObserver(this);
    	rowToMessage = new HashMap<Integer, MessageContainer>();
    	rowToTimeStamp = new HashMap<Integer, Date>();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    private void addMessage(Message message) {
    	int row = rowToMessage.size();
    	MessageContainer messageContainer = new MessageContainer(message);
    	rowToMessage.put(row, messageContainer);
    	rowToTimeStamp.put(row, new Date(System.currentTimeMillis()));
        fireTableRowsInserted(row, row);
    }
  
    public Class<String> getColumnClass(int columnIndex) {
        return String.class;
    }

    public int getRowCount() {
    	return rowToMessage.size();
    }

    public int getColumnCount() {
        return headers.length;
    }

    public MessageContainer getMessage(int row) {
        return rowToMessage.get(row);
    }
    
    public String getColumnName(int columnIndex) {
        return headers[columnIndex];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
    	MessageContainer messageContainer = getMessage(rowIndex);
    	Date timeStamp = rowToTimeStamp.get(rowIndex);
 
    	switch (columnIndex) {
        case TIME:
        	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.S");  
        	return sdf.format(timeStamp);
        case MSGTYPE:
        	return messageContainer.getMsgType();
        case SYMBOL:
        	return messageContainer.getSymbol();
        case ORDID:
        	return messageContainer.getDisplayID();
        case ORDQTY:
        	return messageContainer.getMsgQty();
        case STATUS:
        	return messageContainer.getStatus();
        case TEXT:
        	return messageContainer.getText();
        case MESSAGE:
            return messageContainer.getMessage().toString();
        }
        return "#NA";
    }

	public void update(Observable o, Object arg) {
		Message message = (Message) arg;
        addMessage(message);
	}

}




@SuppressWarnings("serial")
class MessageTable extends JTable implements MouseListener {

    public MessageTable(TraderApplication application) {
        super(new MessageTableModel(application));
        initColumnWidths();
        addMouseListener(this);
        this.setAutoCreateRowSorter(true);
    }
 
	private void initColumnWidths() {
		TableColumnModel model = this.getColumnModel();
        TableColumn column = model.getColumn(0);
        column.setPreferredWidth((int) (80));
        column = model.getColumn(1);
        column.setPreferredWidth((int) (60));
        column = model.getColumn(2);
        column.setPreferredWidth((int) (80));
        column = model.getColumn(3);
        column.setPreferredWidth((int) (40));
        column = model.getColumn(4);
        column.setPreferredWidth((int) (50));
        column = model.getColumn(5);
        column.setPreferredWidth((int) (30));
        column = model.getColumn(6);
        column.setPreferredWidth((int) (30));
        column = model.getColumn(7);
        column.setPreferredWidth((int) (200));
	}

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

        int viewIdx = row;
        int modelIdx = convertRowIndexToModel(viewIdx);

    	MessageContainer messaingeContainer = ((MessageTableModel) getModel()).getMessage(modelIdx);
        String direction = messaingeContainer.getDirection();
        DefaultTableCellRenderer r = (DefaultTableCellRenderer) renderer;

        if ("Outbound".equals(direction))
        	r.setForeground(Color.blue);
        else
        	r.setForeground(Color.black);

        return super.prepareRenderer(renderer, row, column);
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() != 2)
            return;
        //int row = rowAtPoint(e.getPoint());
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}
}
