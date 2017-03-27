package com.roundaboutam.trader.rmp;

import junit.framework.TestCase;


public class TimeStampTest extends TestCase {

	TimeStamp testTimeStamp = TimeStamp.parse("20170324-17:05:05:010");
		
	public void testTimeStamp() {		
		assertEquals(testTimeStamp.toString(), "20170324-17:05:05:010");
	}

	public void testTimeStampFixTags() {
		assertEquals(testTimeStamp.getRmpTag(), TimeStamp.RMPFieldID + "=20170324-17:05:05:010");
	}

}

