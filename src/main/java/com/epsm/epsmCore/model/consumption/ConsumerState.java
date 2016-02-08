package com.epsm.epsmCore.model.consumption;

import java.time.LocalDateTime;

import com.epsm.epsmCore.model.dispatch.State;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsumerState extends State{
	
	@JsonProperty("loadInMW")
	private float loadInMW;
	
	@JsonCreator
	public ConsumerState(
			@JsonProperty("powerObjectId") long powerObjectId,
			@JsonProperty("realTimeStamp") LocalDateTime realTimeStamp,
			@JsonProperty("simulationTimeStamp") LocalDateTime simulationTimeStamp,
			@JsonProperty("loadInMW") float loadInMW) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
		this.loadInMW = loadInMW;
	}

	public float getLoadInMW() {
		return loadInMW;
	}
	
	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("Consumer#");
		stringBuilder.append(powerObjectId);
		stringBuilder.append(" [sim.time: ");
		stringBuilder.append(simulationTimeStamp.toString());
		stringBuilder.append(" realtime: ");
		stringBuilder.append(realTimeStamp.toString());
		stringBuilder.append(", load MW: ");
		stringBuilder.append(numberFormatter.format(loadInMW));
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
