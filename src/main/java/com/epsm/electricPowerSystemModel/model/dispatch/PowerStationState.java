package com.epsm.electricPowerSystemModel.model.dispatch;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PowerStationState extends PowerObjectState{
	private int powerStationNumber;
	private LocalTime timeStamp;
	private float frequency;
	private Set<GeneratorState> generatorsStates;
	private StringBuilder stringBuilder;
	private DateTimeFormatter timeFormatter;
	private DecimalFormat numberFormatter;

	@JsonCreator
	public PowerStationState(
			@JsonProperty("powerStationNumber") int powerStationNumber,
			@JsonProperty("timeStamp") LocalTime timeStamp,
			@JsonProperty("frequency") float frequency,
			@JsonProperty("generatorsStates") Set<GeneratorState> generatorsStates) {
		
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
		stringBuilder.append("PowerSt. №");
		stringBuilder.append(powerStationNumber);
		stringBuilder.append(" [time: ");
		stringBuilder.append(timeStamp.format(timeFormatter));
		stringBuilder.append(" freq.: ");
		stringBuilder.append(numberFormatter.format(frequency));
		stringBuilder.append("Hz");
		stringBuilder.append(" gener.: ");
		stringBuilder.append(generatorsStates);
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
