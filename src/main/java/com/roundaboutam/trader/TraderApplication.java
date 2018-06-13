package com.roundaboutam.trader;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import com.roundaboutam.trader.order.Order;
import com.roundaboutam.trader.order.OrderBasket;
import com.roundaboutam.trader.order.OrderBasketBook;
import com.roundaboutam.trader.order.OrderBook;
import com.roundaboutam.trader.order.CancelOrder;
import com.roundaboutam.trader.order.ReplaceOrder;
import com.roundaboutam.trader.ramfix.FIXOrder;
import com.roundaboutam.trader.rmp.MessageClass;
import com.roundaboutam.trader.rmp.Parser.ParsedRMPObject;
import com.roundaboutam.trader.rmp.Parser;
import com.roundaboutam.trader.zmq.ZMQServer;
import com.roundaboutam.trader.execution.ExecutionBook;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FieldConvertError;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.UnsupportedMessageType;
import quickfix.field.BeginString;
import quickfix.field.BusinessRejectReason;
import quickfix.field.ClOrdID;
import quickfix.field.ExecID;
import quickfix.field.MsgSeqNum;
import quickfix.field.MsgType;
import quickfix.field.OrdStatus;
import quickfix.field.RefMsgType;
import quickfix.field.RefSeqNum;
import quickfix.field.SenderCompID;
import quickfix.field.TargetCompID;
import quickfix.field.Text;

public class TraderApplication implements Application {

    private final ObservableOrder observableOrder = new ObservableOrder();
    private final ObservableMessage observableMessage = new ObservableMessage();
    private final ObservableLogon observableLogon = new ObservableLogon();
    private final ObservableBasket observableBasket = new ObservableBasket();
    private final SessionSettings settings;
    private final OrderBook orderBook;
	private final OrderBasketBook orderBasketBook;
	private final ExecutionBook executionBook;
	private ZMQServer zmqServer = null;
	private Boolean isProd = null;

    private final DefaultMessageFactory messageFactory = new DefaultMessageFactory();

    private SessionID sessionID = null;
	static private final HashMap<SessionID, HashSet<ExecID>> execIDs = 
			new HashMap<SessionID, HashSet<ExecID>>();

	
	public TraderApplication(SessionSettings settings) throws ConfigError, FieldConvertError {
		this.settings = settings;
		setLaunchEnv();
		orderBook = new OrderBook();
		executionBook = new ExecutionBook(settings.getString("CustomLogPath"));
		orderBasketBook = new OrderBasketBook();
	}
	
	private void setLaunchEnv() throws ConfigError, FieldConvertError {
		String launchEnvString = this.settings.getString("LaunchEnvironment");
		if (launchEnvString.equals("prod"))
			this.isProd = true;
		else
			this.isProd = false;
	}
	
	
	/*
	 *  CORE FUNCTIONALITY
	 */
    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound,
    	IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
    	try {
    		SwingUtilities.invokeLater(new MessageProcessor(message, sessionID));
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    public class MessageProcessor implements Runnable {

    	private final Message message;
        private final SessionID sessionID;

        public MessageProcessor(Message message, SessionID sessionID) {
            this.message = message;
            this.sessionID = sessionID;
        }

        public void run() {
            try {
                MsgType msgType = new MsgType();
                if (message.getHeader().getField(msgType).valueEquals("8")) {
                	executionReport(message, sessionID);
                } else if (message.getHeader().getField(msgType).valueEquals("9")) {
                	orderCancelReject(message, sessionID);
                } else {
                	sendBusinessReject(message, BusinessRejectReason.UNSUPPORTED_MESSAGE_TYPE,
                			"Unsupported Message Type");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            observableMessage.update(message);
        }
    }

    private void sendBusinessReject(Message message, int rejectReason, String rejectText)
            throws FieldNotFound, SessionNotFound {
        Message reply = createMessage(message, MsgType.BUSINESS_MESSAGE_REJECT);
        reverseRoute(message, reply);
        String refSeqNum = message.getHeader().getString(MsgSeqNum.FIELD);
        reply.setString(RefSeqNum.FIELD, refSeqNum);
        reply.setString(RefMsgType.FIELD, message.getHeader().getString(MsgType.FIELD));
        reply.setInt(BusinessRejectReason.FIELD, rejectReason);
        reply.setString(Text.FIELD, rejectText);
        Session.sendToTarget(reply);
        System.out.println("sendBusinessReject: " + reply.toString());  // Debugging
    }

    private Message createMessage(Message message, String msgType) throws FieldNotFound {
        return messageFactory.create(message.getHeader().getString(BeginString.FIELD), msgType);
    }

    private void reverseRoute(Message message, Message reply) throws FieldNotFound {
        reply.getHeader().setString(SenderCompID.FIELD,
                message.getHeader().getString(TargetCompID.FIELD));
        reply.getHeader().setString(TargetCompID.FIELD,
                message.getHeader().getString(SenderCompID.FIELD));
    }

    private void executionReport(Message message, SessionID sessionID) throws FieldNotFound {

        ExecID execID = (ExecID) message.getField(new ExecID());
        if (executionIsProcessed(execID, sessionID)) { return; }
        
        MessageContainer messageContainer = new MessageContainer(message);
        String orderID = messageContainer.getClOrdID();
        String orderMessage = messageContainer.getText();
        char ordStatus = message.getChar(OrdStatus.FIELD);

        if (ordStatus == OrdStatus.REJECTED) {
        	System.out.println("TraderApplication.executionReport - Rejected: " + orderMessage);
        	orderBook.orderRejected(orderID);
        }
        else {
        	orderBook.processExecutionReport(messageContainer, sessionID);
        }

        Order order = orderBook.getOrder(orderID);
        observableOrder.update(order);
        executionBook.processExecutionReport(messageContainer, order);

        if (order.getOrderBasketID() != null){
        	OrderBasket ob = orderBasketBook.getBasket(order.getOrderBasketID());
        	observableBasket.update(ob);
        }
    }

    private void orderCancelReject(Message message, SessionID sessionID) throws FieldNotFound {
    	MessageContainer messageContainer = new MessageContainer(message);
    	orderBook.processOrderCancelReject(message.getString(ClOrdID.FIELD));
    	executionBook.processOrderCancelReject(messageContainer, orderBook.getOrder(messageContainer.getOrigClOrdID()));
    }

    private boolean executionIsProcessed(ExecID execID, SessionID sessionID) {
        HashSet<ExecID> set = execIDs.get(sessionID);
        if (set == null) {
            set = new HashSet<ExecID>();
            set.add(execID);
            execIDs.put(sessionID, set);
            return false;
        } else {
            if (set.contains(execID))
                return true;
            set.add(execID);
            return false;
        }
    }

    private void sendToBroker(Message message, SessionID sessionID) {        
    	try {
        	Session.sendToTarget(message, sessionID);
        	observableMessage.update(message);
        } catch (SessionNotFound e) {
        	System.out.println(e);
        }
    }
	/*
	 *  ORDER AND ORDER BASKET FUNTIONS
	 */
    public void sendNewOrder(Order order) {
    	orderBook.addOrder(order);
    	sendToBroker(FIXOrder.formatNewOrder(order, this.isProd), order.getSessionID());
    }

    public void sendCancelOrder(CancelOrder cancelOrder) {
    	orderBook.addCancelOrder(cancelOrder);
    	sendToBroker(FIXOrder.formatCancelOrder(cancelOrder), cancelOrder.getSessionID());
    }

    public void sendReplaceOrder(ReplaceOrder replaceOrder) {
    	orderBook.addReplaceOrder(replaceOrder);
        sendToBroker(FIXOrder.formatReplaceOrder(replaceOrder), replaceOrder.getSessionID());
    }

    public void cancelAllOpenOrders() {
    	for (Order o : orderBook.getAllOpenOrders()) {
            sendCancelOrder(new CancelOrder(o));
    	}
    }

    public void sendBasket(OrderBasket orderBasket) {
    	for (Order order : orderBasket.getAllOrders()) {
	    	sendNewOrder(order);
    	}
		orderBasket.setStaged(false);
		orderBasket.setLive(true);
    }

    public void cancelBasket(OrderBasket orderBasket) {
    	for (Order order : orderBasket.getAllOpenOrders()) {
            sendCancelOrder(new CancelOrder(order));
    	}
		orderBasket.setStaged(false);
		orderBasket.setLive(false);
    }

    public void deleteBasket(OrderBasket orderBasket) {    	
		orderBasketBook.deleteBasket(orderBasket);
		observableBasket.update(orderBasket);
    }
    
	public void writeOrderBookToCSV(String outputLocation) {
		String orderBookCSV = orderBook.getOrderBookCSV();
		PrintWriter out;
		try {
			out = new PrintWriter(outputLocation);
			out.println(orderBookCSV);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/*
	 *  FIX SESSION 
	 */
    public void onCreate(SessionID sessionID) { }

    public void onLogon(SessionID sessionID) {
    	setSessionID(sessionID);
        observableLogon.logon(sessionID);
		executionBook.setExecutionLogs(sessionID);
    }

    public void onLogout(SessionID sessionID) {
    	setSessionID(null);
    	observableLogon.logoff(sessionID);
    	executionBook.closeExecutionLogs(sessionID);
    }

    public void toAdmin(Message message, SessionID sessionID) { }

    public void toApp(Message message, SessionID sessionID) throws DoNotSend { }

    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound,
    	IncorrectDataFormat, IncorrectTagValue, RejectLogon {
    	System.out.println(message.toString());
    	observableMessage.update(message);
    }

    public SessionID getSessionID() {
    	return sessionID;
    }

    private void setSessionID(SessionID sessionID) {
    	this.sessionID = sessionID;
    }
   
    public SessionSettings getSessionSettings(){
    	return settings;
    }

    private void assertSession() {
    	if (sessionID == null) {
        	throw new IllegalStateException("Active Session required. No session exists.");
    	}
    }
	/*
	 *  OBSERVABLES 
	 */    
     public void addLogonObserver(Observer observer) {
        observableLogon.addObserver(observer);
    }

    public void deleteLogonObserver(Observer observer) {
        observableLogon.deleteObserver(observer);
    }

    public void addOrderObserver(Observer observer) {
        observableOrder.addObserver(observer);
    }

    public void deleteOrderObserver(Observer observer) {
        observableOrder.deleteObserver(observer);
    }

    public void addMessageObserver(Observer observer) {
        observableMessage.addObserver(observer);
    }

    public void deleteMessageObserver(Observer observer) {
    	observableMessage.deleteObserver(observer);
    }

    public void addOrderBasketObserver(Observer observer) {
        observableBasket.addObserver(observer);
    }

    public void deleteOrderBasketObserver(Observer observer) {
    	observableBasket.deleteObserver(observer);
    }

    private static class ObservableMessage extends Observable {
        public void update(Message message) {
            setChanged();
            notifyObservers(message);
            clearChanged();
        }
    }

    private static class ObservableOrder extends Observable {
        public void update(Order order) {
            setChanged();
            notifyObservers(order);
            clearChanged();
        }
    }

    private static class ObservableLogon extends Observable {
        private final HashSet<SessionID> set = new HashSet<SessionID>();

        public void logon(SessionID sessionID) {
            set.add(sessionID);
            setChanged();
            notifyObservers(new LogonEvent(sessionID, true));
            clearChanged();
        }

        public void logoff(SessionID sessionID) {
            set.remove(sessionID);
            setChanged();
            notifyObservers(new LogonEvent(sessionID, false));
            clearChanged();
        }
    }

    private static class ObservableBasket extends Observable {
        public void update(OrderBasket orderBasket) {
            setChanged();
            notifyObservers(orderBasket);
            clearChanged();
        }
    }
	/*
	 *  ZMQ SERVER 
	 */   
    public void startZMQServer(Integer port) {
    	if (zmqServer == null) {
        	try {
				zmqServer = new ZMQServer(this, port, settings.getString("CustomLogPath"));
			} catch (ConfigError | FieldConvertError e) {
				e.printStackTrace();
			}
        	Thread zmqThread = new Thread(zmqServer);
        	zmqThread.start();
    	}
    }

    public void stopZMQServer() {
    	if (zmqServer != null) {
    		zmqServer.setShutdown(true);
    		zmqServer = null;
    	}
    }
    
    public Boolean getZMQServerStatus() {
    	if (zmqServer == null)
    		return false;
    	else 
    		return zmqServer.isStarted();
    }
    
    public void toZMQLog(String logString, char dir) {
    	if (zmqServer != null) {
    		zmqServer.toLog(logString, dir);
    	}
    }
	/*
	 *  RMP HANDLING
	 */ 
    public void fromRMP(String rmpMessage) {
    	ParsedRMPObject parsedRMPObject = Parser.parseMessage(rmpMessage);
    	MessageClass messageClass = parsedRMPObject.messageClass;

    	if (messageClass == MessageClass.NEW_BASKET) {
    		newRMPOrderBasket(parsedRMPObject);
    	}
    	else if (messageClass == MessageClass.NEW_ORDER) {
    		newRMPOrder(parsedRMPObject);
    	}
    }

    private void newRMPOrderBasket(ParsedRMPObject parsedRMPObject) {
		OrderBasket orderBasket = (OrderBasket) parsedRMPObject.object;
		String basketName = orderBasket.getBasketName();

		if (orderBasketBook.basketExists(basketName)) {
			toZMQLog(basketName + " already exists", ZMQServer.Parsing);
		} else {
			orderBasketBook.addBasket(orderBasket);
			observableBasket.update(orderBasket);
			toZMQLog("New basket: " + basketName, ZMQServer.Parsing);
		}
    }

    private void newRMPOrder (ParsedRMPObject parsedRMPObject) {
		Order order = (Order) parsedRMPObject.object;
		assertSession();
		//SessionID sessionID = new SessionID("FIX.4.2:ROUNDTEST02->REALTICK2:RYAN"); // TODO FOR TESTING ONLY
		order.setSessionID(sessionID);
		String basketName = order.getOrderBasketName(); 
		if (basketName != null) {
			OrderBasket orderBasket = orderBasketBook.getBasketbyName(basketName);
			if (orderBasket != null) {
				if (!orderBasket.isStaged())
					toZMQLog("Basket not staged: " + basketName + " symbol:" + order.getSymbol(), ZMQServer.Parsing);
				else {
					orderBasket.addOrder(order);
					observableBasket.update(orderBasket);
					toZMQLog("New order " + order.getSymbol(), ZMQServer.Parsing);
				}	
			}
			else
				toZMQLog("Unknown basket: " + basketName + " symbol:" + order.getSymbol(), ZMQServer.Parsing);
		}
		else {
			toZMQLog("No basket info:" + order.getSymbol(), ZMQServer.Parsing);
		}
    }

    public void testRMP() {
    	// FOR TESTING - NOT CALLED UNLESS EXPLICITLY CHANGED
    	String newBasketString = "1=RMP|2=20170313-13:54:44|3=NB|4=TESTSENDER|5=TRADERENGINE|6=ParseBasket";
		fromRMP(newBasketString);
		String newOrderString = "1=RMP|2=20170313-14:54:44|3=NO|4=TESTSENDER|5=TRADERENGINE|6=ParseBasket|7=IBM|9=BY|10=100|11=MOC";
		fromRMP(newOrderString);
		String newLimitOrderString = "1=RMP|2=20170313-15:54:44|3=NO|4=TESTSENDER|5=TRADERENGINE|6=ParseBasket|7=GLD|9=SL|10=175|11=LOC|12=117.05";
		fromRMP(newLimitOrderString);
    }



}
