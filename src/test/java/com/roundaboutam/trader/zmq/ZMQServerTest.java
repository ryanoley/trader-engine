package com.roundaboutam.trader.zmq;

import org.zeromq.ZMQ;

import junit.framework.TestCase;


public class ZMQServerTest extends TestCase {

	private ZMQServer testServer;
	
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    	testServer = new ZMQServer(5556);
    }

	public void testProcessFormattedRequest() {
		String connectString = "1=RMP|3=ORC|4=PYSENDER|5=TRADERENGINE";
		String basketString = "1=RMP|2=20170427-08:49:47|3=NB|4=PYSENDER|5=TRADERENGINE|6=ErnPeadFIX";
		String orderString = "1=RMP|2=20170427-08:49:47:181000|3=NO|4=PYSENDER|5=TRADERENGINE|6=ErnPeadFIX|7=ZEUS|9=SS|10=39251|11=V";

		String connectResult = testServer.processFormattedRequest(connectString);
		String basketResult = testServer.processFormattedRequest(basketString);
		String orderResult = testServer.processFormattedRequest(orderString);

		assertEquals(connectResult, "1=RMP|3=ORC|4=TRADERENGINE|5=PYSENDER");
		assertEquals(basketResult, "ACK");
		assertEquals(orderResult, "ACK");
	}

	
	public void testZMQConnect() {
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket requester = context.socket(ZMQ.REQ);
		requester.connect("tcp://localhost:5556");

		requester.send("TEST");
		String testReqRepStr = testServer.zmqRecieve();
		assertEquals(testReqRepStr, "TEST");
		
		testServer.zmqSend("TEST2");
		String testRepReqStr = new String(requester.recv(0));
		assertEquals(testRepReqStr, "TEST2");
	}

    @Override
    protected void tearDown() throws Exception {
    	testServer.closeZMQSocket();
    	super.tearDown();
    }

}

