package com.epsm.electricPowerSystemModel.model.dispatch;

import java.util.Set;

public abstract class CommandContainerValidator extends CommandValidator{
	protected void validate(Command command, Parameters parameters){
		super.validate(command, parameters);
		validateOnCommandIdNumbers();
	}
	
	private void validateOnCommandIdNumbers(){
		if(areIdsNotEquals()){
			String className = getClass().getSimpleName();
			Set<Long> commandsIds = command.getCommandsIds();
			Set<Long> parametersIds = parameters.getCommandsIds();
			String message = String.format("%s validateOnCommandIdNumbers(...): commands"
					+ " ids %s, but parameters ids %s.", className, commandsIds, parametersIds);
			
			System.out.println(message);
			throw new DispatchingException(message);
		}
	}
	
	private boolean areIdsNotEquals(){
		return !command.getCommandsIds().equals(parameters.getCommandsIds());
	}
}
