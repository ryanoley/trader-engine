package main;

import java.util.ArrayList;
import java.util.HashMap;

import quickfix.Message;

public class OrdersContainer {

	public static HashMap<String, ArrayList<Message>> ordIDtoMessagesOpen = new HashMap<>();
	public static HashMap<String, ArrayList<Message>> ordIDtoMessagesClosed = new HashMap<>();
}
