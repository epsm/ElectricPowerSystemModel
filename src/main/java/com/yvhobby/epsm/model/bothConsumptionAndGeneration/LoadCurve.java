package main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration;

import java.time.LocalTime;

import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;

public class LoadCurve{
	private float[] loadByHoursInMW;
	private LocalTime requestedTime;
	private float loadOnRequestedHour;
	private float loadOnNextHour;
	private int requestedHour;
	private int nextHour;
	private float nanosFromStartOfRequestedHour;
	
	public LoadCurve(float[] loadByHoursInMW) {
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
		return loadOnRequestedHour + (nanosFromStartOfRequestedHour / GlobalConstatnts.NANOS_IN_HOUR) *
				(loadOnNextHour - loadOnRequestedHour);
	}
}
