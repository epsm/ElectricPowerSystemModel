package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class PowerUnitMessagesContainer extends Message{
	private int expectedQuantityOfMessages;
	private Map<Integer, PowerUnitMessage> messages;
	
	public PowerUnitMessagesContainer(long powerObjectId, LocalDateTime realTimeStamp,
			LocalTime simulationTimeStamp, int quantityOfMessages) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
		messages = new HashMap<Integer, PowerUnitMessage>();
		expectedQuantityOfMessages = quantityOfMessages;
	}

	public final int getQuantityOfMessages(){
		throwExceptionIfQuantityOfMessagesNotAsDefinedInConstructor();
		return expectedQuantityOfMessages;
	}
	
	private void throwExceptionIfQuantityOfMessagesNotAsDefinedInConstructor(){
		if(isContainerKeepWrongPowerUnitQuantity()){
			throwWrongMessageQuantityException();
		}
	}
	
	private boolean isContainerKeepWrongPowerUnitQuantity(){
		return expectedQuantityOfMessages != messages.size();
	}
	
	private void throwWrongMessageQuantityException(){
		String message = String.format("%s keeps %d message(s) but expected %d message(s).",
				getNameOfThisClass(), messages.size(), expectedQuantityOfMessages);
		throw new DispatchingException(message);
	}
	
	private String getNameOfThisClass(){
		return this.getClass().getSimpleName();
	}
	
	public final Set<Integer> getMessagesNumbers(){
		throwExceptionIfQuantityOfMessagesNotAsDefinedInConstructor();
		return Collections.unmodifiableSet(messages.keySet());
	}
	
	public final PowerUnitMessage getMessage(int generatorNumber){
		throwExceptionIfQuantityOfMessagesNotAsDefinedInConstructor();
		throwExceptionIfRequestedMessageDoesNotExist(generatorNumber);
		return messages.get(generatorNumber);
	}
	
	private void throwExceptionIfRequestedMessageDoesNotExist(int generatorNumber){
		String message = String.format("%s: there isn't message with number %d, presents only messages "
				+ "with numbers: %s.",
				getNameOfThisClass(), generatorNumber, messages.keySet());
		
		System.out.println(message);
		throw new DispatchingException(message);
	}
	
	public final void addMessage(PowerUnitMessage powerUnitMessage){
		if(powerUnitMessage == null){
			String message = String.format("%s addMessage(...): powerUnitMessage can't be null.",
					getNameOfThisClass());
			throw new DispatchingException(message);
		}
		
		int powerUnitNumber = powerUnitMessage.getPowerUnitNumber();
		
		if(isMessageToAddAlreadyInContainer(powerUnitNumber)){
			String message = String.format("%s already contain message with this number.",
					getNameOfThisClass());
			throw new DispatchingException(message);
		}

		messages.put(powerUnitNumber, powerUnitMessage);
	}
	
	private boolean isMessageToAddAlreadyInContainer(int powerUnitNumber){
		return messages.containsKey(powerUnitNumber);
	}
}
