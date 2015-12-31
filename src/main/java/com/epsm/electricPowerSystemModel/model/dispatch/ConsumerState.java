package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConsumerState extends Message{
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
		stringBuilder.append("Consumer with id ");
		stringBuilder.append(powerObjectId);
		stringBuilder.append(" [time: ");
		stringBuilder.append(simulationTimeStamp.format(timeFormatter));
		stringBuilder.append(" load MW: ");
		stringBuilder.append(numberFormatter.format(load));
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
