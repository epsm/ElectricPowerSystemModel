package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import java.time.LocalTime;
import java.util.Arrays;

import com.epsm.epsmCore.model.generalModel.Constants;
import com.epsm.epsmCore.model.generation.GenerationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class LoadCurve{
	
	@JsonProperty("loadByHoursInMW")
	private float[] loadByHoursInMW;
	
	private LocalTime requestedTime;
	private float loadOnRequestedHour;
	private float loadOnNextHour;
	private int requestedHour;
	private int nextHour;
	private float nanosFromStartOfRequestedHour;
	
	@JsonCreator
	public LoadCurve(@JsonProperty("loadByHoursInMW") float[] loadByHoursInMW){
		if(loadByHoursInMW == null){
			String message = "LoadCurve constructor: loadByHoursInMW must not be null.";
			throw new GenerationException(message);
		}else if(loadByHoursInMW.length != Constants.DETERMINED_HOURS_IN_DAY){
			String message = String.format("Incoming array length must be %s.", Constants.DETERMINED_HOURS_IN_DAY);
			throw new GenerationException(message);
		}
		
		this.loadByHoursInMW = loadByHoursInMW;
	}
	
	public float getPowerOnTimeInMW(LocalTime time){
		saveTime(time);
		doCalculations();
		
		return interpolateValuesWithinHour();
	}
	
	private void saveTime(LocalTime time){
		this.requestedTime = time;
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
		long totalSeconds = seconds + (minutes * 60);
		long totalNanos = nanos + (totalSeconds * Constants.NANOS_IN_SECOND);
		
		return totalNanos;
	}
	
	private float interpolateValuesWithinHour(){
		return loadOnRequestedHour + (nanosFromStartOfRequestedHour / Constants.NANOS_IN_HOUR) *
				(loadOnNextHour - loadOnRequestedHour);
	}
	
	public String toString(){
		return String.format("<LoadCurve: load in MW on day by hours, starts on 00.00: %s>",
				Arrays.toString(loadByHoursInMW));
	}
}
