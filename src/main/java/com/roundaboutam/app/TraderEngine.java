package com.roundaboutam.app;

import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.roundaboutam.app.ui.TraderFrame;

public class TraderEngine {
	private static final CountDownLatch shutdownLatch = new CountDownLatch(1);
	 
	private static TraderEngine traderEngine;
	private JFrame frame = null;

	public TraderEngine() {

		OrderTableModel orderTableModel = new OrderTableModel();
        ExecutionTableModel executionTableModel = new ExecutionTableModel();
    	TraderApplication application = new TraderApplication(orderTableModel,
    			executionTableModel);
    	
    	frame = new TraderFrame(orderTableModel, executionTableModel, application);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
    public synchronized void logon() {
    	System.out.println("Logging on");
    }
    
    public void logout() {
    	System.out.println("Logging out");
    }
	
	public static TraderEngine get() {
		return traderEngine;
	}

    public static void main( String[] args ) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        	e.printStackTrace();
        }
    	traderEngine = new TraderEngine();

    	try {
			shutdownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    
}
