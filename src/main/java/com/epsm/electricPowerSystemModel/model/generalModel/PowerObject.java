package com.epsm.electricPowerSystemModel.model.generalModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingObject;
import com.epsm.electricPowerSystemModel.model.dispatch.ObjectConnectionManager;

public abstract class PowerObject implements DispatchingObject{
	protected long id;//must not bee changed after creation
	protected ElectricPowerSystemSimulation simulation;
	@SuppressWarnings("unused")
	private ObjectConnectionManager manager;
	private Logger logger;

	public PowerObject(ElectricPowerSystemSimulation simulation, TimeService timeService,
			Dispatcher dispatcher) {
		
		if(simulation == null){
			String message = "PowerObject constructor: simulation must not be null.";
			throw new DispatchingException(message);
		}else if(timeService == null){
			String message = "PowerObject constructor: timeService must not be null.";
			throw new DispatchingException(message);
		}else if(dispatcher == null){
			String message = "PowerObject constructor: dispatcher must not be null.";
			throw new DispatchingException(message);
		}
		
		id = simulation.generateId();
		this.simulation = simulation;
		manager = new ObjectConnectionManager(timeService, dispatcher, this);
		logger = LoggerFactory.getLogger(PowerObject.class);
		logger.info("{}#{} was created.", this.getClass().getSimpleName(), id);
	}

	public long getId(){
		return id;
	}
}
