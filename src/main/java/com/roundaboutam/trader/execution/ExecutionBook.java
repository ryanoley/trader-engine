package com.roundaboutam.trader.execution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import quickfix.ConfigError;
import quickfix.FieldConvertError;
import quickfix.SessionID;
import quickfix.SessionSettings;

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

	public void addExecution(Execution execution) {
		if (logFlag) {
			try {
				logFile.write(execution.getLogEntry());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeExecutionLog(SessionID sessionID) {
		try {
			if (logFlag) {
				logFile.write("Hello World");
				logFile.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logFlag = false;
	}
}
