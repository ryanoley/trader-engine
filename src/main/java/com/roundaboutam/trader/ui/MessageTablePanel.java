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
import com.roundaboutam.trader.MessageContainer;

import quickfix.Message;
import ramfix.MessageType;



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
    	if (messageContainer.getMessageType() != MessageType.HEARTBEAT) {
	    	rowToMessage.put(row, messageContainer);
	    	rowToTimeStamp.put(row, new Date(System.currentTimeMillis()));
	        fireTableRowsInserted(row, row);
    	}
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
        	MessageType messageType = messageContainer.getMessageType();
        	String typeString = (messageType == null) ? null : messageType.toString();
        	return replaceNull(typeString);
        case SYMBOL:
        	return replaceNull(messageContainer.getSymbol());
        case ORDID:
        	return replaceNull(messageContainer.getDisplayID());
        case ORDQTY:
        	Integer dispQty = messageContainer.getDisplayQty();
        	String qtyString = (dispQty == null) ? null : dispQty.toString();
        	return replaceNull(qtyString);
        case STATUS:
        	return replaceNull(messageContainer.getDisplayStatus());
        case TEXT:
        	return replaceNull(messageContainer.getText());
        case MESSAGE:
            return replaceNull(messageContainer.getMessage().toString());
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

        int modelIdx = convertRowIndexToModel(row);
    	MessageContainer messaingeContainer = ((MessageTableModel) getModel()).getMessage(modelIdx);
        String direction = messaingeContainer.getDirection();
        Component c = super.prepareRenderer(renderer, row, column); 

        if ("Outbound".equals(direction))
        	c.setForeground(Color.blue);
        else
        	c.setForeground(Color.black);
    
        JComponent jc = (JComponent) c;
        if (isRowSelected(row)){
          c.setBackground(Color.white);
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
