package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.dispatch.State;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsumerState extends State{
	private float load;
	
	@JsonCreator
	public ConsumerState(
			@JsonProperty("powerObjectId")long powerObjectId,
			@JsonProperty("realTimeStamp")LocalDateTime realTimeStamp,
			@JsonProperty("simulationTimeStamp")LocalTime simulationTimeStamp,
			@JsonProperty("load")float load) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
		this.load = load;
	}

	public float getLoad() {
		return load;
	}
	
	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("Consumer#");
		stringBuilder.append(powerObjectId);
		stringBuilder.append(" [sim.time: ");
		stringBuilder.append(simulationTimeStamp.format(timeFormatter));
		stringBuilder.append(" load MW: ");
		stringBuilder.append(numberFormatter.format(-load));
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
