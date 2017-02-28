package fix;

import java.util.HashMap;
import java.util.Map;


	
public class ExecutionType {

	static private final Map<String, ExecutionType> known = new HashMap<>();
	
	// Trader Engine Types
    static public final ExecutionType NEW = new ExecutionType("New");
    static public final ExecutionType PARTIAL_FILL = new ExecutionType("PartialFill");
    static public final ExecutionType FILL = new ExecutionType("Filled");
    static public final ExecutionType DONE_FOR_DAY = new ExecutionType("DoneForDay");
    static public final ExecutionType CANCELED = new ExecutionType("Canceled");
    static public final ExecutionType REPLACE = new ExecutionType("Replaced");
    static public final ExecutionType STOPPED = new ExecutionType("Stopped");
    static public final ExecutionType SUSPENDED = new ExecutionType("Suspended");
    static public final ExecutionType REJECTED = new ExecutionType("Rejected");
    static public final ExecutionType PENDING_CANCEL = new ExecutionType("PendingCancel");
    static public final ExecutionType PENDING_REPLACE = new ExecutionType("PendingReplace");
    static public final ExecutionType PENDING_NEW = new ExecutionType("PendingNew");
    static public final ExecutionType CLEARNING_HOLD = new ExecutionType("ClearingHold");

    static private final ExecutionType[] array = { NEW, PARTIAL_FILL, FILL, DONE_FOR_DAY, CANCELED,
    		REPLACE, STOPPED, SUSPENDED, REJECTED, PENDING_CANCEL, PENDING_REPLACE, PENDING_NEW, CLEARNING_HOLD};

	private final String name;

    private ExecutionType(String name) {
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

    public static ExecutionType parse(String type) throws IllegalArgumentException {
    	ExecutionType result = known.get(type);
        if (result == null) {
            throw new IllegalArgumentException
            ("ExecutionType: " + type + " is unknown.");
        }
        return result;
    }

}
