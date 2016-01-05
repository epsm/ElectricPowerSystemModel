package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;

public abstract class CommandValidator{
	protected Command command;
	protected Parameters parameters;
	
	protected void  validate(Command command, Parameters parameters){
		validateOnNUll(command, parameters);
		saveIncomingValues(command, parameters);
		validateOnId();
	}
	
	private void saveIncomingValues(Command command, Parameters parameters){
		this.command = command;
		this.parameters = parameters;
	}

	private void validateOnNUll(Command command,Parameters parameters){
		if(command == null){
			String message = String.format("%s validate(...) method: command is null.",
					getClass().getSimpleName());
			throw new IllegalArgumentException(message);
		}else if(parameters == null){
			String message = String.format("%s validate(...) method: parameters is null.",
					getClass().getSimpleName());
			throw new IllegalArgumentException(message);
		}
	}
	
	private void validateOnId(){
		if(areIdsNotEquals()){
			String className = getClass().getSimpleName();
			long message_1_Id = command.getPowerObjectId();
			long message_2_Id = parameters.getPowerObjectId();
			String message = String.format("%s validatePowerObjectsId(...): id numbers in"
					+ " command#%d and parameters#%d are different.", className, 
					message_1_Id, message_2_Id);
			
			throw new DispatchingException(message);
		}
	}
	
	private boolean areIdsNotEquals(){
		return command.getPowerObjectId() != parameters.getPowerObjectId();
	}
}
