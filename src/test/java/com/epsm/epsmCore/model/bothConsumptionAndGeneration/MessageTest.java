package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MessageTest {
	private final int POWER_OBJECT_ID = 0;
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.MIN;
	private final LocalDateTime REAL_TIMESTAMP = LocalDateTime.MIN;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionInConstructorIfRealTimeStampIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Message constructor: realTimeStamp can't be null.");
	    
	    new MessageImpl(POWER_OBJECT_ID, null, SIMULATION_TIMESTAMP);
	}
	
	@Test
	public void exceptionInConstructorIfSimulationTimeStamppIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Message constructor: simulationTimeStamp can't be null.");
	    
	    new MessageImpl(POWER_OBJECT_ID, REAL_TIMESTAMP, null);
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
