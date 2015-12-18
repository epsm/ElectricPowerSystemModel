package test.java.com.yvhobby.epsm.model.manualTesting;

import main.java.com.yvhobby.epsm.model.consumption.PowerConsumerWithScheduledLoad;
import main.java.com.yvhobby.epsm.model.consumption.PowerConsumerWithShockLoad;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulationImpl;
import main.java.com.yvhobby.epsm.model.generation.AstaticRegulationUnit;
import main.java.com.yvhobby.epsm.model.generation.ControlUnit;
import main.java.com.yvhobby.epsm.model.generation.Generator;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;
import test.java.com.yvhobby.epsm.model.constantsForTests.TestsConstants;

public class ModelInitializator {
	private ElectricPowerSystemSimulationImpl simulation;
	
	public void initialize(ElectricPowerSystemSimulationImpl powerSystem){
		this.simulation = powerSystem;
		createAndBoundObjects();
	}
	
	private void createAndBoundObjects(){
		createConsumerAndAddItToEnergySystem();
		createPowerStationAndAddToEnergySystem();
	}
	
	private void createConsumerAndAddItToEnergySystem(){
		float[] pattern = TestsConstants.LOAD_BY_HOURS;
		
		//first(shock)
		PowerConsumerWithShockLoad powerConsumer = new PowerConsumerWithShockLoad();
		powerConsumer.setDegreeOfDependingOnFrequency(2);
		powerConsumer.setMaxLoad(20f);
		powerConsumer.setMaxWorkDurationInSeconds(300);
		powerConsumer.setMaxPauseBetweenWorkInSeconds(200);
		powerConsumer.setElectricalPowerSystemSimulation(simulation);
		
		//second(scheduled)
		PowerConsumerWithScheduledLoad powerConsumer_2 = new PowerConsumerWithScheduledLoad();
		powerConsumer_2.setDegreeOfDependingOnFrequency(2);
		powerConsumer_2.setApproximateLoadByHoursOnDayInPercent(pattern);
		powerConsumer_2.setMaxLoadWithoutRandomInMW(100);
		powerConsumer_2.setRandomFluctuationsInPercent(10);
		powerConsumer_2.setElectricalPowerSystemSimulation(simulation);

		//adding
		simulation.addPowerConsumer(powerConsumer);
		simulation.addPowerConsumer(powerConsumer_2);
	}
	
	private void createPowerStationAndAddToEnergySystem(){
		
		PowerStation powerStation = new PowerStation();
		simulation.addPowerStation(powerStation);
		
		//first(astatic)
		Generator generator_1 = new Generator();
		AstaticRegulationUnit regulationUnit_1 = new AstaticRegulationUnit(simulation, generator_1);
		ControlUnit controlUnit_1 = new ControlUnit(simulation, generator_1);
		
		generator_1.setAstaticRegulationUnit(regulationUnit_1);
		generator_1.setControlUnit(controlUnit_1);
		generator_1.setMinimalTechnologyPower(5);
		generator_1.setNominalPowerInMW(95);
		generator_1.turnOnGenerator();
		generator_1.turnOnAstaticRegulation();
		generator_1.setId(1);
		
		//second(static)
		Generator generator_2 = new Generator();
		AstaticRegulationUnit regulationUnit_2 = new AstaticRegulationUnit(simulation, generator_2);
		ControlUnit controlUnit_2 = new ControlUnit(simulation, generator_2);
		
		controlUnit_2.setPowerAtRequiredFrequency(30);
		
		generator_2.setAstaticRegulationUnit(regulationUnit_2);
		generator_2.setControlUnit(controlUnit_2);
		generator_2.setMinimalTechnologyPower(1);
		generator_2.setNominalPowerInMW(35);
		generator_2.turnOnGenerator();
		generator_2.setId(2);
		
		//adding
		powerStation.addGenerator(generator_1);
		powerStation.addGenerator(generator_2);
	}
}
