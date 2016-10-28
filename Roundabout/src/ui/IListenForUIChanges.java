package ui;

public interface IListenForUIChanges {
	public void notifyOpenOrdersChanged();
	public void notifyClosedOrdersChanged();
	
	public void updateStats();
	public void updateConnection(boolean connected);
	
}
