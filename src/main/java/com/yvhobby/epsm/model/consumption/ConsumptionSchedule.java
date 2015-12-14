package main.java.com.yvhobby.epsm.model.consumption;

import java.time.LocalTime;

import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;

public class ConsumptionSchedule{
	private float[] consumptionByHoursInMW;
	private LocalTime requestedTime;
	private float consumptionOnRequestedHour;
	private float consumptionOnNextHour;
	private int requestedHour;
	private int nextHour;
	private float nanosFromStartOfRequestedHour;
	
	public ConsumptionSchedule(float[] consumptionByHoursInMW) {
		this.consumptionByHoursInMW = consumptionByHoursInMW;
	}
	
	public float getConsumptionOnTime(LocalTime time){
		this.requestedTime = time;
		doCalculations();
		
		return interpolateValuesWithinHour();
	}
	
	private void doCalculations(){
		requestedHour = requestedTime.getHour();
		nextHour = requestedTime.plusHours(1).getHour();
		consumptionOnRequestedHour = consumptionByHoursInMW[requestedHour];
		consumptionOnNextHour = consumptionByHoursInMW[nextHour];
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
		return consumptionOnRequestedHour + (nanosFromStartOfRequestedHour / GlobalConstatnts.NANOS_IN_HOUR) *
				(consumptionOnNextHour - consumptionOnRequestedHour);
	}
}
