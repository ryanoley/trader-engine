package ui;

import java.util.ArrayList;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.AvgPx;
import quickfix.field.ClOrdID;
import quickfix.field.CumQty;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import main.OrdersContainer;

public class DeadOrdersTableModel extends AbstractTableModel {
	
	ArrayList<String> ordIDs = new ArrayList<>();
	
	
	@Override
	public String getColumnName(int col) {
		if (col < colNames.length)
			return colNames[col];
		return super.getColumnName(col);
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		return ordIDs.size();
	}

	@Override
	public void fireTableDataChanged() {
		if (OrdersContainer.ordIDtoMessagesClosed.size() != ordIDs.size()) {
			ordIDs = new ArrayList<String>(OrdersContainer.ordIDtoMessagesClosed.keySet());
		}
		super.fireTableDataChanged();
	}


	@Override
	public Object getValueAt(int row, int col) {
		if (row >= ordIDs.size())
			return null;
		
		String id = ordIDs.get(row);
		
		ArrayList<Message> msgs = OrdersContainer.ordIDtoMessagesClosed.get(id);
		if (msgs.size() == 0)
			return null;
		
		Message msg = msgs.get(msgs.size()-1);

		try {
			if (col == 0)
				return msg.getString(Symbol.FIELD);
			if (col == 1)
				if (msg.getChar(Side.FIELD) == Side.BUY)
					return "B";
				else if (msg.getChar(Side.FIELD) == Side.SELL)
					return "S";
				else return "SS";
			if (col == 2)
				return msg.getDouble(OrderQty.FIELD);
			if (col == 3 && msg.isSetField(Price.FIELD))
				return msg.getDouble(Price.FIELD);
			if (col == 4 && msg.isSetField(CumQty.FIELD))
				return msg.getInt(CumQty.FIELD);
			if (col == 5 && msg.isSetField(AvgPx.FIELD))
				return msg.getDouble(AvgPx.FIELD);
			if (col == 9)
				return msg.getString(ClOrdID.FIELD);
			
		} catch (FieldNotFound e) {
			e.printStackTrace();
			return null;
		}
		// TODO also, remember to check for rejects in Status

		return null;
	}
	
	
	String[] colNames = new String[] {"Ticker","Type","Shares","Limit","Filled","AvgPrice","oTime","lTime","Status","OrdID"};

	
	

}
