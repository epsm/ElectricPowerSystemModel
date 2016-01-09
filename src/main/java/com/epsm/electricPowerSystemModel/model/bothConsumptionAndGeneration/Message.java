package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.epsm.electricPowerSystemModel.model.generalModel.Constants;

public abstract class Message{
	protected long powerObjectId;
	protected LocalTime simulationTimeStamp;
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
