package com.epsm.electricPowerSystemModel.model.dispatch;

import com.epsm.electricPowerSystemModel.model.generalModel.PowerObject;

public class MessageFilter {
	private Class<? extends Message> expectedCommandMessageClass;
	private Class<? extends Message> expectedStateMessageClass;
	private Class<? extends Message> expectedParametersMessageClass;
	private Class<? extends PowerObject> object;
	
	public MessageFilter(Class<? extends PowerObject> objectClass) {
		if(objectClass == null){
			String message  = "MessageFilter constructor: objectClass can't be null.";
			throw new DispatchingException(message);
		}
		
		this.object = objectClass;
		
		if(objectClass.equals(MainControlPanel.class)){
			expectedCommandMessageClass = PowerStationGenerationSchedule.class;
			expectedStateMessageClass = PowerStationState.class;
		}
	}
	
	/*-----------------------------------------------------------------------*/
	
	public boolean isCommandMessageValid(Message message){
		throwExceptionIfMessageIsNull(message, "isCommandMessageValid(Message message)");
		return message.getClass() == expectedCommandMessageClass;
	}
	
	private void throwExceptionIfMessageIsNull(Message message, String method){
		if(message == null){
			String exceptionMessage = String.format(
					"MessageFilter %s method: message can't be null.", method);
			throw new DispatchingException(exceptionMessage);
		}
	}
	
	public boolean isStateMessageValid(Message message){
		throwExceptionIfMessageIsNull(message, "isStateMessageValid(Message message)");
		return message.getClass() == expectedStateMessageClass;
	}

	public boolean verifyParametersMessage(Message message){
		throwExceptionIfMessageIsNull(message, "isParametersMessageValid(Message message)");
		return message.getClass() == expectedParametersMessageClass;
	}
	
	/*-----------------------------------------------------------------------*/
	
	public String getExpectedCommandMessageClassName(){
		return expectedCommandMessageClass.getSimpleName();
	}
	
	public String getExpectedStateMessageClassName(){
		return expectedStateMessageClass.getSimpleName();
	}
	
	public String getExpectedParametersMessageClassName(){
		return expectedParametersMessageClass.getSimpleName();
	}
}