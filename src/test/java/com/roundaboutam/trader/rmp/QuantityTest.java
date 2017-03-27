package com.roundaboutam.trader.rmp;

import junit.framework.TestCase;

public class QuantityTest extends TestCase {

	Quantity testQuantity = Quantity.parse(50);
	Quantity testQuantityStr = Quantity.parse("200");
		
	public void testQuantity() {		
		assertEquals(testQuantity.toString(), "50");
		assertEquals(testQuantityStr.toString(), "200");

	}

	public void testQuantityFixTags() {
		assertEquals(testQuantity.getRmpTag(), Quantity.RMPFieldID + "=50");
		assertEquals(testQuantityStr.getRmpTag(), Quantity.RMPFieldID + "=200");
	}

}