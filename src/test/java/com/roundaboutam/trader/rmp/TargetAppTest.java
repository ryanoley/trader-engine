package com.roundaboutam.trader.rmp;

import junit.framework.TestCase;


public class TargetAppTest extends TestCase {

	TargetApp testTargetApp = TargetApp.parse("TRADERENGINE");
		
	public void testTargetApp() {		
		assertEquals(testTargetApp.toString(), "TRADERENGINE");
	}

	public void testTargetAppFixTags() {
		assertEquals(testTargetApp.getRmpTag(), TargetApp.RMPFieldID + "=TRADERENGINE");
	}

}

