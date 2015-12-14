package test.java.com.yvhobby.epsm.model.consumption;

import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.java.com.yvhobby.epsm.model.consumption.ConsumptionSchedule;
import main.java.com.yvhobby.epsm.model.consumption.ConsumptionScheduleCalculator;
import main.java.com.yvhobby.epsm.model.generalModel.DailyConsumptionPattern;

public class ConsumptionScheduleTest{
	private DailyConsumptionPattern dailyPattern = new DailyConsumptionPattern();
	private float maxConsumptionWithoutRandomInMW = 100;
	private float randomComponent = 10;
	private ConsumptionSchedule schedule;
	private ConsumptionScheduleCalculator calculator = new ConsumptionScheduleCalculator();
	
	@Before
	public void createSchedule(){
		schedule = calculator.calculateConsumptionScheduleInMW(
				dailyPattern, maxConsumptionWithoutRandomInMW, randomComponent);
	}
	
	@Test
	public void isScheduleInterpolized(){
		LocalTime pointer = LocalTime.of(0, 0);
		float currentConsumption = 0;
		float previousConsumption = 0;
		float actualDdelta = 0;
		float maxDelta = 0;
		
		maxDelta = calculateMaxDelta();
		
		do{
			currentConsumption = schedule.getConsumptionOnTime(pointer);
			previousConsumption = schedule.getConsumptionOnTime(pointer.minusSeconds(1));
			actualDdelta = Math.abs(currentConsumption - previousConsumption);
			
			Assert.assertTrue(actualDdelta <= maxDelta);

			pointer = pointer.plusSeconds(1);
		}while(pointer.isAfter(LocalTime.of(0, 0)));
	}
	
	private float calculateMaxDelta(){
		float maxConsumptionValue = Float.MIN_VALUE;
		float minConsumptionValue = Float.MAX_VALUE;
		float maxlDelta = 0;
		LocalTime pointer = LocalTime.of(0, 0);
		
		for(int i = 0; i < 24; i++){
			float currentValue = schedule.getConsumptionOnTime(pointer);
			
			if(maxConsumptionValue < currentValue){
				maxConsumptionValue = currentValue;
			}
			
			if(minConsumptionValue > currentValue){
				minConsumptionValue = currentValue;
			}
			
			pointer = pointer.plusHours(1);
		}
		
		Assert.assertNotEquals(maxConsumptionValue, Float.MIN_VALUE, 0);
		Assert.assertNotEquals(minConsumptionValue, Float.MAX_VALUE, 0);
		
		maxlDelta = (maxConsumptionValue - minConsumptionValue) / (60 * 60);
		
		return maxlDelta;
	}
}
