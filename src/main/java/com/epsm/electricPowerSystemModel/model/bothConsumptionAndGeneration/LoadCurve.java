package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.generalModel.Constants;
import com.epsm.electricPowerSystemModel.model.generation.GenerationException;

public class LoadCurve{
	private float[] loadByHoursInMW;
	private LocalTime requestedTime;
	private float loadOnRequestedHour;
	private float loadOnNextHour;
	private int requestedHour;
	private int nextHour;
	private float nanosFromStartOfRequestedHour;
	
	public LoadCurve(float[] loadByHoursInMW){
		if(loadByHoursInMW == null){
			String message = "There is null instead incoming values.";
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
}
