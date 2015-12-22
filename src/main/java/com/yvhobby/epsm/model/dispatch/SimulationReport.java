package main.java.com.yvhobby.epsm.model.dispatch;

import java.time.LocalTime;

public class SimulationReport {
	private float totalGeneration;
	private float totalLoad;
	private float frequencyInPowerSystem;
	private LocalTime timeStamp;
	
	public SimulationReport(float totalGeneration, float totalLoad,
			float frequencyInPowerSystem, LocalTime timeStamp) {
		this.totalGeneration = totalGeneration;
		this.totalLoad = totalLoad;
		this.frequencyInPowerSystem = frequencyInPowerSystem;
		this.timeStamp = timeStamp;
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
		return timeStamp;
	}
}
