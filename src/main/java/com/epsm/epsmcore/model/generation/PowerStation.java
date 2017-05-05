package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.PowerObject;
import com.epsm.epsmcore.model.common.PowerObjectStateManager;
import com.epsm.epsmcore.model.dispatch.DispatchedPowerStation;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class PowerStation extends PowerObject<PowerStationParameters, PowerStationState> implements DispatchedPowerStation {

	private StationControlPanel controlPanel;
	private Map<Integer, Generator> generators = new HashMap<>();
	private LocalDateTime currentDateTime;
	private float currentFrequency;
	private float currentGenerationInMW;
	private Generator generatorToAdd;
	private PowerStationState powerStationState;
	private static final Logger logger = LoggerFactory.getLogger(PowerStation.class);
	
	public PowerStation(
			Simulation simulation,
	        Dispatcher dispatcher,
			PowerStationParameters parameters,
			PowerObjectStateManager stateManager) {
		
		super(simulation, dispatcher, parameters, stateManager);
		
		controlPanel = new StationControlPanel(simulation, this);
	}
	
	@Override
	protected float calculatPowerBalance() {
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
						id, currentDateTime, currentFrequency, generator.getGeneratorNumber(),
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
		powerStationState = new PowerStationState(id, currentDateTime, currentFrequency);
	}
	
	private void prepareGeneratorsStates(){
		for(Generator generator: generators.values()){
			powerStationState.getStates().put(generator.getGeneratorNumber(), generator.getState());
		}
	}
	
	public void addGenerator(Generator generator){
		this.generatorToAdd = generator;
		verifyIsGeneratorNotNull();
		verifyIfGeneartorWithTheSameNumberExists();
		addGenerator();
		logger.info("Generator #{} added to power station", generator.getGeneratorNumber());
	}
	
	private void verifyIsGeneratorNotNull(){
		if(generatorToAdd == null){
			String message = "Generator must not be null.";
			throw new IllegalArgumentException(message);
		}
	}
	
	private void verifyIfGeneartorWithTheSameNumberExists(){
		int generatorNumber = generatorToAdd.getGeneratorNumber();
		Generator existingGenerator = generators.get(generatorNumber);
		
		if(existingGenerator != null){
			String message = String.format("Generator#%d already installed.",
					generatorNumber);
			throw new GenerationException(message);
		}
	}
	
	private void addGenerator(){
		int generatorNumber = generatorToAdd.getGeneratorNumber();
		generators.put(generatorNumber, generatorToAdd);
	}

	@Override
	public PowerStationState getState(){
		return powerStationState;
	}
	
	public Generator getGenerator(int generatorNumber){
		return generators.get(generatorNumber);
	}
	
	public Collection<Integer> getGeneratorsNumbers(){
		return Collections.unmodifiableSet(generators.keySet());
	}

	@Override
	public void executeSchedule(PowerStationGenerationSchedule schedule) {
		controlPanel.acceptGenerationSchedule(schedule);
	}
}
