package com.roundaboutam.trader;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roundaboutam.trader.ui.TraderFrame;

import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

import org.quickfixj.jmx.JmxExporter;

public class TraderEngine {

	private static final CountDownLatch shutdownLatch = new CountDownLatch(1);

	private static final Logger log = LoggerFactory.getLogger(TraderEngine.class);
	private static boolean initiatorStarted = false;

	private static TraderEngine traderEngine;
	private static Initiator initiator;
	private TraderApplication application;

	public TraderEngine() throws Exception {

		SessionSettings settings = getSettings();

		application = new TraderApplication(settings);

        initiator = new SocketInitiator(
        		application, 
        		new FileStoreFactory(settings), 
        		settings, 
        		new FileLogFactory(settings),
        		new DefaultMessageFactory());

        JmxExporter exporter = new JmxExporter();
        exporter.register(initiator);

        new TraderFrame(application);
	}

	private SessionSettings getSettings() throws ConfigError, IOException {
		InputStream inputStream = TraderEngine.class.getResourceAsStream("FIXConfig.cfg");
        SessionSettings settings = new SessionSettings(inputStream);
        inputStream.close();

        // Workaround for env variables
        String rootpath = System.getenv("DATA") + "\\trader-engine\\LOGS";
        settings.setString("FileStorePath", rootpath + "\\fileStore");
        settings.setString("FileLogPath", rootpath + "\\logs");
        settings.setString("CustomLogPath", rootpath + "\\custom");
        return settings;
	}

    public synchronized void logon() {
    	System.out.println("TraderEngine.logon() invoked");
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
    		if (Session.lookupSession(sessionId).isLoggedOn())
    			Session.lookupSession(sessionId).logout("User requested");
        }
    }

    public void stopInitiator() {
    	logout();
    	if(initiatorStarted)
    		initiator.stop();
        initiatorStarted = false;
    }

    public boolean getInitiatorState() {
    	return initiatorStarted;
    }

    public void shutdown() {
    	System.out.println("TraderEngine.shutdown() invoked");
    	System.out.println(application.getSessionIDs().size());
    	logout();
        initiator.stop();
        while (application.getSessionIDs().size() > 0) {
        	try { Thread.sleep(1000); } catch (InterruptedException e) { }
        }
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
        shutdownLatch.await();
    }

}
