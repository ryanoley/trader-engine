package com.roundaboutam.trader;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roundaboutam.trader.ui.TraderFrame;

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

		InputStream inputStream = TraderEngine.class.getResourceAsStream("FIXConfig.cfg");
        SessionSettings settings = new SessionSettings(inputStream);
        inputStream.close();

        boolean logHeartbeats = Boolean.valueOf(System.getProperty("logHeartbeats", "false"));

        // Workaround for env variables
        String rootpath = System.getenv("DATA") + "\\trader-engine\\LOGS";
        settings.setString("FileStorePath", rootpath + "\\fileStore");
        settings.setString("FileLogPath", rootpath + "\\logs");
        settings.setString("CustomLogPath", rootpath + "\\custom");

        TraderApplication application = new TraderApplication(settings);

        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new ScreenLogFactory(true, true, true, logHeartbeats);
        MessageFactory messageFactory = new DefaultMessageFactory();

        initiator = new SocketInitiator(application, messageStoreFactory, settings, logFactory,
                messageFactory);

        JmxExporter exporter = new JmxExporter();
        exporter.register(initiator);

        new TraderFrame(application);
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
    	System.out.println("TraderEngine.logout() invoked");
    	for (SessionID sessionId : initiator.getSessions()) {
        	System.out.println("Shutting down session: " + sessionId.toString());
            Session.lookupSession(sessionId).logout("User requested");
        }
    }

    public void stopInitiator() {
    	System.out.println("TraderEngine.stopInitiator() invoked");
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
    	try {
    		logout();
    		shutdownLatch.countDown();
    	} catch (Exception e) { }
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
