package main.java.com.epsm.electricPowerSystemModel.model.generalModel;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.epsm.electricPowerSystemModel.model.consumption.Consumer;
import main.java.com.epsm.electricPowerSystemModel.model.generation.PowerStation;

public class ElectricPowerSystemSimulationImpl implements ElectricPowerSystemSimulation{
	private Set<PowerStation> powerStations;
	private Set<Consumer> powerConsumers;
	private float frequencyInPowerSystem = GlobalConstatnts.STANDART_FREQUENCY;
	private LocalTime currentTimeInSimulation;
	private final float TIME_CONASTNT = 2_000;
	private Logger logger;
	private final int SIMULATION_STEP_IN_NANOS = 100_000_000;
	private final float ACCEPTABLE_FREQUENCY_DELTA = 0.03f;

	public ElectricPowerSystemSimulationImpl() {
		powerStations = new HashSet<PowerStation>();
		powerConsumers = new HashSet<Consumer>();
		currentTimeInSimulation = LocalTime.NOON;
		logger = LoggerFactory.getLogger(ElectricPowerSystemSimulationImpl.class);
	}

	@Override
	public void calculateNextStep() {
		float totalGeneration = calculateTotalGenerationsInMW();
		float totalLoad = calculateTotalLoadInMW();
		
		calculateFrequencyInPowerSystem(totalGeneration, totalLoad);
		changeTimeForStep();
		
		if(isFrequencyLowerThanNormal()){
			logFrequency();
		}
	}
	
	private float calculateTotalGenerationsInMW(){
		float generation = 0;
		
		for(PowerStation station: powerStations){
			generation += station.calculateGenerationInMW();
		}
		
		return generation;
	}
	
	private float calculateTotalLoadInMW(){
		float load = 0;
		
		for(Consumer consumer: powerConsumers){
			load += consumer.calculateCurrentLoadInMW();
		}
		
		return load;
	}
	
	private void calculateFrequencyInPowerSystem(float totalGeneration,
			float totalLoad){
		frequencyInPowerSystem = frequencyInPowerSystem + ((totalGeneration - 
				totalLoad) / TIME_CONASTNT) * ((float)SIMULATION_STEP_IN_NANOS /
				GlobalConstatnts.NANOS_IN_SECOND);
	}
	
	private void changeTimeForStep(){
		currentTimeInSimulation = currentTimeInSimulation.plusNanos(SIMULATION_STEP_IN_NANOS);
	}

	private boolean isFrequencyLowerThanNormal(){
		float delta = Math.abs(GlobalConstatnts.STANDART_FREQUENCY - frequencyInPowerSystem);
		
		if(delta > ACCEPTABLE_FREQUENCY_DELTA){
			return true;
		}else{
			return false;
		}
	}
	
	private void logFrequency(){
		logger.warn("Frqueency is unacceptable: " + frequencyInPowerSystem + " Hz.");
	}
	
	@Override
	public void addPowerStation(PowerStation powerStation) {
		powerStations.add(powerStation);
	}

	@Override
	public void addPowerConsumer(Consumer powerConsumer) {
		powerConsumers.add(powerConsumer);
	}
	
	@Override
	public float getFrequencyInPowerSystem(){
		return frequencyInPowerSystem;
	}
	
	@Override
	public LocalTime getTime(){
		return currentTimeInSimulation;
	}
}
