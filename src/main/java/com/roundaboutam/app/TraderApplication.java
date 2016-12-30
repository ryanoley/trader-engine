package com.roundaboutam.app;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;

public class TraderApplication implements Application {

    private OrderTableModel orderTableModel = null;
    private ExecutionTableModel executionTableModel = null;

	public TraderApplication(OrderTableModel orderTableModel,
            ExecutionTableModel executionTableModel) {
        this.orderTableModel = orderTableModel;
        this.executionTableModel = executionTableModel;
	}

	public void fromAdmin(Message arg0, SessionID arg1)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		// TODO Auto-generated method stub
	}

	public void fromApp(Message arg0, SessionID arg1)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		// TODO Auto-generated method stub		
	}

	public void onCreate(SessionID arg0) {
		// TODO Auto-generated method stub
	}

	public void onLogon(SessionID arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onLogout(SessionID arg0) {
		// TODO Auto-generated method stub
		
	}

	public void toAdmin(Message arg0, SessionID arg1) {
		// TODO Auto-generated method stub
		
	}

	public void toApp(Message arg0, SessionID arg1) throws DoNotSend {
		// TODO Auto-generated method stub
		
	}

}
