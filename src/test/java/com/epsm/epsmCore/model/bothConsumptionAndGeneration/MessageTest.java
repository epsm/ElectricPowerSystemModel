package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MessageTest {
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionInConstructorIfRealTimeStampIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Message constructor: realTimeStamp can't be null.");
	    
	    new MessageImpl(0, null, LocalDateTime.MIN);
	}
	
	@Test
	public void exceptionInConstructorIfSimulationTimeStamppIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Message constructor: simulationTimeStamp can't be null.");
	    
	    new MessageImpl(0, LocalDateTime.MIN, null);
	}
	
	private class MessageImpl extends Message{
		public MessageImpl(long powerObjectId, LocalDateTime realTimeStamp,
				LocalDateTime simulationTimeStamp) {
			
			super(powerObjectId, realTimeStamp, simulationTimeStamp);
		}

		@Override
		public String toString() {
			return null;
		}
	}
}
