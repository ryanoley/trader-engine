package com.roundaboutam.trader.order;

import junit.framework.TestCase;

public class IdGeneratorTest extends TestCase {

	public void testIdGenerator() {
		
		String id1 = IdGenerator.makeID();
		String id2 = IdGenerator.makeID();
		
		assertNotSame(id1, id2);

	}
}
