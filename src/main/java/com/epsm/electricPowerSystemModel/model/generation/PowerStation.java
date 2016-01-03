package com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.Message;
import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.PowerObject;
import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;
import com.epsm.electricPowerSystemModel.model.dispatch.State;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class PowerStation extends PowerObject{

	private MainControlPanel controlPanel;
	private Map<Integer, Generator> generators;
	private LocalTime currentTime;
	private float currentFrequency;
	private float currentGenerationInMW;
	private PowerStationParameters stationParameters;
	private Generator generatorToAdd;
	private PowerStationState state;
	private Logger logger = LoggerFactory.getLogger(PowerStation.class);
	
	public PowerStation(ElectricPowerSystemSimulation simulation, TimeService timeService, Dispatcher dispatcher,
			Parameters parameters) {
		super(simulation, timeService, dispatcher, parameters);
		
		generators = new HashMap<Integer, Generator>();
		controlPanel = new MainControlPanel(simulation, this);
	}
	
	public float calculateGenerationInMW(){
		getTimeAndFrequencyFromSimulation();
		adjustGenerators();
		setGenerationOnThisStepToZero();
		getTotalGeneratorGeneration();
		prepareStationState();
		
		return currentGenerationInMW;
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
		createStationState();
		prepareGeneratorsStates();
	}
	
	private void createStationState(){
		state = new PowerStationState(id, timeService.getCurrentTime(), currentTime,
				generators.size(), currentFrequency);
	}
	
	private void prepareGeneratorsStates(){
		for(Generator generator: generators.values()){
			prepareGeneratorState(generator);
		}
	}
	
	private void prepareGeneratorState(Generator generator){
		GeneratorState generatorState = generator.getState(); 
		state.addGeneratorState(generatorState);
	}
	
	public void createStationParameters(){
		stationParameters = new PowerStationParameters(id, timeService.getCurrentTime(),
				currentTime, generators.size());
	}
	
	private void createAndSaveParametersForEveryGenerator(){
		for(Generator generator: generators.values()){
			GeneratorParameters parameters = createGeneratorParameters(generator);
			stationParameters.addGeneratorParameters(parameters);
		}
	}
	
	private GeneratorParameters createGeneratorParameters(Generator generator){
		int generatorNumber = generator.getNumber();
		float minimalPower = generator.getMinimalPowerInMW();
		float nominalPower = generator.getNominalPowerInMW(); 
		
		return new GeneratorParameters(generatorNumber, nominalPower, minimalPower);
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
	
	@Override
	public State getState(){
		return state;
	}
	
	public Generator getGenerator(int generatorNumber){
		return generators.get(generatorNumber);
	}
	
	public Collection<Integer> getGeneratorsNumbers(){
		return Collections.unmodifiableSet(generators.keySet());
	}

	@Override
	public void executeCommand(Command command) {
		controlPanel.acceptGenerationSchedule(command);
	}

	@Override
	public float calculatePowerBalance() {
		// TODO Auto-generated method stub
		return 0;
	}
}
