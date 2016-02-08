package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

import com.epsm.epsmCore.model.generalModel.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Message{
	
	@JsonProperty("powerObjectId")
	protected final long powerObjectId;
	
	@JsonProperty("simulationTimeStamp")
	protected LocalDateTime simulationTimeStamp;
	
	@JsonProperty("realTimeStamp")
	protected LocalDateTime realTimeStamp;
	
	protected StringBuilder stringBuilder;
	protected DecimalFormat numberFormatter;
	
	public Message(long powerObjectId, LocalDateTime realTimeStamp, LocalDateTime simulationTimeStamp){
		if(realTimeStamp == null){
			throw new IllegalArgumentException("Message constructor: realTimeStamp can't be null.");
		}else if(simulationTimeStamp == null){
			throw new IllegalArgumentException("Message constructor: simulationTimeStamp can't be null.");
		}
		
		this.powerObjectId = powerObjectId;
		this.realTimeStamp = realTimeStamp;
		this.simulationTimeStamp = simulationTimeStamp;
		stringBuilder = new StringBuilder();
		numberFormatter = new DecimalFormat("0000.000", Constants.SYMBOLS);
	}

	public long getPowerObjectId(){
		return powerObjectId;
	}
	
	public LocalDateTime getSimulationTimeStamp() {
		return simulationTimeStamp;
	}

	public LocalDateTime getRealTimeStamp() {
		return realTimeStamp;
	}

	public abstract String toString();
}
