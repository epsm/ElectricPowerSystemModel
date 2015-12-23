package main.java.com.epsm.electricPowerSystemModel.model.control;

import main.java.com.epsm.electricPowerSystemModel.model.consumption.ConsumerWithScheduledLoad;
import main.java.com.epsm.electricPowerSystemModel.model.consumption.ConsumerWithShockLoad;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.MainControlPanel;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSender;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.epsm.electricPowerSystemModel.model.generation.AstaticRegulator;
import main.java.com.epsm.electricPowerSystemModel.model.generation.StaticRegulator;
import main.java.com.epsm.electricPowerSystemModel.model.generation.Generator;
import main.java.com.epsm.electricPowerSystemModel.model.generation.PowerStation;
import test.java.com.epsm.electricPowerSystemModel.model.constantsForTests.TestsConstants;

public class DefaultConfigurator {
	private ElectricPowerSystemSimulation simulation;
	private MainControlPanel controlPanel;
	private Dispatcher dispatcher;
	
	public void initialize(ElectricPowerSystemSimulation simulation, Dispatcher dispatcher){
		this.simulation = simulation;
		this.dispatcher = dispatcher;
		
		createAndBoundObjects();
	}
	
	private void createAndBoundObjects(){
		createConsumerAndAddItToEnergySystem();
		createPowerStationAndAddToEnergySystem();
	}
	
	private void createConsumerAndAddItToEnergySystem(){
		float[] pattern = TestsConstants.LOAD_BY_HOURS;
		
		ConsumerWithShockLoad powerConsumer_scheduled = new ConsumerWithShockLoad(1);
		powerConsumer_scheduled.setDegreeOfDependingOnFrequency(2);
		powerConsumer_scheduled.setMaxLoad(10f);
		powerConsumer_scheduled.setMaxWorkDurationInSeconds(300);
		powerConsumer_scheduled.setMaxPauseBetweenWorkInSeconds(200);
		powerConsumer_scheduled.setElectricalPowerSystemSimulation(simulation);
		
		ConsumerWithScheduledLoad powerConsumer_shock = new ConsumerWithScheduledLoad(2);
		powerConsumer_shock.setDegreeOfDependingOnFrequency(2);
		powerConsumer_shock.setApproximateLoadByHoursOnDayInPercent(pattern);
		powerConsumer_shock.setMaxLoadWithoutRandomInMW(100);
		powerConsumer_shock.setRandomFluctuationsInPercent(10);
		powerConsumer_shock.setElectricalPowerSystemSimulation(simulation);

		simulation.addPowerConsumer(powerConsumer_scheduled);
		simulation.addPowerConsumer(powerConsumer_shock);
	}
	
	private void createPowerStationAndAddToEnergySystem(){
		PowerStation powerStation = new PowerStation(1);
		simulation.addPowerStation(powerStation);
		
		controlPanel = new MainControlPanel();
		controlPanel.setSimulation(simulation);
		controlPanel.setStation(powerStation);
		
		ReportSender sender = new ReportSender(controlPanel);
		
		Generator generator_1 = new Generator(1);
		AstaticRegulator regulationUnit_1 = new AstaticRegulator(simulation, generator_1);
		StaticRegulator controlUnit_1 = new StaticRegulator(simulation, generator_1);
		
		generator_1.setAstaticRegulator(regulationUnit_1);
		generator_1.setStaticRegulator(controlUnit_1);
		generator_1.setMinimalPowerInMW(5);
		generator_1.setNominalPowerInMW(40);
		
		Generator generator_2 = new Generator(2);
		AstaticRegulator regulationUnit_2 = new AstaticRegulator(simulation, generator_2);
		StaticRegulator controlUnit_2 = new StaticRegulator(simulation, generator_2);
		
		generator_2.setAstaticRegulator(regulationUnit_2);
		generator_2.setStaticRegulator(controlUnit_2);
		generator_2.setMinimalPowerInMW(25);
		generator_2.setNominalPowerInMW(100);
		
		powerStation.addGenerator(generator_1);
		powerStation.addGenerator(generator_2);
	}
}
