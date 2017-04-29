package com.epsm.epsmCore.model.consumption;

import java.util.Arrays;
import java.util.Random;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.PowerCurve;
import com.epsm.epsmCore.model.generalModel.Constants;

public class LoadCurveFactory {
	private Random random = new Random();
	private float[] newLoadByHoursInMW = new float[Constants.DETERMINED_HOURS_IN_DAY];
	private float[] originalLoadByHoursInPercent;
	private float maxLoadWithoutRandomInMW;
	private float randomFluctuatonInPercent;
	
	public PowerCurve getRandomCurve(float[] originalLoadByHoursInPercent,
                                     float MaxLoadWithoutRandomInMW, float randomFluctuationInPercent){
		
		this.originalLoadByHoursInPercent = originalLoadByHoursInPercent;
		this.maxLoadWithoutRandomInMW = MaxLoadWithoutRandomInMW;
		this.randomFluctuatonInPercent = randomFluctuationInPercent;
		
		fillNewLoadByHoursInMW();
		
		return new PowerCurve(Arrays.copyOf(newLoadByHoursInMW, newLoadByHoursInMW.length));
	}
	
	private void fillNewLoadByHoursInMW(){
		for(int i = 0; i < Constants.DETERMINED_HOURS_IN_DAY ; i++){
			newLoadByHoursInMW[i] = calculateLoadForHourInMW(i);
		}
	}
	
	private float calculateLoadForHourInMW(int hour){
		float basePartInMW = calculateBasePartInMW(hour);
		float randomPartInMW = calculateRandomPartInMW(basePartInMW);
		
		return Math.abs(basePartInMW + randomPartInMW);
	}
	
	private float calculateBasePartInMW(int hour){
		float baseComponentInPercent = originalLoadByHoursInPercent[hour];
		
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
