package main.java.com.yvhobby.epsm.model.dispatch;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SimulationReport extends Report{
	private float totalGeneration;
	private float totalLoad;
	private float frequencyInPowerSystem;
	private LocalTime timeStamp;
	private StringBuilder stringBuilder;
	private DecimalFormat loadFormatter;
	private DateTimeFormatter timeFormatter;
	
	public SimulationReport(float totalGeneration, float totalLoad,
			float frequencyInPowerSystem, LocalTime timeStamp) {
		this.totalGeneration = totalGeneration;
		this.totalLoad = totalLoad;
		this.frequencyInPowerSystem = frequencyInPowerSystem;
		this.timeStamp = timeStamp;
		
		loadFormatter = new DecimalFormat("0000.000");
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
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

	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("SimulationReport ");
		stringBuilder.append("[time in simulation= ");
		stringBuilder.append(timeStamp.format(timeFormatter));
		stringBuilder.append(", load=");
		stringBuilder.append(loadFormatter.format(totalLoad));
		stringBuilder.append(", generation=");
		stringBuilder.append(loadFormatter.format(totalGeneration));
		stringBuilder.append(", frequency=");
		stringBuilder.append(loadFormatter.format(frequencyInPowerSystem));
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
