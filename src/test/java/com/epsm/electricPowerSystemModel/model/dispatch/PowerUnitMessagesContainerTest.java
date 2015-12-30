package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PowerUnitMessagesContainerTest {
	private PowerUnitMessagesContainer container;
	private PowerUnitMessageImpl message_1;
	private PowerUnitMessageImpl message_2;
	
	@Before
	public void initialize(){
		container = new PowerUnitsMessageContainerImpl(1, LocalDateTime.MIN, LocalTime.MIN, 2);
		message_1 = new PowerUnitMessageImpl(1);
		message_2 = new PowerUnitMessageImpl(2);
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionInGetQuantityOfMessagesMethodIfInContainerDifferentQuantityOfMessagesThanExpected(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerUnitsMessageContainerImpl keeps 1 message(s) but expected 2 "
	    		+ "message(s).");
	    
	    addOneMessageToContainer();
	    container.getQuantityOfMessages();
	}
	
	private void addOneMessageToContainer(){
		container.addMessage(message_1);
	}
	
	@Test
	public void exceptionInGetMessagesNumbersMethodIfInContainerDifferentQuantityOfMessagesThanExpected(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerUnitsMessageContainerImpl keeps 1 message(s) but expected 2 "
	    		+ "message(s).");
	    
	    addOneMessageToContainer();
	    container.getMessagesNumbers();
	}
	
	@Test
	public void exceptionInGetMessageMethodIfInContainerDifferentQuantityOfMessagesThanExpected(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerUnitsMessageContainerImpl keeps 1 message(s) but expected 2 "
	    		+ "message(s).");
	    
	    addOneMessageToContainer();
	    container.getMessage(1);
	}
	
	@Test
	public void exceptionInGetMessageMesthodIfRequestedMessageDoesNotExist(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerUnitsMessageContainerImpl: there isn't message with number 3,"
	    		+ " presents only messages with numbers: [1, 2]");
	    
	    addTwoMessagesToContainer();
	    container.getMessage(3);
	}
	
	private void  addTwoMessagesToContainer(){
		container.addMessage(message_1);
		container.addMessage(message_2);
	}
	
	@Test
	public void exceptionIfTryToAddNullMessage(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerUnitsMessageContainerImpl addMessage(...): powerUnitMessage "
	    		+ "can't be null.");
	    
	    container.addMessage(null);
	}
	
	@Test
	public void exceptionIfTryToAddMessageWithTheSameNumberAsExist(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerUnitsMessageContainerImpl already contain message with this number.");
	    
	    addTwoMessagesWithTheSameNumbersToContainer();
	}
	
	private void addTwoMessagesWithTheSameNumbersToContainer(){
		message_1 = new PowerUnitMessageImpl(1);
		message_2 = new PowerUnitMessageImpl(1);
		container.addMessage(message_1);
		container.addMessage(message_2);
	}
	
	private class PowerUnitsMessageContainerImpl extends PowerUnitMessagesContainer{
		PowerUnitsMessageContainerImpl(long powerObjectId, LocalDateTime realTimeStamp, 
				LocalTime simulationTimeStamp, 	int quantityOfPowerUnits){
			
			super(powerObjectId, realTimeStamp, simulationTimeStamp, quantityOfPowerUnits);
		}

		@Override
		public String toString() {
			return null;
		}
	}
	
	private class PowerUnitMessageImpl extends PowerUnitMessage{
		public PowerUnitMessageImpl(int powerUnitNumber) {
			super(powerUnitNumber);
		}
	}
}
