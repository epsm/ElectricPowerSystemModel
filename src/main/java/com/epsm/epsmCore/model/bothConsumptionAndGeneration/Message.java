package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.epsm.epsmCore.model.generalModel.Constants;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public abstract class Message{
	protected long powerObjectId;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	protected LocalDateTime simulationTimeStamp;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	protected LocalDateTime realTimeStamp;
	
	protected StringBuilder stringBuilder;
	protected DecimalFormat numberFormatter;
	protected DateTimeFormatter timeFormatter;
	
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
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
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