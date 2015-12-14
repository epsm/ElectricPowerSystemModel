package test.java.com.yvhobby.epsm.model.consumption;

import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Test;

import main.java.com.yvhobby.epsm.model.consumption.ConsumptionSchedule;
import main.java.com.yvhobby.epsm.model.consumption.ConsumptionScheduleCalculator;
import main.java.com.yvhobby.epsm.model.generalModel.DailyConsumptionPattern;

public class ConsumptionScheduleCalculatorTest {
	private DailyConsumptionPattern dailyPattern = new DailyConsumptionPattern();
	private float maxConsumptionWithoutRandomInMW = 100;
	private float randomComponent = 10;
	private ConsumptionSchedule schedule;
	private LocalTime timeToTest;
	private ConsumptionScheduleCalculator calculator = new ConsumptionScheduleCalculator();

	@Test
	public void conformityCalculatedPowerConsumptionToDailyConsumptionPattern(){
		schedule = calculator.calculateConsumptionScheduleInMW(
				dailyPattern, maxConsumptionWithoutRandomInMW, randomComponent);
		
		timeToTest = LocalTime.of(0, 0);
		
		for(int i = 0; i < 24; i++){
			float expectedPowerConsumptionInMW = calculateExpectedConsumptionInMW();
			float permissibleDelta = calculatePermissibleDeltaInMW(expectedPowerConsumptionInMW);
			
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
		ConsumptionSchedule schedule_1 = calculator.calculateConsumptionScheduleInMW(
				dailyPattern, maxConsumptionWithoutRandomInMW, randomComponent);
		ConsumptionSchedule schedule_2 = calculator.calculateConsumptionScheduleInMW(
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
			
			//System.out.println("v1=" + schedule_1 + ", v2=" + schedule_2);
			System.out.println("v1=" + valueFromSchedule_1 + ", v2=" + valueFromSchedule_2);
		}

		Assert.assertTrue(atLeastOneUnique);
	}
}
