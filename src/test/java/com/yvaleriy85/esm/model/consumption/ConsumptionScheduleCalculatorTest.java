package test.java.com.yvaleriy85.esm.model.consumption;

import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

import main.java.com.yvaleriy85.esm.model.consumption.ConsumptionSchedule;
import main.java.com.yvaleriy85.esm.model.consumption.ConsumptionScheduleCalculator;
import main.java.com.yvaleriy85.esm.model.generalModel.DailyConsumptionPattern;

public class ConsumptionScheduleCalculatorTest {
	private DailyConsumptionPattern dailyPattern = new DailyConsumptionPattern();
	private float maxConsumptionWithoutRandomInMW = 100;
	private float randomComponent = 10;
	private ConsumptionSchedule schedule;
	private LocalTime timeToTest;

	@Test
	public void conformityCalculatedPowerConsumptionToDailyConsumptionPattern(){
		schedule = ConsumptionScheduleCalculator.calculateConsumptionScheduleInMW(
				dailyPattern, maxConsumptionWithoutRandomInMW, randomComponent);
		
		timeToTest = LocalTime.of(0, 0);
		
		for(int i = 0; i < 24; i++){
			float expectedPowerConsumptionInMW = calculateExpectedConsumptionInMW();
			float permissibleDelta = calculatePermissibleDeltaInMW(
					expectedPowerConsumptionInMW);
			
			Assert.assertEquals(expectedPowerConsumptionInMW, schedule.
					getConsumptionOnTime(timeToTest), permissibleDelta);
			
			timeToTest = timeToTest.plusHours(1);
		}
	}
	
	private float calculateExpectedConsumptionInMW(){
		float expectedBaseConsumptionInPercent = 
				dailyPattern.getPowerInPercentForCurrentHour(timeToTest);
		
		float expectedBaseConsumptionInMW = expectedBaseConsumptionInPercent *
				maxConsumptionWithoutRandomInMW / 100;
		
		return expectedBaseConsumptionInMW;
	}
	
	private float calculatePermissibleDeltaInMW(float currentConsumption){
		return currentConsumption * randomComponent / 100;
	}
	
	@Test
	public void isGeneratedConsumptionSchedulesRandom(){
		boolean atLeastOneUnique = false;
		ConsumptionSchedule schedule_1 = ConsumptionScheduleCalculator.calculateConsumptionScheduleInMW(
				dailyPattern, maxConsumptionWithoutRandomInMW, randomComponent);
		ConsumptionSchedule schedule_2 = ConsumptionScheduleCalculator.calculateConsumptionScheduleInMW(
				dailyPattern, maxConsumptionWithoutRandomInMW, randomComponent);
		
		timeToTest = LocalTime.of(0, 0);
		
		for(int i = 0; i < 24; i++){
			double valueFromSchedule_1 = schedule_1.getConsumptionOnTime(timeToTest);
			double valueFromSchedule_2 = schedule_2.getConsumptionOnTime(timeToTest);
			
			if(valueFromSchedule_1 != valueFromSchedule_2){
				atLeastOneUnique = true;
				break;
			}
			
			timeToTest = timeToTest.plusHours(1);
		}
		
		Assert.assertTrue(atLeastOneUnique);
	}
}
