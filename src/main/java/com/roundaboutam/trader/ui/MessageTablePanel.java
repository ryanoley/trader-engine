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

import edu.emory.mathcs.backport.java.util.Arrays;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.TransactTime;
import quickfix.field.ClOrdID;




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

    private final static int TIMESTAMP = 0;
    private final static int ORDID = 1;
    private final static int MESSAGE = 2;
    private final HashMap<Integer, Message> rowToMessage;
    private final HashMap<Integer, Date> rowToTimeStamp;

    public final String[] headers = new String[] {"TimeStamp", "OrdID", "Message"};

    public MessageTableModel(TraderApplication application) {
    	application.addMessageObserver(this);
    	rowToMessage = new HashMap<Integer, Message>();
    	rowToTimeStamp = new HashMap<Integer, Date>();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    private void addMessage(Message message) {
    	int row = rowToMessage.size();
    	rowToMessage.put(row, message);
    	rowToTimeStamp.put(row, new Date(System.currentTimeMillis()));
        fireTableRowsInserted(row, row);
    }
  
    public void setValueAt(Object value, int rowIndex, int columnIndex) { }

    public Class<String> getColumnClass(int columnIndex) {
        return String.class;
    }

    public int getRowCount() {
    	return rowToMessage.size();
    }

    public int getColumnCount() {
        return headers.length;
    }

    public String getColumnName(int columnIndex) {
        return headers[columnIndex];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
    	Message message = rowToMessage.get(rowIndex);
    	Date timeStamp = rowToTimeStamp.get(rowIndex);
    	switch (columnIndex) {
        case TIMESTAMP:
        	SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yy HH:mm:ss");  
        	return sdf.format(timeStamp);
        case ORDID:
            try {
				return message.getString(ClOrdID.FIELD);
			} catch (FieldNotFound e) {
				return "#NA";
			}
        case MESSAGE:
            return message.toString();
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

	private transient TraderApplication application;

    public MessageTable(TraderApplication application) {
        super(new MessageTableModel(application));
        this.application = application;
        initColumnWidths();
        addMouseListener(this);
    }
 
	private void initColumnWidths() {
		TableColumnModel model = this.getColumnModel();
        TableColumn column = model.getColumn(0);
        column.setPreferredWidth((int) (100));
        column = model.getColumn(1);
        column.setPreferredWidth((int) (100));
        column = model.getColumn(2);
        column.setPreferredWidth((int) (500));
	}

	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        DefaultTableCellRenderer r = (DefaultTableCellRenderer) renderer;
        r.setBackground(Color.white);
        return super.prepareRenderer(renderer, row, column);
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() != 2)
            return;
        int row = rowAtPoint(e.getPoint());
        System.out.println(row);
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}
}
