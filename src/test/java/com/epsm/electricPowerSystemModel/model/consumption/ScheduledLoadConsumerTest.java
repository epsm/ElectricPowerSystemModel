package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalTime;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.consumption.Consumer;
import com.epsm.electricPowerSystemModel.model.consumption.ScheduledLoadConsumer;
import com.epsm.electricPowerSystemModel.model.dispatch.ConsumerState;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;
import com.epsm.electricPowerSystemModel.model.constantsForTests.TestsConstants;

public class ScheduledLoadConsumerTest{
	private ScheduledLoadConsumer consumer;
	private ElectricPowerSystemSimulation simulation;
	private float[] approximateLoadByHoursInPercent;
	private float expectedLoad;
	private LocalTime expectedTime;
	private ConsumerState state;
	private Random random = new Random();
	private final int CONSUMER_NUMBER = 664;
	
	@Before
	public void initialize(){
		simulation = createPowerSystemStub();
		consumer = new ScheduledLoadConsumer(CONSUMER_NUMBER, simulation);
		approximateLoadByHoursInPercent = TestsConstants.LOAD_BY_HOURS;
		
		consumer.setApproximateLoadByHoursOnDayInPercent(approximateLoadByHoursInPercent);
		consumer.setMaxLoadWithoutRandomInMW(100);
		consumer.setRandomFluctuationsInPercent(10);
		consumer.setDegreeOfDependingOnFrequency(2);
	}
	
	//realization with Mock works too slow
	private ElectricPowerSystemSimulation createPowerSystemStub(){
		return new ElectricPowerSystemSimulation() {
			LocalTime time = LocalTime.MIDNIGHT;
			float possibleFluctuations = 1.2f;
			float frequency = 50;
						
			@Override
			public LocalTime getTimeInSimulation() {
				return time;
			}
			
			@Override
			public float getFrequencyInPowerSystem() {
				return frequency;
			}
			
			@Override
			public void calculateNextStep() {
				time = time.plusNanos(100_000_000);
				frequency = (random.nextFloat() * GlobalConstatnts.STANDART_FREQUENCY * 
						possibleFluctuations);
			}

			@Override
			public void addPowerConsumer(Consumer powerConsumer) {
			}

			@Override
			public void addPowerStation(PowerStation powerStation) {
			}
		};
	}
	
	@Test
	public void ConsumerLoadIsInExpectingRange(){
		LocalTime time = null;
		float minPermissibleValue = 0;
		float maxPermissibleValue = 0;
		float actualValue = 0;
		int repeats = 2 * 24 * 60 * 60 * 10;//two days with step 0.1s
		
		for(int i = 0; i < repeats; i++){
			time = simulation.getTimeInSimulation();
			actualValue = consumer.calculateCurrentLoadInMW();
			minPermissibleValue = calculateMinPermissibleValue(time);
			maxPermissibleValue = calculateMaxPermissibleValue(time);
			
			Assert.assertTrue(actualValue >= minPermissibleValue);
			Assert.assertTrue(actualValue <= maxPermissibleValue);
		
			simulation.calculateNextStep();
		}
	}
	
	private float calculateMinPermissibleValue(LocalTime time){
		float minValueWithoutRandom = findMinValueWithoutRandomInMW(time);
		float minValueWithRandom = substractRandomPartInMw(minValueWithoutRandom);
		float minValueWithRandomAndCountingFrequency = 
				calculateLoadCountingFrequencyInMW(minValueWithRandom);
		
		return minValueWithRandomAndCountingFrequency;
	}
	
	private float findMinValueWithoutRandomInMW(LocalTime time){
		float valueOnRequestedHour = approximateLoadByHoursInPercent[time.getHour()];
		float valueOnNextHour = approximateLoadByHoursInPercent[time.plusHours(1).getHour()];
		
		return Math.min(valueOnRequestedHour, valueOnNextHour) *
				consumer.getMaxLoadWithoutRandomInMW() / 100;
	}
	
	private float substractRandomPartInMw(float load){
		return load - load * consumer.getRandomFluctuationsInPercent() / 100;
	}
	
	private float calculateLoadCountingFrequencyInMW(float load){
		float currentFrequency = simulation.getFrequencyInPowerSystem();
		float standartFrequency = GlobalConstatnts.STANDART_FREQUENCY;
		float degreeOfDepending = consumer.getDegreeOnDependingOfFrequency();
		
		return (float)Math.pow(
				(currentFrequency / standartFrequency),	degreeOfDepending) * load;
	}
	
	private float calculateMaxPermissibleValue(LocalTime time){
		float maxValueWithoutRandom = findMaxValueWithoutRandomInMW(time);
		float maxValueWithRandom = addRandomPartInMw(maxValueWithoutRandom);
		float maxValueWithRandomAndCountingFrequency = 
				calculateLoadCountingFrequencyInMW(maxValueWithRandom);
		
		return maxValueWithRandomAndCountingFrequency;
	}
	
	private float findMaxValueWithoutRandomInMW(LocalTime time){
		float valueOnRequestedHour = approximateLoadByHoursInPercent[time.getHour()];
		float valueOnNextHour = approximateLoadByHoursInPercent[time.plusHours(1).getHour()];
		
		return Math.max(valueOnRequestedHour, valueOnNextHour) *
				consumer.getMaxLoadWithoutRandomInMW() / 100;
	}
	
	private float addRandomPartInMw(float load){
		return load + load * consumer.getRandomFluctuationsInPercent() / 100;
	}
	
	@Test
	public void consumerStateContainsCorrectData(){
		getExpectedValues();
		getState();
		compareValues();
	}
	
	private void getExpectedValues(){
		expectedLoad = consumer.calculateCurrentLoadInMW();
		expectedTime = simulation.getTimeInSimulation();
	}
	
	private void getState(){
		state = (ConsumerState) consumer.getState();
	}
	
	private void compareValues(){
		int actualConsumerNumber = state.getConsumerNumber();
		LocalTime actualInState = state.getTimeStamp();
		float actualLoad = state.getTotalLoad();
		
		Assert.assertEquals(CONSUMER_NUMBER, actualConsumerNumber, 0);
		Assert.assertEquals(expectedTime, actualInState);
		Assert.assertEquals(expectedLoad, actualLoad, 0);
	}
}
