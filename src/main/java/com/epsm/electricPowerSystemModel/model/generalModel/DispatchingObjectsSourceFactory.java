package com.epsm.electricPowerSystemModel.model.generalModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.epsm.electricPowerSystemModel.model.consumption.ScheduledLoadConsumerCreationParametersStub;
import com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumerCreationParametersStub;
import com.epsm.electricPowerSystemModel.model.control.SimulationRunner;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generalModel.DispatchingObjectsSource;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationCreationParametersStub;

@Component
public class DispatchingObjectsSourceFactory {
	private ElectricPowerSystemSimulation simulation;
	private SimulationRunner runner;
	private TimeService timeService;
	private Dispatcher dispatcher;

	@Autowired
	public DispatchingObjectsSourceFactory(TimeService timeService, Dispatcher dispatcher) {
		this.timeService = timeService;
		this.dispatcher = dispatcher;
		runner = new SimulationRunner();
	}

	@Bean
	public DispatchingObjectsSource createSource(){
		makeSimulation(dispatcher);
		fillSimulationWithObjects();
		runSimulation();
		
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
