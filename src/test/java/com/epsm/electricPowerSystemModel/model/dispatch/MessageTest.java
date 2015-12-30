package com.epsm.electricPowerSystemModel.model.dispatch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.generation.GenerationException;

public class MessageTest {
	private Message message;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void initialize(){
		message = mock(Message.class);
	}
	
	@Test
	public void exceptionInConstructorRealTimeStampIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("Message constructor: realTimeStamp can't be null.");
	    
	    new MessageImpl(0, null, LocalTime.MIN);
	}
	
	@Test
	public void exceptionInConstructorSimulationTimeStamppIsNull(){
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
