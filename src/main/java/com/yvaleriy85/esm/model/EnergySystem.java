package main.java.com.yvaleriy85.esm.model;

import java.util.ArrayList;
import java.util.List;

public class EnergySystem{

	private List<PowerStation> powerStations = new ArrayList<PowerStation>();
	private List<PowerConsumer> powerConsumers = new ArrayList<PowerConsumer>();
	private static float frequencyInPowerSystem;
	private final float TIME_CONASTNT = 20;
	private final int NANOS_IN_SECOND = (int)Math.pow(10, 9);
	
	public static float getFrequencyInPowerSystem(){
		return frequencyInPowerSystem;
	}

	public void calculateNextStep() {
		float totalGenerations = calculateTotalGenerationsInMW();
		float totalConsumption = calculateTotalConsumptionsInMW();
		frequencyInPowerSystem = frequencyInPowerSystem + (totalGenerations - totalConsumption) /
				(TIME_CONASTNT * Simulation.SIMULATION_STEP_IN_NANOS / NANOS_IN_SECOND);
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

	public void addPowerStation(PowerStation powerStation) {
		powerStations.add(powerStation);
	}

	public void addPowerConsumer(PowerConsumer powerConsumer) {
		powerConsumers.add(powerConsumer);
	}
	
	
}
