package com.epsm.electricPowerSystemModel.model.dispatch;

import com.epsm.electricPowerSystemModel.model.generalModel.PowerObject;

public class MessageFilter {
	private Class<? extends Message> expectedCommandMessageClass;
	private Class<? extends Message> expectedStateMessageClass;
	private Class<? extends Message> expectedParametersMessageClass;
	private Class<? extends PowerObject> object;
	
	public MessageFilter(Class<? extends PowerObject> object) {
		this.object = object;
		
	}
	
	public boolean verifyCommandMessage(Message message){
		return message.getClass().equals(expectedCommandMessageClass.getClass());
	}
	
	public boolean verifyStateMessage(Message message){
		return message.getClass().equals(expectedStateMessageClass.getClass());
	}

	public boolean verifyParametersMessage(Message message){
		return message.getClass().equals(expectedParametersMessageClass.getClass());
	}
	
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