package com.roundaboutam.trader.rmp;

import junit.framework.TestCase;

public class PriceLimitTest extends TestCase {

	PriceLimit testPriceLimit = PriceLimit.parse(199.99);
	PriceLimit testPriceLimitStr = PriceLimit.parse("200");
		
	public void testPriceLimit() {		
		assertEquals(testPriceLimit.toString(), "199.99");
		assertEquals(testPriceLimitStr.toString(), "200");
	}

	public void testPriceLimitFixTags() {
		assertEquals(testPriceLimit.getRmpTag(), PriceLimit.RMPFieldID + "=199.99");
		assertEquals(testPriceLimitStr.getRmpTag(), PriceLimit.RMPFieldID + "=200");
	}

}