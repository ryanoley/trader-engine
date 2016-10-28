package fix;

import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.field.MsgSeqNum;
import quickfix.fix42.ExecutionReport;
import quickfix.fix42.Heartbeat;
import quickfix.fix42.Logon;
import quickfix.fix42.Logout;
import quickfix.fix42.MessageCracker;
import quickfix.fix42.OrderCancelReject;
import quickfix.fix42.Reject;
import ui.IListenForUIChanges;

public class MessageHandler extends MessageCracker {
	

	int lastSequenceNumber = 0;
	ExecutionHandler handler = new ExecutionHandler();
	
	
	public void notifySessionUpdate(SessionID sessionID) {
		// TODO	update ui and possibly strategy clients		
		System.out.println("Session logon.");
		uiListener.updateConnection(true);
	}

	public void notifyDisconnect() {
		// TODO update ui and strategy clients
		System.out.println("Session disconnected.");
	}

	@Override
	public void onMessage(ExecutionReport message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
		// ignore poss dup messages
		// checks if it's processed that sequence number or less...
		if (message.isSetField(43) && message.getBoolean(43)) {
			if (message.getHeader().isSetField(MsgSeqNum.FIELD)) {
				if (message.getHeader().getInt(MsgSeqNum.FIELD) <= lastSequenceNumber)
					return;
			} else
				System.out.println("No seq num found to compare:\t" + message);
		}
		else
			lastSequenceNumber = message.getHeader().getInt(MsgSeqNum.FIELD);
			
		if (handler != null)
			handler.handleMessage(message);
		
	}

	@Override
	public void onMessage(Heartbeat message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
//		super.onMessage(message, sessionID);
	}

	@Override
	public void onMessage(Logon message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		super.onMessage(message, sessionID);
	}

	@Override
	public void onMessage(Logout message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		super.onMessage(message, sessionID);
	}

	@Override
	public void onMessage(OrderCancelReject message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO handle cancel reject
		System.out.println("NOT WIRED FOR ORDER CANCEL REJECT MSG...");
		super.onMessage(message, sessionID);
	}

	@Override
	public void onMessage(Reject message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO handle order reject
		System.out.println("NOT WIRED FOR REJECT MSG...");
		super.onMessage(message, sessionID);
	}

	IListenForUIChanges uiListener = null;
	
	public void setUIListener(IListenForUIChanges uiListener) {
		this.uiListener = uiListener;
		handler.setUIListener(uiListener);
	}
	
	

}
