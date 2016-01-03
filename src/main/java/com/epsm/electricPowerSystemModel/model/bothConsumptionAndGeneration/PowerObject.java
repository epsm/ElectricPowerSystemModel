package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;
import com.epsm.electricPowerSystemModel.model.dispatch.State;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.SimulationObject;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public abstract class PowerObject implements SimulationObject{
	protected long id;//must not bee changed after creation
	protected ElectricPowerSystemSimulation simulation;
	protected TimeService timeService;
	private ObjectConnectionManager manager;
	protected Logger logger;

	public PowerObject(ElectricPowerSystemSimulation simulation, TimeService timeService,
			Dispatcher dispatcher) {
		
		if(simulation == null){
			String message = "PowerObject constructor: simulation can't be null.";
			throw new DispatchingException(message);
		}else if(timeService == null){
			String message = "PowerObject constructor: timeService can't be null.";
			throw new DispatchingException(message);
		}else if(dispatcher == null){
			String message = "PowerObject constructor: dispatcher can't be null.";
			throw new DispatchingException(message);
		}
		
		id = simulation.generateId();
		this.simulation = simulation;
		this.timeService = timeService;
		manager = new ObjectConnectionManager(timeService, dispatcher, this);
		logger = LoggerFactory.getLogger(PowerObject.class);
		logger.info("{}#{} was created.", getClass().getSimpleName(), id);
	}

	public long getId(){
		return id;
	}
	
	@Override
	public final void doRealTimeDependingOperations(){
		manager.sendMessageIfItNecessary();
	}
	
	protected abstract State getState();
	protected abstract Parameters getParameters();
}
