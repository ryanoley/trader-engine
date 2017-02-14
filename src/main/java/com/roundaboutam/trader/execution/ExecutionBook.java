package com.roundaboutam.trader.execution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.roundaboutam.trader.MessageContainer;
import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.OrderSide;

import quickfix.ConfigError;
import quickfix.FieldConvertError;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.field.LastPx;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.SymbolSfx;
import quickfix.field.TransactTime;

public class ExecutionBook {

	private BufferedWriter logFile;
	private boolean logFlag = false;
	private String logFilePath;

	public ExecutionBook(SessionSettings settings){
		try {
			logFilePath = settings.getString("CustomLogPath");
		} catch (ConfigError | FieldConvertError e) {
			e.printStackTrace();
		}
	}

	public void setExecutionLog(SessionID sessionID) {
		String senderCompID = sessionID.getSenderCompID();
		String targetCompID = sessionID.getTargetCompID();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();
		String filePath = logFilePath + "\\executions\\" + senderCompID + "_" + targetCompID + 
				"_" + dateFormat.format(date) + ".txt";
		try {
			logFile = new BufferedWriter(new FileWriter(filePath, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
		logFlag = true;
	}

	public void processExecutionReport(MessageContainer messageContainer, Order order) {
		char execType = messageContainer.rawValues.get("ExecType").charAt(0);
		
    	switch (execType) {
    	case quickfix.field.ExecType.PARTIAL_FILL:
    	case quickfix.field.ExecType.FILL:
    		processFill(messageContainer, order);
    		break;
    	case quickfix.field.ExecType.CANCELED:
    		processCancel(messageContainer, order);
    		break;
    	case quickfix.field.ExecType.REPLACE:
    		processReplace(messageContainer, order);
    		break;
    	case quickfix.field.ExecType.REJECTED:
    		processReject(messageContainer, order);
    		break;
    	}

	}

	private void processReplace(MessageContainer messageContainer, Order order) {
    	Execution execution = new Execution(
    			messageContainer.getClOrdID(),
    			order.getPermanentID(),
    			messageContainer.getSymbol(),
    			messageContainer.getTransactTime(),
    			messageContainer.getSide(),
    			Integer.parseInt(messageContainer.getLastShares()),
    			Double.parseDouble(messageContainer.getLastPx()),
    			Execution.REPLACE
    			);
        if (!messageContainer.getSymbolSfx().equals("FieldNotFound")) {
            execution.setSuffix(messageContainer.getSymbolSfx());
        }
        execution.setCustomTag(order.getCustomTag());
        // TODO: Market data used here to capture BidAsk
        execution.setBid(0);
        execution.setAsk(0);
        addExecution(execution);
	}

	private void processCancel(MessageContainer messageContainer, Order order) {
    	Execution execution = new Execution(
    			messageContainer.getClOrdID(),
    			order.getPermanentID(),
    			messageContainer.getSymbol(),
    			messageContainer.getTransactTime(),
    			messageContainer.getSide(),
    			Integer.parseInt(messageContainer.getLastShares()),
    			Double.parseDouble(messageContainer.getLastPx()),
    			Execution.CANCEL
    			);
        if (!messageContainer.getSymbolSfx().equals("FieldNotFound")) {
            execution.setSuffix(messageContainer.getSymbolSfx());
        }
        execution.setCustomTag(order.getCustomTag());
        // TODO: Market data used here to capture BidAsk
        execution.setBid(0);
        execution.setAsk(0);
        addExecution(execution);
	}


	private void processReject(MessageContainer messageContainer, Order order) {
    	Execution execution = new Execution(
    			messageContainer.getClOrdID(),
    			order.getPermanentID(),
    			messageContainer.getSymbol(),
    			messageContainer.getTransactTime(),
    			messageContainer.getSide(),
    			Integer.parseInt(messageContainer.getLastShares()),
    			Double.parseDouble(messageContainer.getLastPx()),
    			Execution.REJECT
    			);
        if (!messageContainer.getSymbolSfx().equals("FieldNotFound")) {
            execution.setSuffix(messageContainer.getSymbolSfx());
        }
        execution.setCustomTag(order.getCustomTag());
        // TODO: Market data used here to capture BidAsk
        execution.setBid(0);
        execution.setAsk(0);
        addExecution(execution);
	}

	private void processFill(MessageContainer messageContainer, Order order) {
		int fillSize;
		try {
			fillSize = Integer.parseInt(messageContainer.getLastShares());
		} catch (NumberFormatException e) {
			fillSize = 0;
		}

		if (fillSize > 0) {
        	Execution execution = new Execution(
        			messageContainer.getClOrdID(),
        			order.getPermanentID(),
        			messageContainer.getSymbol(),
        			messageContainer.getTransactTime(),
        			messageContainer.getSide(),
        			Integer.parseInt(messageContainer.getLastShares()),
        			Double.parseDouble(messageContainer.getLastPx()),
        			Execution.FILL
        			);

            if (!messageContainer.getSymbolSfx().equals("FieldNotFound")) {
                execution.setSuffix(messageContainer.getSymbolSfx());
            }
            execution.setCustomTag(order.getCustomTag());
            // TODO: Market data used here to capture BidAsk
            execution.setBid(0);
            execution.setAsk(0);
            addExecution(execution);
        }
	}
	
	public void addExecution(Execution execution) {
		if (logFlag) {
			try {
				logFile.write(execution.getLogEntry() + "\n");
				logFile.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeExecutionLog(SessionID sessionID) {
		try {
			if (logFlag) {
				logFile.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logFlag = false;
	}
}
