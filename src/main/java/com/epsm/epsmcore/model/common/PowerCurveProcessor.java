package com.epsm.epsmcore.model.common;

import com.epsm.epsmcore.model.simulation.Constants;

import java.time.LocalTime;

public final class PowerCurveProcessor {

	public float getPowerOnTimeInMW(PowerCurve curve, LocalTime requestedTime){
		int requestedHour = requestedTime.getHour();
		float loadOnRequestedHour = curve.getPowerByHoursInMW().get(requestedHour);
		float loadOnNextHour = curve.getPowerByHoursInMW().get(requestedHour == curve.getPowerByHoursInMW().size() - 1 ? 0 : requestedHour + 1);
		long nanosFromStartOfRequestedHour = getNanosFromStartOfRequestedHour(requestedTime);

		return interpolateValuesWithinHour(loadOnRequestedHour, loadOnNextHour, nanosFromStartOfRequestedHour);
	}
	
	private long getNanosFromStartOfRequestedHour(LocalTime requestedTime){
		long minutes = requestedTime.getMinute();
		long seconds = requestedTime.getSecond();
		long nanos = requestedTime.getNano();
		long totalSeconds = seconds + (minutes * 60);
		long totalNanos = nanos + (totalSeconds * Constants.NANOS_IN_SECOND);
		
		return totalNanos;
	}
	
	private float interpolateValuesWithinHour(float loadOnRequestedHour, float loadOnNextHour, long nanosFromStartOfRequestedHour){
		return loadOnRequestedHour + ((float)nanosFromStartOfRequestedHour / Constants.NANOS_IN_HOUR) *
				(loadOnNextHour - loadOnRequestedHour);
	}
}
