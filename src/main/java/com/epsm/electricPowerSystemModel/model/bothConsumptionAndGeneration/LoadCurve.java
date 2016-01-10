package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.time.LocalTime;
import java.util.Arrays;

import com.epsm.electricPowerSystemModel.model.generalModel.Constants;
import com.epsm.electricPowerSystemModel.model.generation.GenerationException;
import com.epsm.electricPowerSystemModel.util.LoadCurveJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = LoadCurveJsonSerializer.class)
public final class LoadCurve{
	private float[] loadByHoursInMW;
	private LocalTime requestedTime;
	private float loadOnRequestedHour;
	private float loadOnNextHour;
	private int requestedHour;
	private int nextHour;
	private float nanosFromStartOfRequestedHour;
	
	public LoadCurve(float[] loadByHoursInMW){
		if(loadByHoursInMW == null){
			String message = "LoadCurve constructor: loadByHoursInMW must not be null.";
			throw new GenerationException(message);
		}else if(loadByHoursInMW.length != 24){
			String message = "Incoming array length must be 24.";
			throw new GenerationException(message);
		}
		
		this.loadByHoursInMW = loadByHoursInMW;
	}
	
	public float getPowerOnTimeInMW(LocalTime time){
		this.requestedTime = time;
		doCalculations();
		
		return interpolateValuesWithinHour();
	}
	
	private void doCalculations(){
		requestedHour = requestedTime.getHour();
		nextHour = requestedTime.plusHours(1).getHour();
		loadOnRequestedHour = loadByHoursInMW[requestedHour];
		loadOnNextHour = loadByHoursInMW[nextHour];
		nanosFromStartOfRequestedHour = getNanosFromStartOfRequestedHour();
	}
	
	private long getNanosFromStartOfRequestedHour(){
		long minutes = requestedTime.getMinute();
		long seconds = requestedTime.getSecond();
		long nanos = requestedTime.getNano();
		long totalSeconds = seconds + minutes * 60;
		long totalNanos = nanos + totalSeconds * 1_000_000_000;
		
		return totalNanos;
	}
	
	private float interpolateValuesWithinHour(){
		return loadOnRequestedHour + (nanosFromStartOfRequestedHour / Constants.NANOS_IN_HOUR) *
				(loadOnNextHour - loadOnRequestedHour);
	}
	
	public String toString(){
		return String.format("LoadCurve: load in MW on day by hours, starts on 00.00: %s.",
				Arrays.toString(loadByHoursInMW));
	}
}
