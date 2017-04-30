package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.PowerObjectStateManager;
import com.epsm.epsmcore.model.constantsForTests.TestsConstants;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Constants;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmcore.model.simulation.TimeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledLoadConsumerTest {

	public static final int DEGREE_ON_DEPENDING_OF_FREQUENCY = 0;
	public static final int MAX_LOAD_WITHOUT_FLUCTUATIONS_IN_MW = 100;
	private Simulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private ScheduledLoadConsumer consumer;
	private List<Float> approximateLoadByHoursInPercent;
	private LocalDateTime currentDateTime;
	private ConsumerState state;
	private float expectedLoad;
	private LocalDateTime expectedTime;
	private final int CONSUMER_NUMBER = 664;
	private final float RANDOM_FLUCTUATION_IN_PERCENT = 10;
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.MIN;
	private final LocalDateTime REAL_TIMESTAMP = LocalDateTime.MIN;

	@Mock
	private PowerObjectStateManager stateManager;
	
	@Before
	public void setUp(){
		ScheduledLoadConsumerParameters parameters = new ScheduledLoadConsumerParameters(
				CONSUMER_NUMBER,
				DEGREE_ON_DEPENDING_OF_FREQUENCY,
				TestsConstants.LOAD_BY_HOURS,
				MAX_LOAD_WITHOUT_FLUCTUATIONS_IN_MW,
				RANDOM_FLUCTUATION_IN_PERCENT);

		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		simulation = new EPSMImplStub(timeService, dispatcher);
		consumer = new ScheduledLoadConsumer(simulation, dispatcher, parameters, stateManager);
		approximateLoadByHoursInPercent = TestsConstants.LOAD_BY_HOURS;
		currentDateTime = LocalDateTime.of(2000, 01, 01, 00, 00);
	}
	
	private class EPSMImplStub extends Simulation{
		public EPSMImplStub(TimeService timeService, Dispatcher dispatcher) {
			super(TestsConstants.START_DATETIME);
		}

		@Override
		public void doNextStep(){
			currentDateTime = currentDateTime.plusHours(1);
		}
		
		@Override
		public float getFrequencyInPowerSystem(){
			return Constants.STANDART_FREQUENCY;
		}
		
		@Override
		public LocalDateTime getDateTimeInSimulation(){
			return currentDateTime;
		}
	}
	
	@Test
	public void ConsumerLoadIsInExpectingRange(){
		do {
			float expectedLoad = -approximateLoadByHoursInPercent.get(currentDateTime.getHour());
			float calculatedLoad = consumer.calculatePowerBalance();
			float permissibleDelta = -expectedLoad * RANDOM_FLUCTUATION_IN_PERCENT / 100;
			simulation.doNextStep();
			
			Assert.assertEquals(expectedLoad, calculatedLoad, permissibleDelta);
		} while(currentDateTime.isBefore(LocalDateTime.of(2000, 01, 01, 00, 00).plusDays(1)));
	}
	
	@Test
	public void ConsumerLoadIsNewEveryDay(){
		float firstDayLoad = 0;
		float seconsDayLoad = 0;
		int hour = 0;
		final int HOURS_IN_TWO_DAY = 48;
		
		for(; hour < HOURS_IN_TWO_DAY ; hour ++){
			if(isItFirstDay(hour)){
				firstDayLoad += consumer.calculatePowerBalance();
			}else{
				seconsDayLoad += consumer.calculatePowerBalance();
			}
			
			simulation.doNextStep();
		}
		
		Assert.assertEquals(hour, HOURS_IN_TWO_DAY);
		Assert.assertNotEquals(firstDayLoad, seconsDayLoad);
	}
	
	private boolean isItFirstDay(int hour){
		return hour < Constants.DETERMINED_HOURS_IN_DAY;
	}
	
	@Test
	public void consumerStateContainsCorrectData(){
		getExpectedValues();
		getState();
		compareValues();
	}
	
	private void getExpectedValues(){
		expectedLoad = -consumer.calculatePowerBalance();
		expectedTime = simulation.getDateTimeInSimulation();
	}
	
	private void getState(){
		state = (ConsumerState) consumer.getState();
	}
	
	private void compareValues(){
		long actualConsumerNumber = state.getPowerObjectId();
		LocalDateTime actualTime = state.getSimulationTimeStamp();
		float actualLoad = state.getLoadInMW();
		
		Assert.assertEquals(CONSUMER_NUMBER, actualConsumerNumber);
		Assert.assertEquals(expectedTime, actualTime);
		Assert.assertEquals(expectedLoad, actualLoad, 0);
	}
}
