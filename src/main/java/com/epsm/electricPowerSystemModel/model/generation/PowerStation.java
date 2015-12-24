package main.java.com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.epsm.electricPowerSystemModel.model.dispatch.GeneratorParameters;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.GeneratorState;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.PowerStationParameters;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.PowerStationState;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;

public class PowerStation{
	private ElectricPowerSystemSimulation simulation;
	private int number;//must not be changed after creation
	private Map<Integer, Generator> generators;
	private LocalTime currentTime;
	private float currentFrequency;
	private float currentGenerationInMW;
	private Set<GeneratorState> generatorsStates;
	private PowerStationParameters stationParameters;
	private Map<Integer, GeneratorParameters> generatorParameters;
	private Generator generatorToAdd;
	private PowerStationState state;
	private Logger logger = LoggerFactory.getLogger(PowerStation.class);
	
	public PowerStation(int number, ElectricPowerSystemSimulation simulation) {
		this.number = number;
		this.simulation = simulation;
		
		generators = new HashMap<Integer, Generator>();
		generatorsStates = new TreeSet<GeneratorState>();
		
		logger.info("Power station #{} was created.", number);
	}

	public float calculateGenerationInMW(){
		getTimeAndFrequencyFromSimulation();
		resetPreviousGeneration();
		getTotalGeneratorGeneration();
		prepareStationState();
		
		return currentGenerationInMW;
	}
	
	private void getTimeAndFrequencyFromSimulation(){
		currentTime = simulation.getTime();
		currentFrequency = simulation.getFrequencyInPowerSystem();	
	}
	
	private void getTotalGeneratorGeneration(){
		for(Generator generator: generators.values()){
			currentGenerationInMW += generator.calculateGeneration();
		}
	}
	
	private void resetPreviousGeneration(){
		currentGenerationInMW = 0;
	}
	
	private void prepareStationState(){
		clearPreviousGeneratorsStates();
		prepareGeneratorsStates();
		createStationState();
	}
	
	private void clearPreviousGeneratorsStates(){
		generatorsStates.clear();
	}
	
	private void prepareGeneratorsStates(){
		for(Generator generator: generators.values()){
			addGeneratorState(generator);
		}
	}
	
	private void addGeneratorState(Generator generator){
		GeneratorState state =generator.getState(); 
		generatorsStates.add(state);
	}
	
	private void createStationState(){
		state = new PowerStationState(number, currentTime, currentFrequency, generatorsStates);
	}
	
	public PowerStationParameters getPowerStationParameters(){
		createNewContainerForGeneratorsParameters();
		createAndSaveParametersForEveryGenerator();
		createStationParameters();

		return stationParameters;
	}
	
	private void createNewContainerForGeneratorsParameters(){
		generatorParameters = new HashMap<Integer, GeneratorParameters>();
	}
	
	private void createAndSaveParametersForEveryGenerator(){
		for(Generator generator: generators.values()){
			int generatorNumber = generator.getNumber();
			GeneratorParameters parameters = getGeneratorParameters(generator);
			generatorParameters.put(generatorNumber, parameters);
		}
	}
	
	private GeneratorParameters getGeneratorParameters(Generator generator){
		int generatorNumber = generator.getNumber();
		float minimalPower = generator.getMinimalPowerInMW();
		float nominalPower = generator.getNominalPowerInMW(); 
		
		return new GeneratorParameters(generatorNumber, nominalPower, minimalPower);
	}
	
	public void createStationParameters(){
		stationParameters = new PowerStationParameters(number, generatorParameters);
	}
	
	public void addGenerator(Generator generator){
		this.generatorToAdd = generator;
		verifyIsGeneratorNotNull();
		verifyIfGeneartorWithTheSameNumberExists();
		addGenerator();
	}
	
	private void verifyIsGeneratorNotNull(){
		if(generatorToAdd == null){
			String message = "Generator must not be null.";
			throw new PowerStationException(message);
		}
	}
	
	private void verifyIfGeneartorWithTheSameNumberExists(){
		int generatorNumber = generatorToAdd.getNumber();
		Generator existingGenerator = generators.get(generatorNumber);
		
		if(existingGenerator != null){
			String message = "Generator with number " + generatorNumber + " already installed.";
			throw new PowerStationException(message);
		}
	}
	
	private void addGenerator(){
		int generatorNumber = generatorToAdd.getNumber();
		generators.put(generatorNumber, generatorToAdd);
	}
	
	public PowerStationState getState(){
		return state;
	}
	
	public Generator getGenerator(int generatorNumber){
		return generators.get(generatorNumber);
	}
	
	public Collection<Integer> getGeneratorsNumbers(){
		return generators.keySet();
	}

	public int getNumber(){
		return number;
	}
}
