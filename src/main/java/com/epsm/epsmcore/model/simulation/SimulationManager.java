package com.epsm.epsmcore.model.simulation;

import com.epsm.epsmcore.model.dispatch.Dispatcher;
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
//		simulation.createPowerObject(new PowerStationParameters());
//		simulation.createPowerObject(new ScheduledLoadConsumerParameters());
//		simulation.createPowerObject(new RandomLoadConsumerParameters());
//		simulation.createPowerObject(new ScheduledLoadConsumerParameters());
//		simulation.createPowerObject(new ScheduledLoadConsumerParameters());
//		simulation.createPowerObject(new RandomLoadConsumerParameters());
//		simulation.createPowerObject(new ScheduledLoadConsumerParameters());
//		simulation.createPowerObject(new ScheduledLoadConsumerParameters());
//		simulation.createPowerObject(new RandomLoadConsumerParameters());
//		simulation.createPowerObject(new ScheduledLoadConsumerParameters());
//		simulation.createPowerObject(new ScheduledLoadConsumerParameters());
//		simulation.createPowerObject(new RandomLoadConsumerParameters());
//		simulation.createPowerObject(new ScheduledLoadConsumerParameters());
	}
	
	private void runSimulation(){
		runner.runSimulation(simulation);
	}
}