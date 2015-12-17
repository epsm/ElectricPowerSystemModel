package main.java.com.yvhobby.epsm.model.generation;

import main.java.com.yvhobby.epsm.model.dispatch.GeneratorParameters;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationParameters;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;

public class PowerStationFactory {
		
	private ElectricPowerSystemSimulation simulation;
	private PowerStation station;
	private Generator generator;
	private ControlUnit controlUnit;
	private AstaticRegulatioUnit regulationUnit;
	private PowerStationParameters stationParameters;
	private int stationId;
	private int generatorId;
	private final int STANDART_COEFFICIENT_OF_STATISM = 4;
	
	public PowerStation createInstance(ElectricPowerSystemSimulation simulation,
			PowerStationParameters parameters){
		this.simulation = simulation;
		this.stationParameters = parameters;
		
		createAndInitializePowerStation();
		
		return station;
	}
	
	private void createAndInitializePowerStation(){
		station = new PowerStation(simulation, stationId++);
		
		for(GeneratorParameters parameters: stationParameters.getGeneratorsParameters()){
			resetGeneratorIdCounter();
			createGeneratorAndItsSystems(parameters);
			initializeGenerator(parameters);
			station.addGenerator(generator);
		}
	}
	
	private void resetGeneratorIdCounter(){
		generatorId = 1;
	}
	
	private void createGeneratorAndItsSystems(GeneratorParameters parameters){
		generator = new Generator();
		controlUnit = createAndInitializeControlUnit(generator);
		regulationUnit = createAndInitializeAstaticRegulationUnit(controlUnit, generator);
	}
	
	private ControlUnit createAndInitializeControlUnit(Generator generator){
		ControlUnit controlUnit = new ControlUnit();
		controlUnit.setElectricPowerSystemSimulation(simulation);
		controlUnit.setCoefficientOfStatism(STANDART_COEFFICIENT_OF_STATISM);
		controlUnit.setGenerator(generator);
		controlUnit.setRequiredFrequency(GlobalConstatnts.STANDART_FREQUENCY);
		
		return controlUnit;
	}
	
	private AstaticRegulatioUnit createAndInitializeAstaticRegulationUnit(ControlUnit controlUnit,
			Generator generator){
		AstaticRegulatioUnit regulationUnit = new AstaticRegulatioUnit();
		regulationUnit.setElectricPowerSystemSimulation(simulation);
		regulationUnit.setControlUnit(controlUnit);
		regulationUnit.setGenerator(generator);
		
		return regulationUnit;
	}
	
	private void initializeGenerator(GeneratorParameters parameters){
		generator.setId(++generatorId);
		generator.setControlUnit(controlUnit);
		generator.setAstaticRegulationUnit(regulationUnit);
		generator.setMinimalTechnologyPower(parameters.getMinimalTechnologyPower());
		generator.setNominalPowerInMW(parameters.getNominalPowerInMW());
	}
}
