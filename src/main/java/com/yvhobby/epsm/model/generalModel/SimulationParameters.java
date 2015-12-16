package main.java.com.yvhobby.epsm.model.generalModel;

import java.time.LocalTime;

public class SimulationParameters {
	private float totalGeneration;
	private float totalLoad;
	private float frequencyInPowerSystem;
	private LocalTime currentTimeInSimulation;
	
	public SimulationParameters(float totalGeneration, float totalLoad,
			float frequencyInPowerSystem, LocalTime currentTimeInSimulation) {
		this.totalGeneration = totalGeneration;
		this.totalLoad = totalLoad;
		this.frequencyInPowerSystem = frequencyInPowerSystem;
		this.currentTimeInSimulation = currentTimeInSimulation;
	}

	public float getTotalGeneration() {
		return totalGeneration;
	}

	public float getTotalLoad() {
		return totalLoad;
	}

	public float getFrequencyInPowerSystem() {
		return frequencyInPowerSystem;
	}

	public LocalTime getCurrentTimeInSimulation() {
		return currentTimeInSimulation;
	}
}
