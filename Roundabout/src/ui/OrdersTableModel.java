package ui;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;

public class OrdersTableModel extends AbstractTableModel implements ListDataListener {

	@Override
	public String getColumnName(int arg0) {
		if (arg0 == 0)
			return "A";
		if (arg0 == 1)
			return "B";
		return super.getColumnName(arg0);
	}

	@Override
	public int getColumnCount() {
		// TODO
		return 2;
	}

	@Override
	public int getRowCount() {
		// TODO 
		return 5;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
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
