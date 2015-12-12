package main.java.com.yvaleriy85.esm.model.consumption;

import java.time.LocalTime;
import java.util.Random;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import main.java.com.yvaleriy85.esm.model.generalModel.DailyConsumptionPattern;

public class ConsumptionScheduleCalculator {
	private static Random random = new Random();
	private static LinearInterpolator interpolator = new LinearInterpolator();
	private static PolynomialSplineFunction interpolatedSchedule;
	private static double[] numbersOfHoursInDay = new double[24];
	private static double[] consumptionsByHours = new double[24];
	private static DailyConsumptionPattern pattern;
	private static float maxConsumptionWithoutRandomInMW;
	private static float randComponentInPercent;
	
	public static ConsumptionSchedule calculateConsumptionScheduleInMW(DailyConsumptionPattern
			dailyConsumptionPattern, float maximalConsumptionWithoutRandomInMW,
			float randomComponentInPercent){
		
		pattern = dailyConsumptionPattern;
		maxConsumptionWithoutRandomInMW = maximalConsumptionWithoutRandomInMW;
		randComponentInPercent = randomComponentInPercent;
		
		doCalculation();
		
		return new ConsumptionSchedule(interpolatedSchedule);
	}
	
	private static void doCalculation(){
		fillConsumptionByHours();
		calculateInterpolizedSchedule();
	}
	
	private static void fillConsumptionByHours(){
		for(int hour = 0; hour < 24 ; hour++){
			numbersOfHoursInDay[hour] = hour;
			consumptionsByHours[hour] = calculateConsumptionForHourInMW(hour);
		}
	}
	
	private static float calculateConsumptionForHourInMW(int hour){
		LocalTime time = LocalTime.of(hour, 0);
		
		float baseComponentInPercent = pattern.getPowerInPercentForCurrentHour(time);
		float baseComponentInMW = baseComponentInPercent * maxConsumptionWithoutRandomInMW / 100;
		float randomComponentInPercent = calculateRandomComponentInPercent();
		float randomComponentInMW = randomComponentInPercent * baseComponentInMW / 100;
		float currentConsumption = baseComponentInMW + randomComponentInMW;
		
		if(currentConsumption < 0){
			currentConsumption = 0;
		}
		
		return currentConsumption;
	}
	
	private static float calculateRandomComponentInPercent(){
		boolean isNegative = random.nextBoolean();
		float randomComponent = random.nextFloat() * randComponentInPercent;
		randomComponent = (isNegative) ? -randomComponent : randomComponent;
		
		return randomComponent;
	}
	
	private static void calculateInterpolizedSchedule(){
		interpolatedSchedule = interpolator.interpolate(numbersOfHoursInDay, consumptionsByHours);
	}
}
