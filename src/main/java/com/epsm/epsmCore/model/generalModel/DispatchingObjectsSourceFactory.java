package com.epsm.epsmCore.model.generalModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.consumption.ScheduledLoadConsumerCreationParametersStub;
import com.epsm.epsmCore.model.consumption.ShockLoadConsumerCreationParametersStub;
import com.epsm.epsmCore.model.control.SimulationRunner;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.generalModel.DispatchingObjectsSource;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.epsmCore.model.generalModel.TimeService;
import com.epsm.epsmCore.model.generation.PowerStationCreationParametersStub;

public class DispatchingObjectsSourceFactory {
	private ElectricPowerSystemSimulation simulation;
	private SimulationRunner runner;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private Logger logger;

	public DispatchingObjectsSourceFactory(TimeService timeService, Dispatcher dispatcher) {
		logger = LoggerFactory.getLogger(DispatchingObjectsSourceFactory.class);
		
		if(timeService == null){
			logger.error("Null timeService in constructor");
			String message = "DispatchingObjectsSourceFactory constructor: timeService"
					+ " can't be null.";
			throw new IllegalArgumentException(message);
		}else if(dispatcher == null){
			logger.error("Null timeService in constructor");
			String message = "DispatchingObjectsSourceFactory constructor: dispatcher can't"
					+ " be null.";
			throw new IllegalArgumentException(message);
		}
		
		this.timeService = timeService;
		this.dispatcher = dispatcher;
		runner = new SimulationRunner();
	}

	public DispatchingObjectsSource createSource(){
		makeSimulation(dispatcher);
		fillSimulationWithObjects();
		runSimulation();
		
		logger.info("Simulation created and run.");
		
		return simulation;
	}
	
	private void makeSimulation(Dispatcher dispatcher){
		simulation = new ElectricPowerSystemSimulationImpl(timeService, dispatcher);
	}
	
	private void fillSimulationWithObjects(){
		simulation.createPowerObject(new PowerStationCreationParametersStub());
		simulation.createPowerObject(new ShockLoadConsumerCreationParametersStub());
		simulation.createPowerObject(new ScheduledLoadConsumerCreationParametersStub());
	}
	
	private void runSimulation(){
		runner.runSimulation(simulation);
	}
}
