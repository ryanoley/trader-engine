package com.roundaboutam.trader.zmq;

import java.util.HashMap;
import java.util.HashSet;

/*
 * 
 */


import org.zeromq.ZMQ;

import com.roundaboutam.trader.TraderApplication;
import com.roundaboutam.trader.rmp.MessageClass;
import com.roundaboutam.trader.rmp.Parser;
import com.roundaboutam.trader.rmp.SourceApp;




public class ZMQServer implements Runnable{

	private Boolean started = false;
	private Boolean connected = false;
	private ZMQ.Context context = null;
	private ZMQ.Socket responder = null;
	volatile boolean shutdown;
	private HashSet<String> rmpConnections;
	private transient TraderApplication application = null;
	private Integer port = new Integer(5555);

	public ZMQServer(TraderApplication application, Integer port) {
		context = ZMQ.context(1);
		rmpConnections = new HashSet<String>();
		this.application = application;
		this.port = port;
	}


	public void run() {
		if (isStarted() == true) {
			throw new IllegalStateException("ZMQ Server aleady started.");
		}
		responder = context.socket(ZMQ.REP);
		responder.setReceiveTimeOut(5000);
	    responder.bind("tcp://*:" + port);
	    setStarted(true);
	    shutdown = false;
	    System.out.println("ZMQ - ZMQ Socket listening on port:" + port);

		while (!shutdown) {
	        // Wait for next request from the client
			String replyString = "";
	        byte[] request = responder.recv(0);
	        if (request == null)
	        	continue;
	        
	        String requestString = new String(request);
	        System.out.println("ZMQ - " + requestString);
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
	    		replyString = "KNOWN SOURCE";
	    	}
	    	else {
	    		System.out.println("ZMQ - Message from unknown source: " + sourceApp);
	    		replyString = "UNKNOWN SOURCE";
	    	}
	    	setConnected(rmpConnections.size() > 0 ? true : false);
	    	// Send reply back to client
	        responder.send(replyString.getBytes(), 0);
	    }
	    System.out.println("ZMQ Server shutdown");
		responder.close();
	    context.term();
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
        shutdown = true;
    }

}