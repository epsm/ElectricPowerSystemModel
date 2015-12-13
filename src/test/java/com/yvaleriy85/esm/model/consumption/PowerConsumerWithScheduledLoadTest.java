package test.java.com.yvaleriy85.esm.model.consumption;

import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.java.com.yvaleriy85.esm.model.consumption.PowerConsumerWithScheduledLoad;
import main.java.com.yvaleriy85.esm.model.generalModel.DailyConsumptionPattern;
import main.java.com.yvaleriy85.esm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvaleriy85.esm.model.generalModel.GlobalConstatnts;
import main.java.com.yvaleriy85.esm.model.generalModel.SimulationParameters;

public class PowerConsumerWithScheduledLoadTest{
	private PowerConsumerWithScheduledLoad consumer;
	private ElectricPowerSystemSimulation simulation;
	private DailyConsumptionPattern pattern;
	
	@Before
	public void initialize(){
		simulation = createPowerSystemStub();
		consumer = new PowerConsumerWithScheduledLoad();
		pattern = new DailyConsumptionPattern();
		
		consumer.setDailyPattern(pattern);
		consumer.setMaxConsumptionWithoutRandomInMW(100);
		consumer.setRandomComponentInPercent(10);
		consumer.setDegreeOfDependingOnFrequency(2);
		consumer.setElectricalPowerSystemSimulation(simulation);
	}
	
	private ElectricPowerSystemSimulation createPowerSystemStub(){
		return new ElectricPowerSystemSimulation() {
			LocalTime time = LocalTime.of(0, 0);
			float possibleFluctuations = 1.1f;
			float frequency = 0;
						
			@Override
			public LocalTime getTime() {
				return time;
			}
			
			@Override
			public float getFrequencyInPowerSystem() {
				return frequency;
			}
			
			@Override
			public SimulationParameters calculateNextStep() {
				time = time.plusNanos(100_000_000);
				frequency = (float) (Math.random() * GlobalConstatnts.STANDART_FREQUENCY * 
						possibleFluctuations);
				
				return null;
			}
		};
	}
	
	@Test
	public void isConsumerConsumptionIsInExpectingRange(){
		LocalTime time = simulation.getTime();
		float minPermissibleValue = 0;
		float maxPermissibleValue = 0;
		float actualValue = 0;
		int times = 2 * 24 * 60 * 60 * 10;
		
		for(int i = 0; i < times; i++){
			actualValue = consumer.getCurrentConsumptionInMW();
			minPermissibleValue = calculateMinPermissibleValue(time);
			maxPermissibleValue = calculateMaxPermissibleValue(time);
			
			Assert.assertTrue(actualValue >= minPermissibleValue);
			Assert.assertTrue(actualValue <= maxPermissibleValue);
		
			simulation.calculateNextStep();
			time = simulation.getTime();
		}
	
		//System.out.println("last time=" + time);
	}
	
	private float calculateMinPermissibleValue(LocalTime time){
		float minValueWithoutRandom = findMinValueWithoutRandomInMW(time);
		float minValueWithRandom = substractRandomPartInMw(minValueWithoutRandom);
		float minValueWithRandomAndCountingFrequency = 
				calculateConsumptionCountingFrequencyInMW(minValueWithRandom);
		
		return minValueWithRandomAndCountingFrequency;
	}
	
	private float findMinValueWithoutRandomInMW(LocalTime time){
		float valueOnRequestedHour = pattern.getPowerInPercentForCurrentHour(time);
		float valueOnNextHour = pattern.getPowerInPercentForCurrentHour(time.plusHours(1));
		
		return Math.min(valueOnRequestedHour, valueOnNextHour) *
				consumer.getMaxConsumptionWithoutRandomInMW() / 100;
	}
	
	private float substractRandomPartInMw(float consumption){
		return consumption - consumption * consumer.getRandomComponentInPercent() / 100;
	}
	
	private float calculateConsumptionCountingFrequencyInMW(float consumption){
		float currentFrequency = simulation.getFrequencyInPowerSystem();
		float standartFrequency = GlobalConstatnts.STANDART_FREQUENCY;
		float degreeOfDepending = consumer.getDegreeOnDependingOfFrequency();
		
		return (float)Math.pow(
				(currentFrequency / standartFrequency),	degreeOfDepending) * consumption;
	}
	
	private float calculateMaxPermissibleValue(LocalTime time){
		float maxValueWithoutRandom = findMaxValueWithoutRandomInMW(time);
		float maxValueWithRandom = addRandomPartInMw(maxValueWithoutRandom);
		float maxValueWithRandomAndCountingFrequency = 
				calculateConsumptionCountingFrequencyInMW(maxValueWithRandom);
		
		return maxValueWithRandomAndCountingFrequency;
	}
	
	private float findMaxValueWithoutRandomInMW(LocalTime time){
		float valueOnRequestedHour = pattern.getPowerInPercentForCurrentHour(time);
		float valueOnNextHour = pattern.getPowerInPercentForCurrentHour(time.plusHours(1));
		
		return Math.max(valueOnRequestedHour, valueOnNextHour) *
				consumer.getMaxConsumptionWithoutRandomInMW() / 100;
	}
	
	private float addRandomPartInMw(float consumption){
		return consumption + consumption * consumer.getRandomComponentInPercent() / 100;
	}
}
