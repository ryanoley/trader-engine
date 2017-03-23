package com.roundaboutam.trader.execution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.roundaboutam.trader.MessageContainer;
import com.roundaboutam.trader.order.Order;

import quickfix.SessionID;



public class ExecutionBook {

	private BufferedWriter executionFile;
	private BufferedWriter cancelRejectFile;
	private boolean logFlag = false;
	private String logFilePath;
	private final HashMap<String, Execution> executionMap;
	private final HashMap<String, OrderCancelReject> orderCancelRejectMap;
	

	public ExecutionBook(String filePath){
		executionMap = new HashMap<String, Execution>();
		orderCancelRejectMap = new HashMap<String, OrderCancelReject>();
		this.logFilePath = filePath;
	}

	public void setExecutionLogs(SessionID sessionID) {
		String senderCompID = sessionID.getSenderCompID();
		String targetCompID = sessionID.getTargetCompID();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();
		String executionLogPath = logFilePath + "\\executions\\" + senderCompID + "_" + targetCompID + 
				"_" + dateFormat.format(date) + ".executions.txt";
		String orderCancelRejectLogPath = logFilePath + "\\executions\\" + senderCompID + "_" + targetCompID + 
				"_" + dateFormat.format(date) + ".ordercancelreject.txt";
		try {
			executionFile = new BufferedWriter(new FileWriter(executionLogPath, true));
			cancelRejectFile = new BufferedWriter(new FileWriter(orderCancelRejectLogPath, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
		logFlag = true;
	}

	public void processExecutionReport(MessageContainer messageContainer, Order order) {
    	Execution execution = new Execution(
    			messageContainer.getClOrdID(),
    			order.getPermanentID(),
    			messageContainer.getSymbol(),
    			messageContainer.getTransactTime(),
    			messageContainer.getOrderSide(),
    			messageContainer.getLastShares(),
    			messageContainer.getLastPx(),
    			messageContainer.getExecutionType()
    			);
 
        if (messageContainer.getSymbolSfx() != null) {
            execution.setSuffix(messageContainer.getSymbolSfx());
        }
        execution.setCustomTag(order.getCustomTag());
        // TODO: Market data used here to capture BidAsk
        execution.setBid(0);
        execution.setAsk(0);
        addExecution(execution);
	}
	
	public void addExecution(Execution execution) {
		executionMap.put(execution.getID(), execution);
		if (logFlag) {
			try {
				executionFile.write(execution.getLogEntry() + "\n");
				executionFile.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void processOrderCancelReject(MessageContainer messageContainer, Order order){
		OrderCancelReject orderCancelReject = new OrderCancelReject(
    			messageContainer.getClOrdID(),
    			order.getPermanentID(),
    			order.getSymbol(),
    			messageContainer.getSendingTime(),
    			order.getOrderSide(),
    			messageContainer.getText()
    			);
        if (messageContainer.getSymbolSfx() != null) {
        	orderCancelReject.setSuffix(messageContainer.getSymbolSfx());
        }
        orderCancelReject.setCustomTag(order.getCustomTag());
		addOrderCancelReject(orderCancelReject);
	}

	public void addOrderCancelReject(OrderCancelReject orderCancelReject) {
		orderCancelRejectMap.put(orderCancelReject.getID(), orderCancelReject);
		if (logFlag) {
			try {
				cancelRejectFile.write(orderCancelReject.getLogEntry() + "\n");
				cancelRejectFile.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeExecutionLogs(SessionID sessionID) {
		try {
			if (logFlag) {
				executionFile.close();
				cancelRejectFile.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logFlag = false;
	}
	
}

