package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public abstract class AbstractPowerObjectFactory {
	protected ElectricPowerSystemSimulation simulation;
	protected TimeService timeService;
	protected Dispatcher dispatcher;
	
	public AbstractPowerObjectFactory(ElectricPowerSystemSimulation simulation,
			TimeService timeService, Dispatcher dispatcher) {
		
		if(simulation == null){
			String message = "AbstractPowerObjectFactory constructor: simulation can't be null.";
			throw new IllegalArgumentException(message);
		}else if(timeService == null){
			String message = "AbstractPowerObjectFactory constructor: timeService can't be null.";
			throw new IllegalArgumentException(message);
		}else if(dispatcher == null){
			String message = "AbstractPowerObjectFactory constructor: dispatcher can't be null.";
			throw new IllegalArgumentException(message);
		}
		
		this.simulation = simulation;
		this.timeService = timeService;
		this.dispatcher = dispatcher;
	}
}
