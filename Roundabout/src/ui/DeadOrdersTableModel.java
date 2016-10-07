package ui;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;

public class DeadOrdersTableModel extends AbstractTableModel implements ListDataListener {
	
	String[] colNames = new String[] {"Ticker","Type","Shares","Limit","Filled","AvgPrice","oTime","lTime","Status"};

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
		// TODO 
		return 5;
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO
		return null;
	}
	
	

	@Override
	public void contentsChanged(ListDataEvent arg0) {
		// TODO 
		
	}

	@Override
	public void intervalAdded(ListDataEvent arg0) {
		// TODO
		
	}

	@Override
	public void intervalRemoved(ListDataEvent arg0) {
		// TODO 
		
	}

}
