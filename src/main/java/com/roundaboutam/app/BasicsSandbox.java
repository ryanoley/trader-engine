package com.roundaboutam.app;

import quickfix.DefaultMessageFactory;
import quickfix.field.MsgType;

public class BasicsSandbox {

	private final static DefaultMessageFactory messageFactory = new DefaultMessageFactory();
			
	public static void main( String[] args ) {
		System.out.println(MsgType.TRADING_SESSION_STATUS);
		System.out.println(messageFactory.create("ASDF", MsgType.REJECT));
	}
}
