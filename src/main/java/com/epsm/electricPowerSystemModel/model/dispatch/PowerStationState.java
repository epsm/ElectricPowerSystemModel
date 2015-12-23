package main.java.com.epsm.electricPowerSystemModel.model.dispatch;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

public class PowerStationState extends PowerObjectState{
	private int powerStationNumber;
	private LocalTime timeStamp;
	private float frequency;
	private Set<GeneratorState> generatorsStatesReports;
	private StringBuilder stringBuilder;
	private DateTimeFormatter timeFormatter;
	private DecimalFormat numberFormatter;

	public PowerStationState(int powerStationNumber, LocalTime timeStamp, float frequency,
			Set<GeneratorState> generatorsStatesReports) {
		this.powerStationNumber = powerStationNumber;
		this.timeStamp = timeStamp;
		this.frequency = frequency;
		this.generatorsStatesReports = Collections.unmodifiableSet(generatorsStatesReports);
		
		stringBuilder = new StringBuilder();
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		numberFormatter = new DecimalFormat("0000.000");
	}

	public int getPowerStationNumber() {
		return powerStationNumber;
	}
	
	public LocalTime getTimeStamp() {
		return timeStamp;
	}

	public float getFrequency() {
		return frequency;
	}

	public Set<GeneratorState> getGeneratorsStatesReports() {
		return generatorsStatesReports;
	}

	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("PowerStationState ");
		stringBuilder.append("[time in simulation= ");
		stringBuilder.append(timeStamp.format(timeFormatter));
		stringBuilder.append(", powerStationNumber=");
		stringBuilder.append(powerStationNumber);
		stringBuilder.append(", frequency=");
		stringBuilder.append(numberFormatter.format(frequency));
		stringBuilder.append(", generatorsStatesReports ");
		stringBuilder.append(generatorsStatesReports);
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}