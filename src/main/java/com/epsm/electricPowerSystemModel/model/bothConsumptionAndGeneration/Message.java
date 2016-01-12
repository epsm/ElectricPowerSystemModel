package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.epsm.electricPowerSystemModel.model.generalModel.Constants;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

public abstract class Message{
	protected long powerObjectId;
	
	@JsonSerialize(using = LocalTimeSerializer.class)
	@JsonDeserialize(using = LocalTimeDeserializer.class)
	protected LocalTime simulationTimeStamp;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	protected LocalDateTime realTimeStamp;
	
	protected StringBuilder stringBuilder;
	protected DecimalFormat numberFormatter;
	protected DateTimeFormatter timeFormatter;
	
	public Message(long powerObjectId, LocalDateTime realTimeStamp, LocalTime simulationTimeStamp){
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
	
	public LocalTime getSimulationTimeStamp() {
		return simulationTimeStamp;
	}

	public LocalDateTime getRealTimeStamp() {
		return realTimeStamp;
	}

	public abstract String toString();
}
