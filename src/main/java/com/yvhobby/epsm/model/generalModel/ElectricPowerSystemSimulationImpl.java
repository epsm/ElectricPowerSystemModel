package main.java.com.yvhobby.epsm.model.generalModel;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import main.java.com.yvhobby.epsm.model.consumption.PowerConsumer;
import main.java.com.yvhobby.epsm.model.dispatch.SimulationReport;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;

public class ElectricPowerSystemSimulationImpl implements ElectricPowerSystemSimulation{

	private Set<PowerStation> powerStations;
	private Set<PowerConsumer> powerConsumers;
	private float frequencyInPowerSystem = GlobalConstatnts.STANDART_FREQUENCY;
	private LocalTime currentTimeInSimulation;
	private final float TIME_CONASTNT = 2_000;
	private final int SIMULATION_STEP_IN_NANOS = 100_000_000;
	
	public ElectricPowerSystemSimulationImpl() {
		powerStations = new HashSet<PowerStation>();
		powerConsumers = new HashSet<PowerConsumer>();
		currentTimeInSimulation = LocalTime.NOON;
	}

	@Override
	public SimulationReport calculateNextStep() {
		float totalGeneration = calculateTotalGenerationsInMW();
		float totalLoad = calculateTotalLoadInMW();
		
		calculateFrequencyInPowerSystem(totalGeneration, totalLoad);
		changeTimeForStep();
		
		return new SimulationReport(totalGeneration, totalLoad,
				frequencyInPowerSystem, currentTimeInSimulation);
	}
	
	private float calculateTotalGenerationsInMW(){
		float generation = 0;
		
		for(PowerStation station: powerStations){
			generation += station.getCurrentGenerationInMW();
		}
		
		return generation;
	}
	
	private float calculateTotalLoadInMW(){
		float load = 0;
		
		for(PowerConsumer consumer: powerConsumers){
			load += consumer.getCurrentLoadInMW();
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
	
	@Override
	public void addPowerStation(PowerStation powerStation) {
		powerStations.add(powerStation);
	}

	@Override
	public void addPowerConsumer(PowerConsumer powerConsumer) {
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
