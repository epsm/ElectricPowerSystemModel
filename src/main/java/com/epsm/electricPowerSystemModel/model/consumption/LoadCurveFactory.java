package com.epsm.electricPowerSystemModel.model.consumption;

import java.util.Arrays;
import java.util.Random;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;

public class LoadCurveFactory {
	private Random random = new Random();
	private float[] newLoadByHoursInMW = new float[24];
	private float[] originalLoadByHoursInPercent;
	private float maxLoadWithoutRandomInMW;
	private float randomFluctuatonInPercent;
	
	public LoadCurve getRandomCurve(float[] originalLoadByHoursInPercent,
			float MaxLoadWithoutRandomInMW, float randomFluctuationInPercent){
		
		this.originalLoadByHoursInPercent = originalLoadByHoursInPercent;
		this.maxLoadWithoutRandomInMW = MaxLoadWithoutRandomInMW;
		this.randomFluctuatonInPercent = randomFluctuationInPercent;
		
		fillNewLoadByHoursInMW();
		
		return new LoadCurve(Arrays.copyOf(newLoadByHoursInMW, newLoadByHoursInMW.length));
	}
	
	private void fillNewLoadByHoursInMW(){
		for(int i = 0; i < 24 ; i++){
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
