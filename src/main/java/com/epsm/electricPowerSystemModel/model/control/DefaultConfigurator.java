package main.java.com.epsm.electricPowerSystemModel.model.control;

import main.java.com.epsm.electricPowerSystemModel.model.consumption.ScheduledLoadConsumer;
import main.java.com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumer;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.MainControlPanel;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSender;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.epsm.electricPowerSystemModel.model.generation.AstaticRegulator;
import main.java.com.epsm.electricPowerSystemModel.model.generation.Generator;
import main.java.com.epsm.electricPowerSystemModel.model.generation.PowerStation;
import main.java.com.epsm.electricPowerSystemModel.model.generation.StaticRegulator;
import test.java.com.epsm.electricPowerSystemModel.model.constantsForTests.TestsConstants;

public class DefaultConfigurator {
	private ElectricPowerSystemSimulation simulation;
	private MainControlPanel controlPanel;
	
	public void initialize(ElectricPowerSystemSimulation simulation){
		this.simulation = simulation;
		
		createAndBoundObjects();
	}
	
	private void createAndBoundObjects(){
		createConsumerAndAddItToEnergySystem();
		createPowerStationAndAddToEnergySystem();
	}
	
	private void createConsumerAndAddItToEnergySystem(){
		float[] pattern = TestsConstants.LOAD_BY_HOURS;
		
		ShockLoadConsumer powerConsumer_scheduled = new ShockLoadConsumer(1);
		powerConsumer_scheduled.setDegreeOfDependingOnFrequency(2);
		powerConsumer_scheduled.setMaxLoad(10f);
		powerConsumer_scheduled.setMaxWorkDurationInSeconds(300);
		powerConsumer_scheduled.setMaxPauseBetweenWorkInSeconds(200);
		powerConsumer_scheduled.setElectricalPowerSystemSimulation(simulation);
		
		ScheduledLoadConsumer powerConsumer_shock = new ScheduledLoadConsumer(2);
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
	}
}
