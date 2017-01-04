package com.roundaboutam.app;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.quickfixj.jmx.JmxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roundaboutam.app.ui.TraderFrame;

import quickfix.DefaultMessageFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

public class TraderEngine {

	private static final CountDownLatch shutdownLatch = new CountDownLatch(1);

	private static final Logger log = LoggerFactory.getLogger(TraderEngine.class);
	private static TraderEngine traderEngine;
	private boolean initiatorStarted = false;
	private Initiator initiator = null;
	private JFrame frame = null;

	public TraderEngine() throws Exception {

		// Get FIX Config
		InputStream inputStream = TraderEngine.class.getResourceAsStream("FIXConfig.cfg");
        SessionSettings settings = new SessionSettings(inputStream);
        inputStream.close();
        boolean logHeartbeats = Boolean.valueOf(System.getProperty("logHeartbeats", "true"));
        
		OrderTableModel orderTableModel = new OrderTableModel();
        ExecutionTableModel executionTableModel = new ExecutionTableModel();
        TraderApplication application = new TraderApplication(orderTableModel,
    			executionTableModel);
        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new ScreenLogFactory(true, true, true, logHeartbeats);
        MessageFactory messageFactory = new DefaultMessageFactory();

        initiator = new SocketInitiator(application, messageStoreFactory, settings, logFactory,
                messageFactory);

        JmxExporter exporter = new JmxExporter();
        exporter.register(initiator);

    	frame = new TraderFrame(orderTableModel, executionTableModel, application);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
    public synchronized void logon() {
    	System.out.println("Logging on");
        if (!initiatorStarted) {
            try {
                initiator.start();
                initiatorStarted = true;
            } catch (Exception e) {
                log.error("Logon failed", e);
            }
        } else {
            for (SessionID sessionId : initiator.getSessions()) {
                Session.lookupSession(sessionId).logon();
            }
        }
    }
    
    public void logout() {
        for (SessionID sessionId : initiator.getSessions()) {
            Session.lookupSession(sessionId).logout("user requested");
        }
    }
	
    public void stop() {
        shutdownLatch.countDown();
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
	public static TraderEngine get() {
		return traderEngine;
	}

	public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
    	traderEngine = new TraderEngine();

        if (!System.getProperties().containsKey("openfix")) {
        	traderEngine.logon();
        }
        shutdownLatch.await();
    }
    
}
