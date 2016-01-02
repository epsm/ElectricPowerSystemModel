package com.epsm.electricPowerSystemModel.model.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.consumption.ScheduledLoadConsumer;
import com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumer;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.SimulationException;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;
import com.epsm.electricPowerSystemModel.model.generation.Generator;
import com.epsm.electricPowerSystemModel.model.generation.MainControlPanel;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationGenerationSchedule;

public class DefaultConfigurator {
	private ElectricPowerSystemSimulation simulation;
	private Dispatcher dispatcher;
	private TimeService timeService;
	
	public final static float[] LOAD_BY_HOURS = new float[]{
			64.88f,  59.54f,  55.72f,  51.90f, 	48.47f,  48.85f,
			48.09f,  57.25f,  76.35f,  91.60f,  100.0f,  99.23f,
			91.60f,  91.60f,  91.22f,  90.83f,  90.83f,  90.83f,
			90.83f,  90.83f,  90.83f,  90.83f,  90.83f,  83.96f 
	};
	
	private Logger logger = LoggerFactory.getLogger(DefaultConfigurator.class);
	
	public void initialize(ElectricPowerSystemSimulation simulation, Dispatcher dispatcher){
				if(simulation == null){
			logger.error("Attempt to initialize model without simulation.");
			throw new SimulationException("DefaultConfigurer: Simulation must not be null.");
		}
		
		if(dispatcher == null){
			logger.error("Attempt to initialize model without dispatcher.");
			throw new SimulationException("DefaultConfigurer: Dispatcher must not be null.");
		}
		
		this.simulation = simulation;
		this.dispatcher = dispatcher;
		timeService = new TimeService();
		createAndBoundObjects();
	}
	
	private void createAndBoundObjects(){
		createConsumerAndAddItToEnergySystem();
		createPowerStationAndAddToEnergySystem();
	}
	
	private void createConsumerAndAddItToEnergySystem(){
		float[] pattern = LOAD_BY_HOURS;
		
		ShockLoadConsumer shockLoadConsumer 
				= new ShockLoadConsumer(simulation, timeService, dispatcher, DispatcherMessage.class);
		shockLoadConsumer.setDegreeOfDependingOnFrequency(2);
		shockLoadConsumer.setMaxLoad(10f);
		shockLoadConsumer.setMaxWorkDurationInSeconds(300);
		shockLoadConsumer.setMaxPauseBetweenWorkInSeconds(200);
		
		ScheduledLoadConsumer scheduledLoadConsumer 
				= new ScheduledLoadConsumer(simulation, timeService, dispatcher, DispatcherMessage.class);
		scheduledLoadConsumer.setDegreeOfDependingOnFrequency(2);
		scheduledLoadConsumer.setApproximateLoadByHoursOnDayInPercent(pattern);
		scheduledLoadConsumer.setMaxLoadWithoutRandomInMW(100);
		scheduledLoadConsumer.setRandomFluctuationsInPercent(10);

		simulation.addPowerConsumer(shockLoadConsumer);
		simulation.addPowerConsumer(scheduledLoadConsumer);
	}
	
	private void createPowerStationAndAddToEnergySystem(){
		PowerStation powerStation = new PowerStation();
		@SuppressWarnings("unused")
		MainControlPanel controlPanel = new MainControlPanel(simulation, timeService, dispatcher,
				PowerStationGenerationSchedule.class, powerStation);
		Generator generator_1 = new Generator(simulation, 1);
		Generator generator_2 = new Generator(simulation, 2);
		
		simulation.addPowerStation(powerStation);
		generator_1.setMinimalPowerInMW(5);
		generator_1.setNominalPowerInMW(40);
		generator_2.setMinimalPowerInMW(25);
		generator_2.setNominalPowerInMW(100);
		powerStation.addGenerator(generator_1);
		powerStation.addGenerator(generator_2);
	}
}
