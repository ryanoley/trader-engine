package com.roundaboutam.trader.rmp;

import com.roundaboutam.trader.rmp.Protocol;

import junit.framework.TestCase;


public class ProtocolTest extends TestCase {

	public void testProtocol() {		
		assertEquals(Protocol.RMP.toString(), "RMP");
		assertEquals(Protocol.toArray().length, 1);
	}

	public void testProtocolRMPTags() {
		assertEquals(Protocol.RMP.getRmpTag(), Protocol.RMPFieldID + "=RMP");
	}

}
