package com.roundaboutam.trader;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
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
	private TraderFrame frame;

	public TraderEngine(String launchEnv) throws Exception {
		System.out.println(launchEnv);
		SessionSettings settings = getSettings(launchEnv);
		application = new TraderApplication(settings);

        initiator = new SocketInitiator(
        		application,
        		new FileStoreFactory(settings), 
        		settings, 
        		new FileLogFactory(settings),
        		new DefaultMessageFactory());

        JmxExporter exporter = new JmxExporter();
        exporter.register(initiator);
        frame = new TraderFrame(application);
        startLogOnMonitor(launchEnv);
	}

	private SessionSettings getSettings(String launchEnv) throws ConfigError, IOException {
		InputStream inputStream;
		String rootpath;
		if (launchEnv.equals("prod")){
			inputStream = TraderEngine.class.getResourceAsStream("FIXConfigPROD.cfg");
			rootpath = "C:\\FIX\\LOGS";
		}
		else {
			inputStream = TraderEngine.class.getResourceAsStream("FIXConfig.cfg");
			rootpath = System.getenv("DATA") + "\\trader-engine\\LOGS";
		}

        SessionSettings settings = new SessionSettings(inputStream);
        inputStream.close();

        // Workaround for env variables
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
                System.out.println("TraderEngine Log On Successfull");
            } catch (Exception e) {
                log.error("Logon failed", e);
            }
        } else {
        	System.out.println("TraderEngine - Log on attempt with Initiator Started");
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
    	if (initiatorStarted)
    		stopInitiator();
        while (application.getSessionID() != null) {
        	try { Thread.sleep(1000); } catch (InterruptedException e) { }
        }
        shutdownLatch.countDown();
        System.exit(0);
    }

	public static TraderEngine get() {
		return traderEngine;
	}

    public TraderFrame getTraderFrame() {
    	return frame;
    }

	private void startLogOnMonitor(String launchEnv) {
		class LogOnMonitor implements Runnable {
			int currentTime;
			public LogOnMonitor() {
			}
			public void run() {
				try {
					SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
					timeFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
					while (true) {
						currentTime = Integer.parseInt(timeFormat.format(new Date()));
						if (currentTime >= 1730 && getInitiatorState()) {
							shutdown();
						}
			            Thread.sleep(300000);
			         }
			      }
			      catch (Exception e) {e.printStackTrace();}
			}
		}
		if (launchEnv.equals("prod")){
			logon();
			frame.setFixButtonText("Stop FIX");
			Thread t = new Thread(new LogOnMonitor());
			t.start();
		}
	}

	public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        String launchArg;
        if (args.length == 0)
        	launchArg = "uat";
        else if (args[0].toUpperCase().equals("PROD"))
        	launchArg = "prod";
        else
        	launchArg = "uat";
    	traderEngine = new TraderEngine(launchArg);
        shutdownLatch.await();
    }

}
