package com.roundaboutam.trader.execution;

import fix.ExecutionType;
import fix.FIXMessage;
import junit.framework.TestCase;
import quickfix.field.ExecType;



public class ExecutionTypeTest extends TestCase {

	public void testExecutionType() {		
    
		assertEquals(ExecutionType.NEW.toString(), "New");
		assertEquals(ExecutionType.PARTIAL_FILL.toString(), "PartialFill");
		assertEquals(ExecutionType.FILL.toString(), "Filled");
		assertEquals(ExecutionType.DONE_FOR_DAY.toString(), "DoneForDay");
		assertEquals(ExecutionType.CANCELED.toString(), "Canceled");
		assertEquals(ExecutionType.REPLACE.toString(), "Replaced");
		assertEquals(ExecutionType.STOPPED.toString(), "Stopped");
		assertEquals(ExecutionType.SUSPENDED.toString(), "Suspended");
		assertEquals(ExecutionType.REJECTED.toString(), "Rejected");
		assertEquals(ExecutionType.PENDING_CANCEL.toString(), "PendingCancel");
		assertEquals(ExecutionType.PENDING_REPLACE.toString(), "PendingReplace");
		assertEquals(ExecutionType.PENDING_NEW.toString(), "PendingNew");
		assertEquals(ExecutionType.CLEARNING_HOLD.toString(), "ClearingHold");

		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.NEW), new ExecType(ExecType.NEW));
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.PARTIAL_FILL), new ExecType(ExecType.PARTIAL_FILL));
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.FILL), new ExecType(ExecType.FILL));
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.DONE_FOR_DAY), new ExecType(ExecType.DONE_FOR_DAY));
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.CANCELED), new ExecType(ExecType.CANCELED));
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.REPLACE), new ExecType(ExecType.REPLACE));
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.STOPPED), new ExecType(ExecType.STOPPED));
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.SUSPENDED), new ExecType(ExecType.SUSPENDED));
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.REJECTED), new ExecType(ExecType.REJECTED));
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.PENDING_CANCEL), new ExecType(ExecType.PENDING_CANCEL));
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.PENDING_REPLACE), new ExecType(ExecType.PENDING_REPLACE));
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.PENDING_NEW), new ExecType(ExecType.PENDING_NEW));
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.CLEARNING_HOLD), new ExecType(ExecType.TRADE_IN_A_CLEARING_HOLD));
		
		assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.NEW)), ExecutionType.NEW);
		assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.PARTIAL_FILL)), ExecutionType.PARTIAL_FILL);
		assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.FILL)), ExecutionType.FILL);
		assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.DONE_FOR_DAY)), ExecutionType.DONE_FOR_DAY);
		assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.CANCELED)), ExecutionType.CANCELED);
		assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.REPLACE)), ExecutionType.REPLACE);
		assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.STOPPED)), ExecutionType.STOPPED);
		assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.SUSPENDED)), ExecutionType.SUSPENDED);
		assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.REJECTED)), ExecutionType.REJECTED);
		assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.PENDING_CANCEL)), ExecutionType.PENDING_CANCEL);
		assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.PENDING_REPLACE)), ExecutionType.PENDING_REPLACE);
		assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.PENDING_NEW)), ExecutionType.PENDING_NEW);
		assertEquals(FIXMessage.FIXExecTypeToExecutionType(new ExecType(ExecType.TRADE_IN_A_CLEARING_HOLD)), ExecutionType.CLEARNING_HOLD);

		assertEquals(ExecutionType.toArray().length, 13);
	}

	public void testExecutionTypeFixTags() {

		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.NEW).toString(), "150=0");
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.PARTIAL_FILL).toString(), "150=1");
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.FILL).toString(), "150=2");
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.DONE_FOR_DAY).toString(), "150=3");
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.CANCELED).toString(), "150=4");
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.REPLACE).toString(), "150=5");
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.STOPPED).toString(), "150=7");
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.SUSPENDED).toString(), "150=9");
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.REJECTED).toString(), "150=8");
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.PENDING_CANCEL).toString(), "150=6");
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.PENDING_REPLACE).toString(), "150=E");
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.PENDING_NEW).toString(), "150=A");
		assertEquals(FIXMessage.executionTypeToFIXExecType(ExecutionType.CLEARNING_HOLD).toString(), "150=J");
	}

}
