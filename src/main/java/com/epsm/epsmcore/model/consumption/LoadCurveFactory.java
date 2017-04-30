package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.PowerCurve;
import com.epsm.epsmcore.model.simulation.Constants;

import java.util.*;

public class LoadCurveFactory {

	private Random random = new Random();
	private List<Float> newLoadByHoursInMW = new ArrayList<>(Constants.DETERMINED_HOURS_IN_DAY);
	private List<Float> originalLoadByHoursInPercent;
	private float maxLoadWithoutRandomInMW;
	private float randomFluctuatonInPercent;
	
	public PowerCurve getRandomCurve(List<Float> originalLoadByHoursInPercent,
	                                 float MaxLoadWithoutRandomInMW, float randomFluctuationInPercent){
		
		this.originalLoadByHoursInPercent = originalLoadByHoursInPercent;
		this.maxLoadWithoutRandomInMW = MaxLoadWithoutRandomInMW;
		this.randomFluctuatonInPercent = randomFluctuationInPercent;
		
		fillNewLoadByHoursInMW();
		
		return new PowerCurve(Collections.unmodifiableList(newLoadByHoursInMW));
	}
	
	private void fillNewLoadByHoursInMW(){
		for(int hour = 0; hour < Constants.DETERMINED_HOURS_IN_DAY ; hour++){
			newLoadByHoursInMW.add(calculateLoadForHourInMW(hour));
		}
	}
	
	private float calculateLoadForHourInMW(int hour){
		float basePartInMW = calculateBasePartInMW(hour);
		float randomPartInMW = calculateRandomPartInMW(basePartInMW);
		
		return Math.abs(basePartInMW + randomPartInMW);
	}
	
	private float calculateBasePartInMW(int hour){
		float baseComponentInPercent = originalLoadByHoursInPercent.get(hour);
		
		return baseComponentInPercent * maxLoadWithoutRandomInMW / 100;
	}
	
	private float calculateRandomPartInMW(float baseComponentInMW){
		float randomPartInPercent = calculateRandomPartInPercent();
		
		return randomPartInPercent * baseComponentInMW / 100;
	}
	
	private float calculateRandomPartInPercent(){
		boolean isNegative = random.nextBoolean();
		float randomPart = random.nextFloat() * randomFluctuatonInPercent;
		randomPart = (isNegative) ? -randomPart : randomPart;

		return randomPart;
	}
}
