package com.roundaboutam.app;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import javax.swing.UIManager;

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
import org.quickfixj.jmx.JmxExporter;

public class TraderEngine {

	private static final CountDownLatch shutdownLatch = new CountDownLatch(1);

	private static final Logger log = LoggerFactory.getLogger(TraderEngine.class);
	private static TraderEngine traderEngine;
	private boolean initiatorStarted = false;
	private Initiator initiator = null;

	public TraderEngine() throws Exception {

		// Get FIX Config
		InputStream inputStream = TraderEngine.class.getResourceAsStream("FIXConfig.cfg");
        SessionSettings settings = new SessionSettings(inputStream);
        inputStream.close();
        boolean logHeartbeats = Boolean.valueOf(System.getProperty("logHeartbeats", "false"));

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

    	new TraderFrame(orderTableModel, executionTableModel, application);

	}
	
    public synchronized void logon() {
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
        	System.out.println("Shutting down session: " + sessionId.toString());
            Session.lookupSession(sessionId).logout("user requested");
        }
    }

    public void shutdown() {
    	logout();
    	shutdownLatch.countDown();
    	System.exit(0);
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
