package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import com.epsm.electricPowerSystemModel.model.consumption.Consumer;
import com.epsm.electricPowerSystemModel.model.consumption.ConsumerParameters;
import com.epsm.electricPowerSystemModel.model.consumption.ConsumerState;
import com.epsm.electricPowerSystemModel.model.consumption.ConsumptionPermissionStub;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationParameters;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationState;

public class MessageFilter {
	private Class<? extends Message> expectedCommandMessageClass;
	private Class<? extends Message> expectedStateMessageClass;
	private Class<? extends Message> expectedParametersMessageClass;
	
	public MessageFilter(Class<? extends PowerObject> objectClass) {
		if(objectClass == null){
			String message  = "MessageFilter constructor: objectClass can't be null.";
			throw new DispatchingException(message);
		}
		
		if(isObjectPowerStation(objectClass)){
			expectedCommandMessageClass = PowerStationGenerationSchedule.class;
			expectedStateMessageClass = PowerStationState.class;
			expectedParametersMessageClass = PowerStationParameters.class;
		}else if(isObjectInstanceOfConsumer(objectClass)){
			expectedCommandMessageClass = ConsumptionPermissionStub.class;
			expectedStateMessageClass = ConsumerState.class;
			expectedParametersMessageClass = ConsumerParameters.class;
		}else{
			String message = String.format("%s constructor: %s.class is not supported.", 
					getClass().getSimpleName(), objectClass.getSimpleName());
			throw new DispatchingException(message);
		}
	}
	
	private boolean isObjectPowerStation(Class<? extends PowerObject> objectClass){
		return objectClass == PowerStation.class;
	}
	
	private boolean isObjectInstanceOfConsumer(Class<? extends PowerObject> objectClass){
		return Consumer.class.isAssignableFrom(objectClass);
	}
	
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

	public boolean isParametersMessageValid(Message message){
		throwExceptionIfMessageIsNull(message, "isParametersMessageValid(Message message)");
		return message.getClass() == expectedParametersMessageClass;
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