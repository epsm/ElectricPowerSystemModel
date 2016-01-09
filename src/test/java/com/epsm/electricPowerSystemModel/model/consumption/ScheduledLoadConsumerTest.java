package com.epsm.electricPowerSystemModel.model.consumption;

import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.constantsForTests.TestsConstants;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.electricPowerSystemModel.model.generalModel.Constants;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class ScheduledLoadConsumerTest{
	private ElectricPowerSystemSimulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private ScheduledLoadConsumer consumer;
	private float[] approximateLoadByHoursInPercent;
	private LocalTime currentTime;
	private ConsumerState state;
	private float expectedLoad;
	private LocalTime expectedTime;
	private final int CONSUMER_NUMBER = 664;
	private final float RANDOM_FLUCTUATION_IN_PERCENT = 10;
	
	@Before
	public void setUp(){
		ConsumerParametersStub parameters = new ConsumerParametersStub(
				CONSUMER_NUMBER, LocalDateTime.MIN, LocalTime.MIN); 
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		simulation = new EPSMImplStub(timeService, dispatcher);
		consumer = new ScheduledLoadConsumer(simulation, timeService, dispatcher, parameters);
		approximateLoadByHoursInPercent = TestsConstants.LOAD_BY_HOURS;
		currentTime = LocalTime.MIDNIGHT;
		
		consumer.setApproximateLoadByHoursOnDayInPercent(approximateLoadByHoursInPercent);
		consumer.setMaxLoadWithoutRandomInMW(100);//100MW = 100% in approximateLoadByHoursInPercent
		consumer.setRandomFluctuationsInPercent(RANDOM_FLUCTUATION_IN_PERCENT);
	}
	
	private class EPSMImplStub extends ElectricPowerSystemSimulationImpl{
		public EPSMImplStub(TimeService timeService, Dispatcher dispatcher) {
			super(timeService, dispatcher);
		}

		@Override
		public void calculateNextStep(){
			currentTime = currentTime.plusHours(1);
		}
		
		@Override
		public float getFrequencyInPowerSystem(){
			return Constants.STANDART_FREQUENCY;
		}
		
		@Override
		public LocalTime getTimeInSimulation(){
			return currentTime;
		}
	}
	
	@Test
	public void ConsumerLoadIsInExpectingRange(){
		do{
			float expectedLoad = -approximateLoadByHoursInPercent[currentTime.getHour()];
			float calculatedLoad = consumer.calculatePowerBalance();
			float permissibleDelta = -expectedLoad * RANDOM_FLUCTUATION_IN_PERCENT / 100;
			simulation.calculateNextStep();
			
			Assert.assertEquals(expectedLoad, calculatedLoad, permissibleDelta);
		}while(currentTime.isAfter(LocalTime.MIDNIGHT));
	}
	
	@Test
	public void ConsumerLoadIsNewEveryDay(){
		float firstDayLoad = 0;
		float seconsDayLoad = 0;
		int hour = 0;
		
		for(; hour < 48 ; hour ++){
			if(isItFirstDay(hour)){
				firstDayLoad += consumer.calculatePowerBalance();
			}else{
				seconsDayLoad += consumer.calculatePowerBalance();
			}
			
			simulation.calculateNextStep();
		}
		
		Assert.assertEquals(hour, 48);
		Assert.assertNotEquals(firstDayLoad, seconsDayLoad);
	}
	
	private boolean isItFirstDay(int hour){
		return hour < 24;
	}
	
	@Test
	public void consumerStateContainsCorrectData(){
		getExpectedValues();
		getState();
		compareValues();
	}
	
	private void getExpectedValues(){
		expectedLoad = consumer.calculatePowerBalance();
		expectedTime = simulation.getTimeInSimulation();
	}
	
	private void getState(){
		state = (ConsumerState) consumer.getState();
	}
	
	private void compareValues(){
		long actualConsumerNumber = state.getPowerObjectId();
		LocalTime actualTime = state.getSimulationTimeStamp();
		float actualLoad = state.getLoad();
		
		Assert.assertEquals(CONSUMER_NUMBER, actualConsumerNumber);
		Assert.assertEquals(expectedTime, actualTime);
		Assert.assertEquals(expectedLoad, actualLoad, 0);
	}
}
