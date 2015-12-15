package main.java.com.yvhobby.epsm.model.generalModel;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;

import main.java.com.yvhobby.epsm.model.consumption.PowerConsumer;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;

public class ElectricPowerSystemSimulationImpl implements ElectricPowerSystemSimulation{

	private Collection<PowerStation> powerStations;
	private Collection<PowerConsumer> powerConsumers;
	private float frequencyInPowerSystem = GlobalConstatnts.STANDART_FREQUENCY;
	private LocalTime currentTimeInSimulation;
	private final float TIME_CONASTNT = 20;
	private final int SIMULATION_STEP_IN_NANOS = 100_000_000;
	
	public ElectricPowerSystemSimulationImpl() {
		powerStations = new HashSet<PowerStation>();
		powerConsumers = new HashSet<PowerConsumer>();
		currentTimeInSimulation = LocalTime.of(0, 0);
	}

	@Override
	public SimulationParameters calculateNextStep() {
		float totalGenerations = calculateTotalGenerationsInMW();
		float totalConsumption = calculateTotalConsumptionsInMW();
		
		calculateFrequencyInPowerSystem(totalGenerations, totalConsumption);
		changeTimeForStep();
		
		return new SimulationParameters(totalGenerations, totalConsumption,
				frequencyInPowerSystem, currentTimeInSimulation);
	}
	
	private float calculateTotalGenerationsInMW(){
		float generations = 0;
		
		for(PowerStation station: powerStations){
			generations += station.getCurrentGenerationInMW();
		}
		
		return generations;
	}
	
	private float calculateTotalConsumptionsInMW(){
		float consumption = 0;
		
		for(PowerConsumer consumer: powerConsumers){
			consumption += consumer.getCurrentConsumptionInMW();
		}
		
		return consumption;
	}
	
	private void calculateFrequencyInPowerSystem(float totalGenerations,
			float totalConsumption){
		frequencyInPowerSystem = frequencyInPowerSystem + ((totalGenerations - 
				totalConsumption) /	TIME_CONASTNT) * ((float)SIMULATION_STEP_IN_NANOS /
				GlobalConstatnts.NANOS_IN_SECOND);
	}
	
	private void changeTimeForStep(){
		currentTimeInSimulation = currentTimeInSimulation.
				plusNanos(SIMULATION_STEP_IN_NANOS);
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
