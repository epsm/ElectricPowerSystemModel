package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;
import com.epsm.electricPowerSystemModel.model.dispatch.State;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.RealTimeOperations;
import com.epsm.electricPowerSystemModel.model.generalModel.SimulationObject;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public abstract class PowerObject implements SimulationObject, RealTimeOperations{
	protected long id;//must not bee changed after creation
	protected Parameters parameters;
	protected ElectricPowerSystemSimulation simulation;
	protected TimeService timeService;
	private ObjectConnectionManager manager;
	protected Logger logger;

	public PowerObject(ElectricPowerSystemSimulation simulation, TimeService timeService,
			Dispatcher dispatcher, Parameters parameters) {
		
		if(simulation == null){
			String message = "PowerObject constructor: simulation can't be null.";
			throw new IllegalArgumentException(message);
		}else if(timeService == null){
			String message = "PowerObject constructor: timeService can't be null.";
			throw new IllegalArgumentException(message);
		}else if(dispatcher == null){
			String message = "PowerObject constructor: dispatcher can't be null.";
			throw new IllegalArgumentException(message);
		}else if(parameters == null){
			String message = "PowerObject constructor: parameters can't be null.";
			throw new IllegalArgumentException(message);
		}
		
		this.simulation = simulation;
		this.timeService = timeService;
		this.parameters = parameters;
		id = parameters.getPowerObjectId();
		manager = new ObjectConnectionManager(timeService, dispatcher, this);
		
		logger = LoggerFactory.getLogger(PowerObject.class);
		logger.info("{} was created.", this);
	}

	public long getId(){
		return id;
	}
	
	public final Parameters getParameters(){
		return parameters;
	}
	
	@Override
	public final void doRealTimeDependingOperations(){
		manager.manageConnection();
	}
	
	@Override
	public final void executeCommand(Command command){
		manager.executeCommand(command);
	}
	
	protected abstract void performDispatcheCommand(Command command);
	
	protected abstract State getState();
	
	@Override
	public String toString() {
		return String.format("%s#%d", getClass().getSimpleName(), id);
	}
}
