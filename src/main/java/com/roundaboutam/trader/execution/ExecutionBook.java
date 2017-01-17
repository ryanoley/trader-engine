package com.roundaboutam.trader.execution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExecutionBook {

	BufferedWriter logFile;
	boolean logFlag = false;

	public ExecutionBook(String logPath) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
		Date date = new Date();
		String filePath = logPath + "\\executions\\" + dateFormat.format(date) + ".txt";
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
}
