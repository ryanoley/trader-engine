package fix;

import java.util.HashMap;
import java.util.Map;


	
public class MessageType {

	static private final Map<String, MessageType> known = new HashMap<>();

	// Trader Engine Types
    static public final MessageType EXECUTION_REPORT = new MessageType("ExecutionReport");
    static public final MessageType ORDER_CANCEL_REJECT = new MessageType("Reject");
    static public final MessageType NEW_ORDER = new MessageType("NewOrder");
    static public final MessageType REPLACE_ORDER = new MessageType("ReplaceOrder");
    static public final MessageType CANCEL_ORDER = new MessageType("CancelOrder");
    static public final MessageType LOGON = new MessageType("Logon");
    static public final MessageType HEARTBEAT = new MessageType("Heartbeat");
    static public final MessageType SEQUENCE_RESET = new MessageType("SequenceReset");
    static public final MessageType RESEND_REQUEST = new MessageType("ResendRequest");
    static public final MessageType TEST_REQUEST = new MessageType("TestRequest");


    static private final MessageType[] array = { EXECUTION_REPORT, ORDER_CANCEL_REJECT, NEW_ORDER,
    		REPLACE_ORDER, CANCEL_ORDER, LOGON, HEARTBEAT, SEQUENCE_RESET, RESEND_REQUEST, TEST_REQUEST};

	private final String name;

    private MessageType(String name) {
        this.name = name;
        synchronized (ExecutionType.class) {
            known.put(name, this);
        }
    }

    public String getName() {
        return name;
    }
    
    public String toString() {
        return name;
    }

    static public Object[] toArray() {
        return array;
    }

    public static MessageType parse(String type) throws IllegalArgumentException {
    	MessageType result = known.get(type);
        if (result == null) {
            throw new IllegalArgumentException
            ("MessageType: " + type + " is unknown.");
        }
        return result;
    }

}
