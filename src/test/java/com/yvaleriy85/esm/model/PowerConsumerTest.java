package test.java.com.yvaleriy85.esm.model;

import org.junit.Assert;
import org.junit.Test;

import main.java.com.yvaleriy85.esm.model.DailyConsumptionPattern;
import main.java.com.yvaleriy85.esm.model.PowerConsumer;
import main.java.com.yvaleriy85.esm.model.Simulation;
import main.java.com.yvaleriy85.esm.model.EnergySystem;

public class PowerConsumerTest{

	private PowerConsumer powerConsumer = new PowerConsumer();
	private DailyConsumptionPattern dailyPattern = new DailyConsumptionPattern();
	private float installedPower  = powerConsumer.getInstalledPowerInMW();
	private float randomComponent = powerConsumer.getRandomComponentInPercent();

	@Test
	public void powerConsumptionOfPowerConsumerTest(){
		
		for(int i = 0; i < 1000; i++){
			float realPowerConsumptionInMW = powerConsumer.getCurrentConsumptionInMW();
			float expectedPowerConsumptionInMW = calculateExpectedConsumptionInMW();
			float permissibleDelta = calculatePermissibleDeltaInMW(
					expectedPowerConsumptionInMW);
			
			Assert.assertEquals(realPowerConsumptionInMW, expectedPowerConsumptionInMW,
					permissibleDelta);
			
			Simulation.nextState();
		}
	}
	
	private float calculateExpectedConsumptionInMW(){
		float expectedBaseConsumptionInPercent = 
				dailyPattern.getPowerInPercentForCurrentHour(
						Simulation.getTime());
		
		float expectedBaseConsumptionInMW = expectedBaseConsumptionInPercent *
				installedPower;
		
		return expectedBaseConsumptionInMW;
	}
	
	private float calculatePermissibleDeltaInMW(float currentConsumption){
		return currentConsumption * randomComponent / 100;
	}
}
