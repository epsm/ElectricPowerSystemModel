package com.epsm.electricPowerSystemModel.model.dispatch;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PowerStationState extends Message{
	private float frequency;

	public PowerStationState(long powerObjectId, LocalDateTime realTimeStamp, 
			LocalTime simulationTimeStamp, float frequency) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
		this.frequency = frequency;
	}	

	public float getFrequency() {
		return frequency;
	}

	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("PowerSt. with id ");
		stringBuilder.append(powerObjectId);
		stringBuilder.append(" [time: ");
		stringBuilder.append(simulationTimeStamp.format(timeFormatter));
		stringBuilder.append(" freq.: ");
		stringBuilder.append(timeFormatter.format(frequency));
		stringBuilder.append("Hz");
		stringBuilder.append(" gener.: ");
		stringBuilder.append(generatorsStates);
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
