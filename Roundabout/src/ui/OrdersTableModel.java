package ui;

import java.util.ArrayList;

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
import quickfix.field.SymbolSfx;
import main.OrdersContainer;

public class OrdersTableModel extends AbstractTableModel {
	
	String[] colNames = new String[] {"Ticker","Type","Shares","Limit","Filled","AvgPrice","oTime","lTime","OrdID"};
	
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
		if (OrdersContainer.ordIDtoMessagesOpen.size() != ordIDs.size()) {
			ordIDs = new ArrayList<String>(OrdersContainer.ordIDtoMessagesOpen.keySet());
			for (String id : ordIDs)
				System.out.println("open:\t" +id);
		}
		super.fireTableDataChanged();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (row >= ordIDs.size())
			return null;
		
		String id = ordIDs.get(row);
		
		ArrayList<Message> msgs = OrdersContainer.ordIDtoMessagesOpen.get(id);
		if (msgs == null || msgs.size() == 0)
			return null;
		
		Message msg = msgs.get(msgs.size()-1);
		
		if (col < 0)
			return msg;

		try {
			if (col == 0) {
				if (msg.isSetField(SymbolSfx.FIELD))
					return msg.getString(Symbol.FIELD) + "." + msg.getString(SymbolSfx.FIELD);
				return msg.getString(Symbol.FIELD);
			}
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
			if (col == 8)
				return msg.getString(ClOrdID.FIELD);
			
		} catch (FieldNotFound e) {
			e.printStackTrace();
			return null;
		}

		// TODO
		return null;
	}
	
	

}
