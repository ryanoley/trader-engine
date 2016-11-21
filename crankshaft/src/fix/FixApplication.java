package fix;

import java.util.Iterator;

import quickfix.Application;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.SocketInitiator;
import quickfix.UnsupportedMessageType;
import quickfix.field.MsgSeqNum;
import ui.IListenForUIChanges;

public class FixApplication implements Application {
	

	MessageHandler msgHandler = new MessageHandler();
	SocketInitiator initiator = null;
	
	String defaultSettingsFileName = ".\\settings.txt";
	
	private static FixApplication app = null;
	
	public static FixApplication getFixApplication() {
		if (app == null)
			app = new FixApplication();
		return app;
	}
	
	private FixApplication() {
	}
	
	
	public void connectToServer(IListenForUIChanges uiListener) {
		msgHandler.setUIListener(uiListener);
		connectToServer(defaultSettingsFileName);
	}
	
	public void connectToServer(String settingsFileName) {
		try {
			SessionSettings settings = new SessionSettings(settingsFileName);

			initiator = new SocketInitiator(this, new FileStoreFactory(settings), settings, new FileLogFactory(settings), new DefaultMessageFactory());

			initiator.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean disconnectFromServer() {
		if (initiator != null)
			initiator.stop();
		
		return true;
	}


	@Override
	public void fromAdmin(Message arg0, SessionID arg1) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		// correct for non-sync'd sequence numbers
		try {
			String msg = arg0.toString();
			int index = msg.indexOf("expecting ");
			if (index >= 0) {
				String[] subStr = msg.substring(index).split(" ");
				if (subStr.length < 2)
					return;
				int num = Integer.parseInt(subStr[1]);	
				Session.lookupSession(arg1).setNextSenderMsgSeqNum(num-1);	// since the error results in a request logoff which goes nowhere
				
				System.err.println("\n\n\nRESET SEQUENCE NUMBERS.\n");
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	// MAIN method for getting updates in from the server
	@Override
	public void fromApp(Message message, SessionID arg1) throws FieldNotFound,
			IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		System.out.println(message);
		msgHandler.crack(message, arg1);
	}
	

	@Override
	public void onCreate(SessionID arg0) {
		System.out.println("OnCreate : "+arg0);		
	}

	@Override
	public void onLogon(SessionID arg0) {
		msgHandler.notifySessionUpdate(arg0);
		TradeSender.getTradeCreator().sessionID = arg0;
	}

	@Override
	public void onLogout(SessionID arg0) {
		System.out.println("OnLogout : "+arg0);
		msgHandler.notifyDisconnect();
		TradeSender.getTradeCreator().sessionID = null;
	}

	@Override
	public void toAdmin(Message arg0, SessionID arg1) {		
	}

	@Override
	public void toApp(Message arg0, SessionID arg1) throws DoNotSend {		
	}

}
