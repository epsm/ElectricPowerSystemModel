package test.java.com.yvaleriy85.esm.model.manualTesting;

import main.java.com.yvaleriy85.esm.model.ControlUnit;
import main.java.com.yvaleriy85.esm.model.DailyConsumptionPattern;
import main.java.com.yvaleriy85.esm.model.ElectricPowerSystemSimulation;
import main.java.com.yvaleriy85.esm.model.Generator;
import main.java.com.yvaleriy85.esm.model.PowerConsumerWithCalmLoad;
import main.java.com.yvaleriy85.esm.model.PowerStation;

public class ModelInitializator {
	private ElectricPowerSystemSimulation powerSystemSimulation;
	
	public void initialize(ElectricPowerSystemSimulation powerSystem){
		this.powerSystemSimulation = powerSystem;
		createAndBoundObjects();
	}
	
	private void createAndBoundObjects(){
		createConsumerAndAddItToEnergySystem();
		createPowerStationAndAddToEnergySystem();
	}
	
	private void createConsumerAndAddItToEnergySystem(){
		//PowerConsumer powerConsumer = new PowerConsumerWithConstantPower();
		
		//first
		DailyConsumptionPattern pattern = new DailyConsumptionPattern();
		PowerConsumerWithCalmLoad powerConsumer = new PowerConsumerWithCalmLoad();
		powerConsumer.setDailyPattern(pattern);
		powerConsumer.setMaxConsumptionWithoutRandomInMW(100);
		powerConsumer.setRandomComponentInPercent(10);
		powerConsumer.setElectricalPowerSystemSimulation(powerSystemSimulation);
		
		//second
		PowerConsumerWithCalmLoad powerConsumer_2 = new PowerConsumerWithCalmLoad();
		powerConsumer_2.setDailyPattern(pattern);
		powerConsumer_2.setMaxConsumptionWithoutRandomInMW(100);
		powerConsumer_2.setRandomComponentInPercent(10);
		powerConsumer_2.setElectricalPowerSystemSimulation(powerSystemSimulation);

		//adding
		powerSystemSimulation.addPowerConsumer(powerConsumer);
		powerSystemSimulation.addPowerConsumer(powerConsumer_2);
	}
	
	private void createPowerStationAndAddToEnergySystem(){
		//first
		ControlUnit controlUnit = new ControlUnit();
		Generator generator = new Generator();
		PowerStation powerStation = new PowerStation();
		
		controlUnit.setCoefficientOfStatism(4);
		controlUnit.setRequiredFrequency(50);
		controlUnit.setPowerAtRequiredFrequency(100);
		controlUnit.TurneOnAstaticRegulation();
		controlUnit.setGenerator(generator);
		controlUnit.setElectricPowerSystemSimulation(powerSystemSimulation);
		
		generator.setControlUnit(controlUnit);
		generator.setMinimalTechnologyPower(1);
		generator.setNominalPowerInMW(150);
		generator.turnOnGenerator();

		//second
		ControlUnit controlUnit_2 = new ControlUnit();
		Generator generator_2 = new Generator();
				
		controlUnit_2.setCoefficientOfStatism(4);
		controlUnit_2.setRequiredFrequency(50);
		controlUnit_2.setPowerAtRequiredFrequency(60);
		//controlUnit_2.TurneOnAstaticRegulation();
		controlUnit_2.setGenerator(generator_2);
		controlUnit_2.setElectricPowerSystemSimulation(powerSystemSimulation);
		
		generator_2.setControlUnit(controlUnit_2);
		generator_2.setMinimalTechnologyPower(1);
		generator_2.setNominalPowerInMW(150);
		generator_2.turnOnGenerator();
		
		//adding
		powerStation.addGenerator(generator);
		powerStation.addGenerator(generator_2);
		
		powerSystemSimulation.addPowerStation(powerStation);
	}
}
