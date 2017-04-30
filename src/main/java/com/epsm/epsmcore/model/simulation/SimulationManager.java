package com.epsm.epsmcore.model.simulation;

import com.epsm.epsmcore.model.consumption.RandomLoadConsumerFactoryStub;
import com.epsm.epsmcore.model.consumption.ScheduledLoadConsumerFactoryStub;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.generation.PowerStationFactoryStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class SimulationManager {

	private SimulationRunner runner = new SimulationRunner();
	private Simulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private LocalDateTime startDateTime;
	private static final Logger logger = LoggerFactory.getLogger(SimulationManager.class);

	public SimulationManager(TimeService timeService, Dispatcher dispatcher, LocalDateTime startDateTime) {
		this.timeService = timeService;
		this.dispatcher = dispatcher;
		this.startDateTime = startDateTime;
	}

	public Simulation createAndRun(){
		simulation = new Simulation(startDateTime);
		fillSimulationWithObjects();
		runSimulation();
		
		logger.info("Simulation created and run.");
		
		return simulation;
	}
	
	private void fillSimulationWithObjects(){
		PowerStationFactoryStub powerStationFactoryStub = new PowerStationFactoryStub(simulation, dispatcher, timeService);
		ScheduledLoadConsumerFactoryStub scheduledLoadConsumerFactoryStub = new ScheduledLoadConsumerFactoryStub(simulation, dispatcher, timeService);
		RandomLoadConsumerFactoryStub randomLoadConsumerFactoryStub = new RandomLoadConsumerFactoryStub(simulation, dispatcher, timeService);

		simulation.addPowerStation(powerStationFactoryStub.createPowerStation());

		simulation.addConsumer(scheduledLoadConsumerFactoryStub.createConsumer());
		simulation.addConsumer(randomLoadConsumerFactoryStub.createConsumer());
		simulation.addConsumer(scheduledLoadConsumerFactoryStub.createConsumer());
		simulation.addConsumer(scheduledLoadConsumerFactoryStub.createConsumer());
		simulation.addConsumer(randomLoadConsumerFactoryStub.createConsumer());
		simulation.addConsumer(scheduledLoadConsumerFactoryStub.createConsumer());
		simulation.addConsumer(scheduledLoadConsumerFactoryStub.createConsumer());
		simulation.addConsumer(randomLoadConsumerFactoryStub.createConsumer());
		simulation.addConsumer(scheduledLoadConsumerFactoryStub.createConsumer());
		simulation.addConsumer(scheduledLoadConsumerFactoryStub.createConsumer());
		simulation.addConsumer(randomLoadConsumerFactoryStub.createConsumer());
		simulation.addConsumer(scheduledLoadConsumerFactoryStub.createConsumer());
	}
	
	private void runSimulation(){
		runner.runSimulation(simulation);
	}
}