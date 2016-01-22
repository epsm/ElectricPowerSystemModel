package com.epsm.epsmCore.model.consumption;

import java.time.LocalDateTime;

import com.epsm.epsmCore.model.dispatch.State;
import com.epsm.epsmCore.model.utils.json.ConsumerStateJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ConsumerStateJsonDeserializer.class)
public class ConsumerState extends State{
	private float load;
	
	public ConsumerState(long powerObjectId, LocalDateTime realTimeStamp,
			LocalDateTime simulationTimeStamp, float load) {
		
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
		stringBuilder.append(" realtime: ");
		stringBuilder.append(realTimeStamp.toString());
		stringBuilder.append(", load MW: ");
		stringBuilder.append(numberFormatter.format(-load));
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
