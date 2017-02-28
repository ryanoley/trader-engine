package fix;

import java.util.HashMap;
import java.util.Map;


public class OrderTIF {

	static private final Map<String, OrderTIF> known = new HashMap<>();
	// Trader Engine Types
    static public final OrderTIF DAY = new OrderTIF("Day");
    static public final OrderTIF IOC = new OrderTIF("Immediate Or Cancel");
    static public final OrderTIF AT_OPEN = new OrderTIF("At Open");
    static public final OrderTIF AT_CLOSE = new OrderTIF("At Close");
    static public final OrderTIF GTC = new OrderTIF("Good Till Cancelled");

    static private final OrderTIF[] array = { DAY, IOC, AT_OPEN, AT_CLOSE, GTC };

    private final String name;

    private OrderTIF(String name) {
        this.name = name;
        synchronized (OrderTIF.class) {
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

    public static OrderTIF parse(String type) throws IllegalArgumentException {
        OrderTIF result = known.get(type);
        if (result == null) {
            throw new IllegalArgumentException
            ("OrderTIF: " + type + " is unknown.");
        }
        return result;
    }

}
