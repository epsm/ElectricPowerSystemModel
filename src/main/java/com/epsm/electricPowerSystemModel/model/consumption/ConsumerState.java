package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.dispatch.State;

public class ConsumerState extends State{
	private float load;
	
	public ConsumerState(long powerObjectId, LocalDateTime realTimeStamp, LocalTime simulationTimeStamp, 
			float load) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
		this.load = load;
	}

	public float getTotalLoad() {
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
