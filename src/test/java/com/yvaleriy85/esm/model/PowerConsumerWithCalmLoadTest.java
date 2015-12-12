package test.java.com.yvaleriy85.esm.model;

import org.junit.Assert;
import org.junit.Test;

import main.java.com.yvaleriy85.esm.model.DailyConsumptionPattern;
import main.java.com.yvaleriy85.esm.model.PowerConsumerWithCalmLoad;
import main.java.com.yvaleriy85.esm.model.Simulation;

public class PowerConsumerWithCalmLoadTest{

	private Simulation simulation;
	private PowerConsumerWithCalmLoad powerConsumer;
	private DailyConsumptionPattern dailyPattern;
	private float maxConsumptionWithoutRandomInMW;
	private float randomComponent;

	@Test
	public void powerConsumptionOfPowerConsumerTest(){
		
		initialize();
		
		for(int i = 0; i < 1000; i++){
			float realPowerConsumptionInMW = powerConsumer.getCurrentConsumptionInMW();
			float expectedPowerConsumptionInMW = calculateExpectedConsumptionInMW();
			float permissibleDelta = calculatePermissibleDeltaInMW(
					expectedPowerConsumptionInMW);
			
			Assert.assertEquals(expectedPowerConsumptionInMW, realPowerConsumptionInMW,
					permissibleDelta);
			
			simulation.nextStep();
		}
	}
	
	private void initialize(){
		powerConsumer = new PowerConsumerWithCalmLoad();
		dailyPattern = new DailyConsumptionPattern();
		maxConsumptionWithoutRandomInMW  = 	100;
		randomComponent = 10;
		
		powerConsumer.setDailyPattern(dailyPattern);
		powerConsumer.setMaxConsumptionWithoutRandomInMW(maxConsumptionWithoutRandomInMW);
		powerConsumer.setRandomComponentInPercent(randomComponent);
	}
	
	private float calculateExpectedConsumptionInMW(){
		float expectedBaseConsumptionInPercent = 
				dailyPattern.getPowerInPercentForCurrentHour(
						simulation.getTime());
		
		float expectedBaseConsumptionInMW = expectedBaseConsumptionInPercent *
				maxConsumptionWithoutRandomInMW / 100;
		
		return expectedBaseConsumptionInMW;
	}
	
	private float calculatePermissibleDeltaInMW(float currentConsumption){
		return currentConsumption * randomComponent / 100;
	}
}
