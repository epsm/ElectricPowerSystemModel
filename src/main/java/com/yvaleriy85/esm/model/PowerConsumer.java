package main.java.com.yvaleriy85.esm.model;

import java.time.LocalTime;
import java.util.Random;

public class PowerConsumer{

	private DailyConsumptionPattern dailyPattern = new DailyConsumptionPattern();
	private float installedPowerInMW = 100;
	private float randomComponentInPercent = 10;
	private Random random = new Random();
	
	public float getCurrentConsumptionInMW(){
		LocalTime currentHour = Simulation.getTime();
		float baseComponentInPercent = dailyPattern.getPowerInPercentForCurrentHour(currentHour);
		float baseComponentInMW = baseComponentInPercent * installedPowerInMW;
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

	public float getInstalledPowerInMW() {
		return installedPowerInMW;
	}

	public float getRandomComponentInPercent() {
		return randomComponentInPercent;
	}
}
