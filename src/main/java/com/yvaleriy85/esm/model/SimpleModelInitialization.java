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
		
		DailyConsumptionPattern pattern = new DailyConsumptionPattern();
		PowerConsumerWithCalmLoad powerConsumer = new PowerConsumerWithCalmLoad();
		powerConsumer.setDailyPattern(pattern);
		powerConsumer.setMaxConsumptionWithoutRandomInMW(100);

		energySystem.addPowerConsumer(powerConsumer);
	}
	
	private void createPowerStationAndAddToEnergySystem(){
		ControlUnit controlUnit = new ControlUnit();
		Generator generator = new Generator();
		PowerStation powerStation = new PowerStation();
		
		controlUnit.setCoefficientOfStatism(4);
		controlUnit.setRequiredFrequency(50);
		controlUnit.setPowerAtRequiredFrequency(100);
		controlUnit.TurneOnAstaticRegulation();
		controlUnit.setGenerator(generator);
		
		generator.setControlUnit(controlUnit);
		generator.setMinimalTechnologyPower(20);
		generator.setNominalPowerInMW(150);
		generator.turnOnGenerator();
		
		powerStation.addGenerator(generator);
		
		energySystem.addPowerStation(powerStation);
	}
	
	public static void main(String[] args) {
		new SimpleModelInitialization().initialize();
	}
}
