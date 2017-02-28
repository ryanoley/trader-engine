package ramfix;

import java.util.HashMap;
import java.util.Map;


	
public class OrderStatus {

	static private final Map<String, OrderStatus> known = new HashMap<>();

	// Trader Engine Types
    static public final OrderStatus NEW = new OrderStatus("New");
    static public final OrderStatus PARTIAL_FILL = new OrderStatus("PartialFill");
    static public final OrderStatus FILL = new OrderStatus("Filled");
    static public final OrderStatus DONE_FOR_DAY = new OrderStatus("DoneForDay");
    static public final OrderStatus CANCELED = new OrderStatus("Canceled");
    static public final OrderStatus REPLACE = new OrderStatus("Replaced");
    static public final OrderStatus STOPPED = new OrderStatus("Stopped");
    static public final OrderStatus SUSPENDED = new OrderStatus("Suspended");
    static public final OrderStatus REJECTED = new OrderStatus("Rejected");
    static public final OrderStatus PENDING_CANCEL = new OrderStatus("PendingCancel");
    static public final OrderStatus PENDING_REPLACE = new OrderStatus("PendingReplace");
    static public final OrderStatus PENDING_NEW = new OrderStatus("PendingNew");

    static private final OrderStatus[] array = { NEW, PARTIAL_FILL, FILL, DONE_FOR_DAY, CANCELED,
    		REPLACE, STOPPED, SUSPENDED, REJECTED, PENDING_CANCEL, PENDING_REPLACE, PENDING_NEW};

	private final String name;

    private OrderStatus(String name) {
        this.name = name;
        synchronized (OrderStatus.class) {
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

    public static OrderStatus parse(String type) throws IllegalArgumentException {
    	OrderStatus result = known.get(type);
        if (result == null) {
            throw new IllegalArgumentException
            ("OrderStatus: " + type + " is unknown.");
        }
        return result;
    }

}
