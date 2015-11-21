package main.java.com.yvaleriy85.esm.model;

import java.time.LocalTime;
import java.util.Random;

public class SimplePowerConsumer implements PowerConsumer {

	private DailyPattern dailyPattern = new DailyPattern();
	private PowerSystem powerSystem;
	private float installedPowerInMW = 100;
	private float randomComponentInPercent = 10;
	private Random random = new Random();
	
	@Override
	public float getCurrentConsumptionInMW(){
		LocalTime currentHour = powerSystem.getSystemTime(); 
		float baseComponentInPercent = dailyPattern.getPowerInPercentForCurrentHour(currentHour);
		float baseComponentInMW = baseComponentInPercent * installedPowerInMW;
		
		float randomComponentInPercent = calculateRandomComponentInPercent();
		float randomComponentInMW = randomComponentInPercent * baseComponentInMW / 100;
		
		System.out.println("------------------");
		System.out.println("base: " + baseComponentInMW);
		System.out.println("random: " + randomComponentInMW);
		
		
		float currentConsumption = baseComponentInMW + randomComponentInMW;
		
		System.out.println("base + random :" + currentConsumption);
		System.out.println("------------------\n");
		
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

	@Override
	public void setPowerSystem(PowerSystem powerSystem) {
		this.powerSystem = powerSystem;
	}

	@Override
	public float getInstalledPowerInMW() {
		return installedPowerInMW;
	}

	@Override
	public float getRandomComponentInPercent() {
		return randomComponentInPercent;
	}
}
