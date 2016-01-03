package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;

public abstract class CommandValidator{
	protected Command command;
	protected Parameters parameters;
	
	protected void  validate(Command command, Parameters parameters){
		saveIncomingValues(command, parameters);
		validateOnId();
	}
	
	private void saveIncomingValues(Command command, Parameters parameters){
		this.command = command;
		this.parameters = parameters;
	}
	
	private void validateOnId(){
		if(areIdsNotEquals()){
			String className = getClass().getSimpleName();
			long commandId = command.getPowerObjectId();
			long parametersId = parameters.getPowerObjectId();
			String message = String.format("%s validatePowerObjectsId(...): id numbers in command#%d"
					+ " and parameters#%d are different.", className, commandId, parametersId);
			
			throw new DispatchingException(message);
		}
	}
	
	private boolean areIdsNotEquals(){
		return command.getPowerObjectId() != parameters.getPowerObjectId();
	}
}
