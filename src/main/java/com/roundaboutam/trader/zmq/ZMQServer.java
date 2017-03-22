package com.roundaboutam.trader.zmq;

/*
 *  zmq docs: http://zguide.zeromq.org/page:all
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.zeromq.ZMQ;

import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.execution.Execution;
import com.roundaboutam.trader.rmp.MessageClass;
import com.roundaboutam.trader.rmp.Parser;
import com.roundaboutam.trader.rmp.SourceApp;




public class ZMQServer implements Runnable{
	private static final char Inbound = 'I'; 
	private static final char Outbound = 'O'; 
	
	private ZMQ.Context context = null;
	private Boolean started = false;
	private Boolean connected = false;
	volatile Boolean shutdown = false;
	private HashSet<String> rmpConnections;
	private BufferedWriter zmqLogFile;
	private transient TraderApplication application = null;
	private Integer port = new Integer(5555);

	public ZMQServer(TraderApplication application, Integer port, String logFilePath) {
		context = ZMQ.context(1);
		rmpConnections = new HashSet<String>();
		this.application = application;
		this.port = port;
		setLogFile(logFilePath);
	}

	public void run() {
		if (isStarted() == true) {
			throw new IllegalStateException("ZMQ Server aleady started.");
		}
		ZMQ.Socket responder = context.socket(ZMQ.REP);
		responder.setReceiveTimeOut(5000);
	    responder.bind("tcp://*:" + port);
	    setStarted(true);

		while (!shutdown) {
	        // Wait for next request from the client
			String replyString = "";
	        byte[] request = responder.recv(0);
	        if (request == null)
	        	continue;
	        
	        String requestString = new String(request);
	    	HashMap<Integer, String> fieldMap = Parser.getFieldMap(requestString);
	    	MessageClass messageClass = MessageClass.parse(fieldMap.get(MessageClass.RMPFieldID));
	    	String sourceApp = fieldMap.get(SourceApp.RMPFieldID);
    		
	    	if (messageClass == MessageClass.OPEN_RMP_CONNECTION | messageClass == MessageClass.CLOSE_RMP_CONNECTION) {
	    		if (messageClass == MessageClass.OPEN_RMP_CONNECTION) {
	    			rmpConnections.add(sourceApp);
	    			replyString = "1=RMP|3=ORC|4=TRADERENGINE|5=" + sourceApp;
	    		} else {
	    			rmpConnections.remove(sourceApp);
	    			replyString = "1=RMP|3=CRC|4=TRADERENGINE|5=" + sourceApp;
	    		}
	    	} else if (rmpConnections.contains(sourceApp)) {
	    		application.fromRMP(requestString);
	    		replyString = "RMP PARSING";
	    	}
	    	else {
	    		System.out.println("ZMQ - Message from unknown source: " + sourceApp);
	    		replyString = "UNKNOWN SOURCE";
	    	}
	    	setConnected(rmpConnections.size() > 0 ? true : false);
	    	// Send reply back to client
	        responder.send(replyString.getBytes(), 0);
	        toLog(requestString, Inbound);
	        toLog(replyString, Outbound);	
	    }
	    System.out.println("ZMQ Server shutdown");
		responder.close();
	    context.term();
	}

	public void setLogFile(String filePath) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();
		String zmqLogPath = filePath + "\\zmq\\" + dateFormat.format(date) + ".zmq.txt";
		try {
			zmqLogFile = new BufferedWriter(new FileWriter(zmqLogPath, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeZMQLogs() {
		try {
			zmqLogFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void toLog(String zmqString, char dir) {
		try {
			zmqLogFile.write(dir + "|"+ zmqString + "\n");
			zmqLogFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Boolean isConnected() {
		return connected;
	}
	
	private void setConnected(Boolean bool) {
		connected = bool;
	}

	public Boolean isStarted() {
		return started;
	}
	
	private void setStarted(Boolean bool) {
		started = bool;
	}
	
    public void shutdown() {
    	closeZMQLogs();
        shutdown = true;
    }

}