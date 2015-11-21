package test.java.com.yvaleriy85.esm.model;

import java.time.LocalTime;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;


import main.java.com.yvaleriy85.esm.model.*;

public class SimplePowerConsumerTest{

	private PowerConsumer powerConsumer = new SimplePowerConsumer();
	private DailyPattern dailyPattern = new DailyPattern();
	private PowerSystem powerSystem = new PowerSystemImpl();
	private float installedPower  = powerConsumer.getInstalledPowerInMW();
	private float randomComponent = powerConsumer.getRandomComponentInPercent();

	
	@Test
	public void powerConsumptionTest(){
		powerConsumer.setPowerSystem(powerSystem);
		
		for(int i = 0; i < 100; i++){
			System.out.println("=======================");
			System.out.println("time: " + powerSystem.getSystemTime());
			float realPowerConsumption = powerConsumer.getCurrentConsumptionInMW();
			System.out.println("real: " + realPowerConsumption);
			float expectedPowerConsumption = calculateExpectedConsumption();
			System.out.println("expectected:" + expectedPowerConsumption);
			float permissibleDelta = calculatePermissibleDeltaInPercent(
					expectedPowerConsumption);
			System.out.println("delta: " + permissibleDelta);
			System.out.println("=====================\n");
			
			Assert.assertEquals(realPowerConsumption, expectedPowerConsumption,
					permissibleDelta);
			
			powerSystem.getBalance();
		}
	}
	
	private float calculateExpectedConsumption(){
		float expectedBaseConsumptionInPercent = 
				dailyPattern.getPowerInPercentForCurrentHour(
						powerSystem.getSystemTime());
		
		float expectedBaseConsumptionInMW = expectedBaseConsumptionInPercent *
				installedPower;
		
		return expectedBaseConsumptionInMW;
	}
	
	private float calculatePermissibleDeltaInPercent(float currentConsumption){
		return currentConsumption * randomComponent / 100;
	}
}
