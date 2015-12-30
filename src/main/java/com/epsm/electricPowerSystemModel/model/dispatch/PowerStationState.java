package com.epsm.electricPowerSystemModel.model.dispatch;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PowerStationState extends PowerObjectState{
	private LocalTime timeStamp;
	private float frequency;
	private Set<GeneratorState> generatorsStates;
	private StringBuilder stringBuilder;

	@JsonCreator
	public PowerStationState(
			@JsonProperty("powerObjectId") long powerObjectId,
			@JsonProperty("timeStamp") LocalTime timeStamp,
			@JsonProperty("frequency") float frequency,
			@JsonProperty("generatorsStates") Set<GeneratorState> generatorsStates) {
		
		super(powerObjectId);
		this.timeStamp = timeStamp;
		this.frequency = frequency;
		this.generatorsStates = Collections.unmodifiableSet(generatorsStates);
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
		stringBuilder.append("PowerSt. with id ");
		stringBuilder.append(powerObjectId);
		stringBuilder.append(" [time: ");
		stringBuilder.append(timeStamp.format(timeFormatter));
		stringBuilder.append(" freq.: ");
		stringBuilder.append(timeFormatter.format(frequency));
		stringBuilder.append("Hz");
		stringBuilder.append(" gener.: ");
		stringBuilder.append(generatorsStates);
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
