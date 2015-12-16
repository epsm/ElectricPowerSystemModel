package main.java.com.yvhobby.epsm.model.consumption;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;

import main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration.PowerOnHoursPattern;

public class ConsumptionScheduleCalculator {
	private Random random = new Random();
	private float[] consumptionsByHours = new float[24];
	private PowerOnHoursPattern pattern;
	private float maxConsumptionWithoutRandomInMW;
	private float randComponentInPercent;
	
	public ConsumptionSchedule calculateConsumptionScheduleInMW(PowerOnHoursPattern
			dailyConsumptionPattern, float maximalConsumptionWithoutRandomInMW,
			float randomComponentInPercent){
		
		pattern = dailyConsumptionPattern;
		maxConsumptionWithoutRandomInMW = maximalConsumptionWithoutRandomInMW;
		randComponentInPercent = randomComponentInPercent;
		
		fillConsumptionByHours();
		
		return new ConsumptionSchedule(Arrays.copyOf(consumptionsByHours,
				consumptionsByHours.length));
	}
	
	private void fillConsumptionByHours(){
		for(int hour = 0; hour < 24 ; hour++){
			consumptionsByHours[hour] = calculateConsumptionForHourInMW(hour);
		}
	}
	
	private float calculateConsumptionForHourInMW(int hour){
		float baseComponentInMW = calculateBaseComponentInMw(hour);
		float randomComponentInMW = calculateRandomComponentInMw(baseComponentInMW);
		
		return Math.abs(baseComponentInMW + randomComponentInMW);
	}
	
	private float calculateBaseComponentInMw(int hour){
		LocalTime time = LocalTime.of(hour, 0);
		float baseComponentInPercent = pattern.getPowerInPercentForCurrentHour(time);
		
		return baseComponentInPercent * maxConsumptionWithoutRandomInMW / 100;
	}
	
	private float calculateRandomComponentInMw(float baseComponentInMW){
		float randomComponentInPercent = calculateRandomComponentInPercent();
		
		return randomComponentInPercent * baseComponentInMW / 100;
	}
	
	private float calculateRandomComponentInPercent(){
		boolean isNegative = random.nextBoolean();
		float randomComponent = random.nextFloat() * randComponentInPercent;
		randomComponent = (isNegative) ? -randomComponent : randomComponent;

		return randomComponent;
	}
}
