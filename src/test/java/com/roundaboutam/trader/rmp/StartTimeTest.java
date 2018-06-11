package com.roundaboutam.trader.rmp;

import junit.framework.TestCase;


public class StartTimeTest extends TestCase {

	StartTime testStartTime = StartTime.parse("20170324-16:56:01:010");
		
	public void testStartTime() {		
		assertEquals(testStartTime.toString(), "20170324-16:56:01:010");
	}

	public void testStartTimeRmpTags() {
		assertEquals(testStartTime.getRmpTag(), StartTime.RMPFieldID + "=20170324-16:56:01:010");
	}

}

