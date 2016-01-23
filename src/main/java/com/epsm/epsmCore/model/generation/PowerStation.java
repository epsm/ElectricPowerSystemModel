package com.epsm.epsmCore.model.generation;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.PowerObject;
import com.epsm.epsmCore.model.dispatch.Command;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.State;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.TimeService;

public final class PowerStation extends PowerObject{

	private MainControlPanel controlPanel;
	private Map<Integer, Generator> generators;
	private LocalDateTime currentDateTime;
	private float currentFrequency;
	private float currentGenerationInMW;
	private Generator generatorToAdd;
	private PowerStationState state;
	private Logger logger;
	
	public PowerStation(ElectricPowerSystemSimulation simulation, TimeService timeService,
			Dispatcher dispatcher, PowerStationParameters parameters) {
		
		super(simulation, timeService, dispatcher, parameters);
		
		generators = new HashMap<Integer, Generator>();
		controlPanel = new MainControlPanel(simulation, this);
		logger = LoggerFactory.getLogger(PowerStation.class);
	}
	
	@Override
	protected float calculateCurrentPowerBalance() {
		calculateGenerationInMW();
		prepareStationState();
		
		return currentGenerationInMW;
	}
	
	private void calculateGenerationInMW(){
		getTimeAndFrequencyFromSimulation();
		adjustGenerators();
		setGenerationOnThisStepToZero();
		getTotalGeneratorGeneration();
	}
	
	private void getTimeAndFrequencyFromSimulation(){
		currentDateTime = simulation.getDateTimeInSimulation();
		currentFrequency = simulation.getFrequencyInPowerSystem();	
	}
	
	private void adjustGenerators(){
		controlPanel.adjustGenerators();
	}
	
	private void getTotalGeneratorGeneration(){
		for(Generator generator: generators.values()){
			float generatorGeneration = generator.calculateGeneration();
			currentGenerationInMW += generatorGeneration;
			
			if(isItExactlyMinute(currentDateTime)){
				logger.debug("State:  st.#{}, sim.time: {}, freq.: {}, generator#{} gen: {} MW,"
						+ " power at req. freq.: {}, ast.reg.On: {}",
						id, currentDateTime, currentFrequency, generator.getNumber(),
						generatorGeneration, generator.getPowerAtRequiredFrequency(),
						generator.isAstaticRegulationTurnedOn());
			}
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
		state = new PowerStationState(id, timeService.getCurrentDateTime(), currentDateTime,
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
	
	public void addGenerator(Generator generator){
		this.generatorToAdd = generator;
		verifyIsGeneratorNotNull();
		verifyIfGeneartorWithTheSameNumberExists();
		addGenerator();
		logger.info("Generator #{} added to power station", generator.getNumber());
	}
	
	private void verifyIsGeneratorNotNull(){
		if(generatorToAdd == null){
			String message = "Generator must not be null.";
			throw new IllegalArgumentException(message);
		}
	}
	
	private void verifyIfGeneartorWithTheSameNumberExists(){
		int generatorNumber = generatorToAdd.getNumber();
		Generator existingGenerator = generators.get(generatorNumber);
		
		if(existingGenerator != null){
			String message = String.format("Generator#%d already installed.",
					generatorNumber);
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
	protected void performDispatcherCommand(Command command) {
		controlPanel.acceptGenerationSchedule((PowerStationGenerationSchedule) command);
	}
}