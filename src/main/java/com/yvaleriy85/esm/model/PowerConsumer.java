package main.java.com.yvaleriy85.esm.model;

import java.time.LocalTime;
import java.util.Random;

public class PowerConsumer{

	private DailyConsumptionPattern dailyPattern;
	private float MaxConsumptionWithoutRandomInMW = 100;
	private float randomComponentInPercent = 10;
	private float[] consumptionOnThisDay = new float[24];
	private LocalTime lastRequest = LocalTime.of(0,0,0,0);
	private Random random = new Random();
	private final float STANDART_FREQUENCY = 50;
	private final long NANOS_IN_HOUR = 60 * 60 * (long)Math.pow(10, 9);
	private LocalTime currentTime;
	private float currentFrequency;
	
	public PowerConsumer() {
		calculateConsumptionScheduleOnThisDay();
	}
	
	public float getCurrentConsumptionInMW(){
		currentTime = Simulation.getTime();
		currentFrequency = EnergySystem.getFrequencyInPowerSystem();
		
		if(isItANewDay()){
			calculateConsumptionScheduleOnThisDay();
		}
		
		return getConsumptionForThisMoment();
	}
	
	private boolean isItANewDay(){
		return lastRequest.isAfter(currentTime);
	}
	
	private void calculateConsumptionScheduleOnThisDay(){
		for(int hour = 0; hour < 24; hour++){
			LocalTime necessaryTime = LocalTime.of(hour, 0);
			consumptionOnThisDay[hour] = calculateConsumptionForHourInMW(necessaryTime);
		}
	}
	
	private float calculateConsumptionForHourInMW(LocalTime time){

		float baseComponentInPercent = dailyPattern.getPowerInPercentForCurrentHour(time);
		float baseComponentInMW = baseComponentInPercent * MaxConsumptionWithoutRandomInMW;
		float randomComponentInPercent = calculateRandomComponentInPercent();
		float randomComponentInMW = randomComponentInPercent * baseComponentInMW / 100;
		float currentConsumption = baseComponentInMW + randomComponentInMW;
		
		if(currentConsumption < 0){
			currentConsumption = 0;
		}
		
		return currentConsumption;
	}
	
	private float calculateRandomComponentInPercent(){
		boolean isNegative = random.nextBoolean();
		float randomComponent = random.nextFloat() * randomComponentInPercent;
		randomComponent = (isNegative) ? -randomComponent : randomComponent;
		
		return randomComponent;
	}
	
	private float getConsumptionForThisMoment(){
		float consumptionWithoutCountingFrequency =  interpolateConsumptionValueBetweenHours();
		
		return countConsumptionCountingFrequency(consumptionWithoutCountingFrequency);
	}
	
	private float interpolateConsumptionValueBetweenHours(){
		int currentHour = currentTime.getHour();
		int nextHour = currentHour + 1;
		float currentHourConsumption = consumptionOnThisDay[currentHour];
		float nextHourConsumption = consumptionOnThisDay[nextHour];
		float interpolizedValue = currentHourConsumption + (getNanosFromStartOfThisHour() /
				(NANOS_IN_HOUR)) * (nextHourConsumption - currentHourConsumption);
		
		return interpolizedValue;
	}
	
	private long getNanosFromStartOfThisHour(){
		long minutesFromStartOfThisHour = currentTime.getMinute();
		long secondsFromStartOfThisHour = currentTime.getSecond();
		long nanosFromStartOfThisHour = currentTime.getNano();
		
		return (minutesFromStartOfThisHour * 60 + secondsFromStartOfThisHour) *
				(long)Math.pow(10, 9) + nanosFromStartOfThisHour;
	}
	
	private float countConsumptionCountingFrequency(float consumptionWithoutCountingFrequency){
		return (float)Math.pow((currentFrequency / STANDART_FREQUENCY), 2) * 
				consumptionWithoutCountingFrequency;
	}

	public float getRandomComponentInPercent() {
		return randomComponentInPercent;
	}

	public void setDailyPattern(DailyConsumptionPattern dailyPattern) {
		this.dailyPattern = dailyPattern;
	}
}
