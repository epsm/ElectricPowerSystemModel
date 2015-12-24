package main.java.com.epsm.electricPowerSystemModel.model.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.epsm.electricPowerSystemModel.model.consumption.ScheduledLoadConsumer;
import main.java.com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumer;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.MainControlPanel;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSender;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.SimulationException;
import main.java.com.epsm.electricPowerSystemModel.model.generation.AstaticRegulator;
import main.java.com.epsm.electricPowerSystemModel.model.generation.Generator;
import main.java.com.epsm.electricPowerSystemModel.model.generation.PowerStation;
import main.java.com.epsm.electricPowerSystemModel.model.generation.StaticRegulator;
import test.java.com.epsm.electricPowerSystemModel.model.constantsForTests.TestsConstants;

public class DefaultConfigurator {
	private ElectricPowerSystemSimulation simulation;
	private Dispatcher dispatcher;
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
		float[] pattern = TestsConstants.LOAD_BY_HOURS;
		
		ShockLoadConsumer shockLoadConsumer = new ShockLoadConsumer(1, simulation);
		ReportSender shockLoadCustomerSender = new ReportSender(shockLoadConsumer);
		shockLoadConsumer.setDegreeOfDependingOnFrequency(2);
		shockLoadConsumer.setMaxLoad(10f);
		shockLoadConsumer.setMaxWorkDurationInSeconds(300);
		shockLoadConsumer.setMaxPauseBetweenWorkInSeconds(200);
		shockLoadConsumer.setReportSender(shockLoadCustomerSender);
		shockLoadCustomerSender.setDispatcher(dispatcher);
		
		ScheduledLoadConsumer scheduledLoadConsumer = new ScheduledLoadConsumer(2, simulation);
		ReportSender scheduledLoadCustomerSender = new ReportSender(scheduledLoadConsumer);
		scheduledLoadConsumer.setDegreeOfDependingOnFrequency(2);
		scheduledLoadConsumer.setApproximateLoadByHoursOnDayInPercent(pattern);
		scheduledLoadConsumer.setMaxLoadWithoutRandomInMW(100);
		scheduledLoadConsumer.setRandomFluctuationsInPercent(10);
		scheduledLoadConsumer.setReportSender(scheduledLoadCustomerSender);
		scheduledLoadCustomerSender.setDispatcher(dispatcher);

		simulation.addPowerConsumer(shockLoadConsumer);
		simulation.addPowerConsumer(scheduledLoadConsumer);
		
		//must be called after dispatcher set up
		shockLoadConsumer.registerWithDispatcher(dispatcher);
		scheduledLoadConsumer.registerWithDispatcher(dispatcher);
	}
	
	private void createPowerStationAndAddToEnergySystem(){
		PowerStation powerStation = new PowerStation(1, simulation);
		MainControlPanel controlPanel = new MainControlPanel();
		ReportSender controlPanelSender = new ReportSender(controlPanel);
		simulation.addPowerStation(powerStation);
		
		controlPanel.setSimulation(simulation);
		controlPanel.setStation(powerStation);
		controlPanel.setReportSender(controlPanelSender);
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
