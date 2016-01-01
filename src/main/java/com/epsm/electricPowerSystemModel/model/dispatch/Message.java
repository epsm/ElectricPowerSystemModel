package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class Message extends Formatting{
	protected long powerObjectId;
	protected LocalTime simulationTimeStamp;
	protected LocalDateTime realTimeStamp;
	
	public Message(long powerObjectId, LocalDateTime realTimeStamp, LocalTime simulationTimeStamp){
		if(realTimeStamp == null){
			throw new DispatchingException("Message constructor: realTimeStamp can't be null.");
		}else if(simulationTimeStamp == null){
			throw new DispatchingException("Message constructor: simulationTimeStamp can't be null.");
		}
		
		this.powerObjectId = powerObjectId;
		this.realTimeStamp = realTimeStamp;
		this.simulationTimeStamp = simulationTimeStamp;
	}

	public long getPowerObjectId(){
		return powerObjectId;
	}
	
	public LocalTime getSimulationTimeStamp() {
		return simulationTimeStamp;
	}

	public LocalDateTime getRealTimeStamp() {
		return realTimeStamp;
	}

	public abstract String toString();
}
