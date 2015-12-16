package test.java.com.yvhobby.epsm.model.manualTesting;

import main.java.com.yvhobby.epsm.model.consumption.PowerConsumerWithScheduledLoad;
import main.java.com.yvhobby.epsm.model.consumption.PowerConsumerWithShockLoad;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulationImpl;
import main.java.com.yvhobby.epsm.model.generation.AstaticRegulatioUnit;
import main.java.com.yvhobby.epsm.model.generation.ControlUnit;
import main.java.com.yvhobby.epsm.model.generation.Generator;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;

public class ModelInitializator {
	private ElectricPowerSystemSimulationImpl powerSystemSimulation;
	
	public void initialize(ElectricPowerSystemSimulationImpl powerSystem){
		this.powerSystemSimulation = powerSystem;
		createAndBoundObjects();
	}
	
	private void createAndBoundObjects(){
		createConsumerAndAddItToEnergySystem();
		createPowerStationAndAddToEnergySystem();
	}
	
	private void createConsumerAndAddItToEnergySystem(){
		float[] pattern = new float[]{
				64.88f,  59.54f,  55.72f,  51.90f, 	48.47f,  48.85f,
				48.09f,  57.25f,  76.35f,  91.60f,  100.0f,  99.23f,
				91.60f,  91.60f,  91.22f,  90.83f,  90.83f,  90.83f,
				90.83f,  90.83f,  90.83f,  90.83f,  90.83f,  83.96f 
		};
		
		//first(shock)
		PowerConsumerWithShockLoad powerConsumer = new PowerConsumerWithShockLoad();
		powerConsumer.setDegreeOfDependingOnFrequency(2);
		powerConsumer.setMaxLoad(20f);
		powerConsumer.setMaxWorkDurationInSeconds(300);
		powerConsumer.setMaxPauseBetweenWorkInSeconds(200);
		powerConsumer.setElectricalPowerSystemSimulation(powerSystemSimulation);
		
		//second(scheduled)
		PowerConsumerWithScheduledLoad powerConsumer_2 = new PowerConsumerWithScheduledLoad();
		powerConsumer_2.setDegreeOfDependingOnFrequency(2);
		powerConsumer_2.setApproximateLoadByHoursOnDayInPercent(pattern);
		powerConsumer_2.setMaxLoadWithoutRandomInMW(100);
		powerConsumer_2.setRandomFluctuationsInPercent(10);
		powerConsumer_2.setElectricalPowerSystemSimulation(powerSystemSimulation);

		//adding
		powerSystemSimulation.addPowerConsumer(powerConsumer);
		powerSystemSimulation.addPowerConsumer(powerConsumer_2);
	}
	
	private void createPowerStationAndAddToEnergySystem(){
		
		PowerStation powerStation = new PowerStation();
		powerSystemSimulation.addPowerStation(powerStation);
		
		//first(astatic)
		AstaticRegulatioUnit regulationUnit = new AstaticRegulatioUnit();
		ControlUnit controlUnit = new ControlUnit();
		Generator generator = new Generator();
			
		regulationUnit.setControlUnit(controlUnit);
		regulationUnit.setGenerator(generator);
		
		controlUnit.setCoefficientOfStatism(4);
		controlUnit.setRequiredFrequency(50);
		controlUnit.TurneOnAstaticRegulation();
		controlUnit.setGenerator(generator);
		controlUnit.setElectricPowerSystemSimulation(powerSystemSimulation);
		controlUnit.setAstaticRegulationUnit(regulationUnit);
		
		generator.setControlUnit(controlUnit);
		generator.setMinimalTechnologyPower(5);
		generator.setNominalPowerInMW(95);
		generator.turnOnGenerator();

		//second(static)
		AstaticRegulatioUnit regulationUnit_2 = new AstaticRegulatioUnit();
		ControlUnit controlUnit_2 = new ControlUnit();
		Generator generator_2 = new Generator();
			
		regulationUnit_2.setControlUnit(controlUnit_2);
		regulationUnit_2.setGenerator(generator_2);
		
		controlUnit_2.setCoefficientOfStatism(4);
		controlUnit_2.setRequiredFrequency(50);
		controlUnit_2.setPowerAtRequiredFrequency(30);
		controlUnit_2.setGenerator(generator_2);
		controlUnit_2.setElectricPowerSystemSimulation(powerSystemSimulation);
		controlUnit_2.setAstaticRegulationUnit(regulationUnit);
		
		generator_2.setControlUnit(controlUnit_2);
		generator_2.setMinimalTechnologyPower(1);
		generator_2.setNominalPowerInMW(35);
		generator_2.turnOnGenerator();
		
		//adding
		powerStation.addGenerator(generator);
		powerStation.addGenerator(generator_2);
	}
}
