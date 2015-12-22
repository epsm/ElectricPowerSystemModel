package main.java.com.yvhobby.epsm.model.control;

import main.java.com.yvhobby.epsm.model.consumption.ConsumerWithScheduledLoad;
import main.java.com.yvhobby.epsm.model.consumption.ConsumerWithShockLoad;
import main.java.com.yvhobby.epsm.model.dispatch.Dispatcher;
import main.java.com.yvhobby.epsm.model.dispatch.MainControlPanel;
import main.java.com.yvhobby.epsm.model.dispatch.ReportSender;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generation.AstaticRegulationUnit;
import main.java.com.yvhobby.epsm.model.generation.ControlUnit;
import main.java.com.yvhobby.epsm.model.generation.Generator;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;
import test.java.com.yvhobby.epsm.model.constantsForTests.TestsConstants;

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
		AstaticRegulationUnit regulationUnit_1 = new AstaticRegulationUnit(simulation, generator_1);
		ControlUnit controlUnit_1 = new ControlUnit(simulation, generator_1);
		
		generator_1.setAstaticRegulationUnit(regulationUnit_1);
		generator_1.setControlUnit(controlUnit_1);
		generator_1.setMinimalTechnologyPower(5);
		generator_1.setNominalPowerInMW(40);
		
		Generator generator_2 = new Generator(2);
		AstaticRegulationUnit regulationUnit_2 = new AstaticRegulationUnit(simulation, generator_2);
		ControlUnit controlUnit_2 = new ControlUnit(simulation, generator_2);
		
		generator_2.setAstaticRegulationUnit(regulationUnit_2);
		generator_2.setControlUnit(controlUnit_2);
		generator_2.setMinimalTechnologyPower(25);
		generator_2.setNominalPowerInMW(100);
		
		powerStation.addGenerator(generator_1);
		powerStation.addGenerator(generator_2);
	}
}
