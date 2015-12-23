package main.java.com.epsm.electricPowerSystemModel.model.dispatch;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

import main.java.com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;

public class PowerStationState extends PowerObjectState{
	private int powerStationNumber;
	private LocalTime timeStamp;
	private float frequency;
	private Set<GeneratorState> generatorsStates;
	private StringBuilder stringBuilder;
	private DateTimeFormatter timeFormatter;
	private DecimalFormat numberFormatter;

	public PowerStationState(int powerStationNumber, LocalTime timeStamp, float frequency,
			Set<GeneratorState> generatorsStates) {
		this.powerStationNumber = powerStationNumber;
		this.timeStamp = timeStamp;
		this.frequency = frequency;
		this.generatorsStates = Collections.unmodifiableSet(generatorsStates);
		
		stringBuilder = new StringBuilder();
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		numberFormatter = new DecimalFormat("00.000", GlobalConstatnts.SYMBOLS);
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

	public Set<GeneratorState> getGeneratorsStates() {
		return generatorsStates;
	}

	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("PowerStation ¹");
		stringBuilder.append(powerStationNumber);
		stringBuilder.append(" [time: ");
		stringBuilder.append(timeStamp.format(timeFormatter));
		stringBuilder.append(" frequency: ");
		stringBuilder.append(numberFormatter.format(frequency));
		stringBuilder.append(" generators: ");
		stringBuilder.append(generatorsStates);
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
