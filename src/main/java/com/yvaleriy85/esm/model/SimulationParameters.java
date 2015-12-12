package main.java.com.yvaleriy85.esm.model;

import java.time.LocalTime;

public class SimulationParameters {
	private float totalGenerations;
	private float totalConsumption;
	private float frequencyInPowerSystem;
	private LocalTime currentTimeInSimulation;
	
	public SimulationParameters(float totalGenerations, float totalConsumption,
			float frequencyInPowerSystem, LocalTime currentTimeInSimulation) {
		this.totalGenerations = totalGenerations;
		this.totalConsumption = totalConsumption;
		this.frequencyInPowerSystem = frequencyInPowerSystem;
		this.currentTimeInSimulation = currentTimeInSimulation;
	}

	public float getTotalGenerations() {
		return totalGenerations;
	}

	public float getTotalConsumption() {
		return totalConsumption;
	}

	public float getFrequencyInPowerSystem() {
		return frequencyInPowerSystem;
	}

	public LocalTime getCurrentTimeInSimulation() {
		return currentTimeInSimulation;
	}
}
