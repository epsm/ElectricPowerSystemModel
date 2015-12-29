package com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.dispatch.GeneratorParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.GeneratorState;
import com.epsm.electricPowerSystemModel.model.dispatch.MainControlPanel;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerStationParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerStationState;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;

public class PowerStation{
	private long id;
	private ElectricPowerSystemSimulation simulation;
	private MainControlPanel controlPanel;
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
	
	public PowerStation() {
		generators = new HashMap<Integer, Generator>();
		generatorsStates = new TreeSet<GeneratorState>();
		prepareStationState();
	}

	public float calculateGenerationInMW(){
		verifyControlPanel();
		getTimeAndFrequencyFromSimulation();
		adjustGenerators();
		setGenerationOnThisStepToZero();
		getTotalGeneratorGeneration();
		prepareStationState();
		
		return currentGenerationInMW;
	}
	
	private void verifyControlPanel(){
		if(controlPanel == null){
			throw new GenerationException("Can't calculate generation with null control panel.");
		}
	}
	
	private void getTimeAndFrequencyFromSimulation(){
		currentTime = simulation.getTimeInSimulation();
		currentFrequency = simulation.getFrequencyInPowerSystem();	
	}
	
	private void adjustGenerators(){
		controlPanel.adjustGenerators();
	}
	
	private void getTotalGeneratorGeneration(){
		for(Generator generator: generators.values()){
			currentGenerationInMW += generator.calculateGeneration();
		}
	}
	
	private void setGenerationOnThisStepToZero(){
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
		GeneratorState state = generator.getState(); 
		generatorsStates.add(state);
	}
	
	private void createStationState(){
		state = new PowerStationState(id, currentTime, currentFrequency, generatorsStates);
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
		stationParameters = new PowerStationParameters(id, generatorParameters);
	}
	
	public void addGenerator(Generator generator){
		this.generatorToAdd = generator;
		verifyIsGeneratorNotNull();
		verifyIfGeneartorWithTheSameNumberExists();
		addGenerator();
		logger.info("Generator №{} added to power station", generator.getNumber());
	}
	
	private void verifyIsGeneratorNotNull(){
		if(generatorToAdd == null){
			String message = "Generator must not be null.";
			throw new GenerationException(message);
		}
	}
	
	private void verifyIfGeneartorWithTheSameNumberExists(){
		int generatorNumber = generatorToAdd.getNumber();
		Generator existingGenerator = generators.get(generatorNumber);
		
		if(existingGenerator != null){
			String message = "Generator with number " + generatorNumber + " already installed.";
			throw new GenerationException(message);
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

	public void setSimulation(ElectricPowerSystemSimulation simulation) {
		this.simulation = simulation;
	}
	
	public void setMainControlPanel(MainControlPanel controlPanel){
		if(controlPanel == null){
			throw new GenerationException("Can't add null control panel.");
		}
		
		this.controlPanel = controlPanel;
		id = controlPanel.getId();
	}
}
