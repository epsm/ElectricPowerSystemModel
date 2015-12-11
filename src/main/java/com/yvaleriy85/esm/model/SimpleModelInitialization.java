package main.java.com.yvaleriy85.esm.model;

public class SimpleModelInitialization {
	EnergySystem energySystem = new EnergySystem();
	
	private void initialize(){
		createAndBoundObjects();
		Simulation.startSimulation();
	}
	
	private void createAndBoundObjects(){
		createConsumerAndAddItToEnergySystem();
		createPowerStationAndAddToEnergySystem();
		Simulation.setEnergySystem(energySystem);
	}
	
	private void createConsumerAndAddItToEnergySystem(){
		//PowerConsumer powerConsumer = new PowerConsumerWithConstantPower();
		
		//first
		DailyConsumptionPattern pattern = new DailyConsumptionPattern();
		PowerConsumerWithCalmLoad powerConsumer = new PowerConsumerWithCalmLoad();
		powerConsumer.setDailyPattern(pattern);
		powerConsumer.setMaxConsumptionWithoutRandomInMW(100);
		
		//second
		PowerConsumerWithCalmLoad powerConsumer_2 = new PowerConsumerWithCalmLoad();
		powerConsumer_2.setDailyPattern(pattern);
		powerConsumer_2.setMaxConsumptionWithoutRandomInMW(100);

		energySystem.addPowerConsumer(powerConsumer);
		energySystem.addPowerConsumer(powerConsumer_2);
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
		
		generator.setControlUnit(controlUnit);
		generator.setMinimalTechnologyPower(1);
		generator.setNominalPowerInMW(150);
		generator.turnOnGenerator();
		
		powerStation.addGenerator(generator);
		
		//second
		ControlUnit controlUnit_2 = new ControlUnit();
		Generator generator_2 = new Generator();
				
		controlUnit_2.setCoefficientOfStatism(4);
		controlUnit_2.setRequiredFrequency(50);
		controlUnit_2.setPowerAtRequiredFrequency(60);
		//controlUnit_2.TurneOnAstaticRegulation();
		controlUnit_2.setGenerator(generator_2);
		
		generator_2.setControlUnit(controlUnit_2);
		generator_2.setMinimalTechnologyPower(1);
		generator_2.setNominalPowerInMW(150);
		generator_2.turnOnGenerator();
		
		powerStation.addGenerator(generator_2);
		
		energySystem.addPowerStation(powerStation);
	}
	
	public static void main(String[] args) {
		new SimpleModelInitialization().initialize();
	}
}
