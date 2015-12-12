package main.java.com.yvaleriy85.esm.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class ElectricPowerSystemSimulation{

	private List<PowerStation> powerStations;
	private List<PowerConsumer> powerConsumers;
	private float frequencyInPowerSystem;
	private LocalTime currentTimeInSimulation;
	private final float TIME_CONASTNT = 20;
	private final int NANOS_IN_SECOND = (int)Math.pow(10, 9);
	private final int SIMULATION_STEP_IN_NANOS = (int)Math.pow(10, 8);
	
	public ElectricPowerSystemSimulation() {
		powerStations = new ArrayList<PowerStation>();
		powerConsumers = new ArrayList<PowerConsumer>();
		currentTimeInSimulation = LocalTime.of(0, 0);
	}

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
		
		for(PowerConsumer station: powerConsumers){
			consumption += station.getCurrentConsumptionInMW();
		}
		
		return consumption;
	}
	
	private void calculateFrequencyInPowerSystem(float totalGenerations,
			float totalConsumption){
		frequencyInPowerSystem = frequencyInPowerSystem + ((totalGenerations - 
				totalConsumption) /	TIME_CONASTNT) * ((float)SIMULATION_STEP_IN_NANOS /
				NANOS_IN_SECOND);
	}
	
	private void changeTimeForStep(){
		currentTimeInSimulation = currentTimeInSimulation.
				plusNanos(SIMULATION_STEP_IN_NANOS);
	}
	
	public void addPowerStation(PowerStation powerStation) {
		powerStations.add(powerStation);
	}

	public void addPowerConsumer(PowerConsumer powerConsumer) {
		powerConsumers.add(powerConsumer);
	}
	
	public float getFrequencyInPowerSystem(){
		return frequencyInPowerSystem;
	}
	
	public LocalTime getTime(){
		return currentTimeInSimulation;
	}
	
	public int getSimulationStepInNanos(){
		return SIMULATION_STEP_IN_NANOS;
	}
}
