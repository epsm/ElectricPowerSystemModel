package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;

public class MessageTest {
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionInConstructorIfRealTimeStampIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("Message constructor: realTimeStamp can't be null.");
	    
	    new MessageImpl(0, null, LocalTime.MIN);
	}
	
	@Test
	public void exceptionInConstructorIfSimulationTimeStamppIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("Message constructor: simulationTimeStamp can't be null.");
	    
	    new MessageImpl(0, LocalDateTime.MIN, null);
	}
	
	private class MessageImpl extends Message{
		public MessageImpl(long powerObjectId, LocalDateTime realTimeStamp, LocalTime simulationTimeStamp) {
			super(powerObjectId, realTimeStamp, simulationTimeStamp);
		}

		@Override
		public String toString() {
			return null;
		}
	}
}
