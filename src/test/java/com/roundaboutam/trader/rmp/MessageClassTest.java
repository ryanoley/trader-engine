package com.roundaboutam.trader.rmp;

import com.roundaboutam.trader.rmp.MessageClass;

import junit.framework.TestCase;


public class MessageClassTest extends TestCase {

	public void testMessageClass() {
		assertEquals(MessageClass.OPEN_RMP_CONNECTION.toString(), "ORC");
		assertEquals(MessageClass.CLOSE_RMP_CONNECTION.toString(), "CRC");
		assertEquals(MessageClass.NEW_ORDER.toString(), "NO");
		assertEquals(MessageClass.NEW_BASKET.toString(), "NB");
		assertEquals(MessageClass.TO_CONSOLE.toString(), "TC");
		assertEquals(MessageClass.BAD_RMP_SYNTAX.toString(), "X");

		assertEquals(MessageClass.toArray().length, 6);
	}

	public void testMessageClassRmpTags() {
		assertEquals(MessageClass.OPEN_RMP_CONNECTION.getRmpTag(), MessageClass.RMPFieldID + "=ORC");
		assertEquals(MessageClass.CLOSE_RMP_CONNECTION.getRmpTag(), MessageClass.RMPFieldID + "=CRC");
		assertEquals(MessageClass.NEW_ORDER.getRmpTag(), MessageClass.RMPFieldID + "=NO");
		assertEquals(MessageClass.NEW_BASKET.getRmpTag(), MessageClass.RMPFieldID + "=NB");
		assertEquals(MessageClass.TO_CONSOLE.getRmpTag(), MessageClass.RMPFieldID + "=TC");
		assertEquals(MessageClass.BAD_RMP_SYNTAX.getRmpTag(), MessageClass.RMPFieldID + "=X");
	}

}
