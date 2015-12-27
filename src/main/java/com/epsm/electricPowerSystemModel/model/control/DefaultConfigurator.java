package com.epsm.electricPowerSystemModel.model.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.consumption.ScheduledLoadConsumer;
import com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumer;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.MainControlPanelTest;
import com.epsm.electricPowerSystemModel.model.dispatch.StateSender;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.SimulationException;
import com.epsm.electricPowerSystemModel.model.generation.AstaticRegulator;
import com.epsm.electricPowerSystemModel.model.generation.Generator;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;
import com.epsm.electricPowerSystemModel.model.generation.StaticRegulator;

public class DefaultConfigurator {
	private ElectricPowerSystemSimulation simulation;
	private Dispatcher dispatcher;
	public final static float[] LOAD_BY_HOURS = new float[]{
			64.88f,  59.54f,  55.72f,  51.90f, 	48.47f,  48.85f,
			48.09f,  57.25f,  76.35f,  91.60f,  100.0f,  99.23f,
			91.60f,  91.60f,  91.22f,  90.83f,  90.83f,  90.83f,
			90.83f,  90.83f,  90.83f,  90.83f,  90.83f,  83.96f 
	};
	
	private Logger logger = LoggerFactory.getLogger(DefaultConfigurator.class);
	
	public void initialize(ElectricPowerSystemSimulation simulation, Dispatcher dispatcher){
		this.simulation = simulation;
		this.dispatcher = dispatcher;
		
		if(simulation == null){
			logger.error("Attempt to initialize model without simulation.");
			throw new SimulationException("DefaultConfigurer: Simulation must not be null.");
		}
		
		if(dispatcher == null){
			logger.error("Attempt to initialize model without dispatcher.");
			throw new SimulationException("DefaultConfigurer: Dispatcher must not be null.");
		}
		
		createAndBoundObjects();
	}
	
	private void createAndBoundObjects(){
		createConsumerAndAddItToEnergySystem();
		createPowerStationAndAddToEnergySystem();
	}
	
	private void createConsumerAndAddItToEnergySystem(){
		float[] pattern = LOAD_BY_HOURS;
		
		ShockLoadConsumer shockLoadConsumer = new ShockLoadConsumer(1, simulation);
		StateSender shockLoadCustomerSender = new StateSender(shockLoadConsumer);
		shockLoadConsumer.setDegreeOfDependingOnFrequency(2);
		shockLoadConsumer.setMaxLoad(10f);
		shockLoadConsumer.setMaxWorkDurationInSeconds(300);
		shockLoadConsumer.setMaxPauseBetweenWorkInSeconds(200);
		shockLoadConsumer.setStateSender(shockLoadCustomerSender);
		shockLoadCustomerSender.setDispatcher(dispatcher);
		
		ScheduledLoadConsumer scheduledLoadConsumer = new ScheduledLoadConsumer(2, simulation);
		StateSender scheduledLoadCustomerSender = new StateSender(scheduledLoadConsumer);
		scheduledLoadConsumer.setDegreeOfDependingOnFrequency(2);
		scheduledLoadConsumer.setApproximateLoadByHoursOnDayInPercent(pattern);
		scheduledLoadConsumer.setMaxLoadWithoutRandomInMW(100);
		scheduledLoadConsumer.setRandomFluctuationsInPercent(10);
		scheduledLoadConsumer.setStateSender(scheduledLoadCustomerSender);
		scheduledLoadCustomerSender.setDispatcher(dispatcher);

		simulation.addPowerConsumer(shockLoadConsumer);
		simulation.addPowerConsumer(scheduledLoadConsumer);
		
		//must be called after dispatcher set up
		shockLoadConsumer.registerWithDispatcher(dispatcher);
		scheduledLoadConsumer.registerWithDispatcher(dispatcher);
	}
	
	private void createPowerStationAndAddToEnergySystem(){
		PowerStation powerStation = new PowerStation(1, simulation);
		MainControlPanelTest controlPanel = new MainControlPanelTest();
		StateSender controlPanelSender = new StateSender(controlPanel);
		simulation.addPowerStation(powerStation);
		
		controlPanel.setSimulation(simulation);
		controlPanel.setStation(powerStation);
		controlPanel.setStateSender(controlPanelSender);
		controlPanelSender.setDispatcher(dispatcher);
		
		Generator generator_1 = new Generator(1);
		AstaticRegulator astaticRegulator_1 = new AstaticRegulator(simulation, generator_1);
		StaticRegulator staticRegulator_1 = new StaticRegulator(simulation, generator_1);
		
		generator_1.setAstaticRegulator(astaticRegulator_1);
		generator_1.setStaticRegulator(staticRegulator_1);
		generator_1.setMinimalPowerInMW(5);
		generator_1.setNominalPowerInMW(40);
		
		Generator generator_2 = new Generator(2);
		AstaticRegulator astaticRegulator_2 = new AstaticRegulator(simulation, generator_2);
		StaticRegulator staticRegulator_2 = new StaticRegulator(simulation, generator_2);
		
		generator_2.setAstaticRegulator(astaticRegulator_2);
		generator_2.setStaticRegulator(staticRegulator_2);
		generator_2.setMinimalPowerInMW(25);
		generator_2.setNominalPowerInMW(100);
		
		powerStation.addGenerator(generator_1);
		powerStation.addGenerator(generator_2);
		
		/*
		 * last to avoid situation when validator throws exception because dispatcher will send schedule for 2
		 * generators but station has 0.
		 */
		controlPanel.registerWithDispatcher(dispatcher);
	}
}
