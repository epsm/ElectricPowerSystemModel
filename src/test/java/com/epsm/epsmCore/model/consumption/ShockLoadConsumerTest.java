package com.epsm.epsmCore.model.consumption;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.epsmCore.model.constantsForTests.TestsConstants;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.generalModel.Constants;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.epsmCore.model.generalModel.TimeService;

public class ShockLoadConsumerTest {
	private ElectricPowerSystemSimulationImpl simulation;
	private ShockLoadConsumer consumer;
	float previousLoad;
	float currentLoad;
	private LocalDateTime turnOnTime;
	private LocalDateTime turnOffTime;
	private float expectedLoad;
	private LocalDateTime expectedTime;
	private ConsumerState state;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private final int WORK_TIME = 4;
	private final int HALF_OF_WORK_TIME = WORK_TIME / 2;
	private final int PAUSE_TIME = 6;
	private final int HALF_OF_PAUSE_TIME = PAUSE_TIME / 2;
	private final long CONSUMER_NUMBER = 664;
	private final int DEGREE_OF_DEPENDENCY_ON_FREQUENCY = 2;
	private final float MAX_LOAD = 100;
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.MIN;
	private final LocalDateTime REAL_TIMESTAMP = LocalDateTime.MIN;
	
	@Before
	public void setUp(){
		ConsumerParametersStub parameters 
			= new ConsumerParametersStub(CONSUMER_NUMBER, REAL_TIMESTAMP, SIMULATION_TIMESTAMP);
		timeService = mock(TimeService.class);
		when(timeService.getCurrentDateTime()).thenReturn(LocalDateTime.of(2000, 01, 01, 00, 00));
		dispatcher = mock(Dispatcher.class);
		simulation = spy(new ElectricPowerSystemSimulationImpl(timeService, dispatcher,
				TestsConstants.START_DATETIME));
		turnOnTime = null;
		turnOffTime = null;
		consumer = new ShockLoadConsumer(simulation, timeService, dispatcher, parameters);
		
		when(simulation.getFrequencyInPowerSystem()).thenReturn(Constants.STANDART_FREQUENCY);
		consumer.setMaxLoad(MAX_LOAD);
		consumer.setMaxWorkDurationInSeconds(WORK_TIME);
		consumer.setMaxPauseBetweenWorkInSeconds(PAUSE_TIME);
		consumer.setDegreeOfDependingOnFrequency(DEGREE_OF_DEPENDENCY_ON_FREQUENCY);
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void loadChargesInstantlyAndThenConstant(){
		boolean loadChangedAfterTurnOn = true;
		
		for(int i = 0; i < 1_000_000; i++){
			rememberCurrentLoadAsPreviousAndDoNextStep();
			
			if(wasLoadTurnedOn()){
				rememberCurrentLoadAsPreviousAndDoNextStep();
				loadChangedAfterTurnOn = wasLoadChanged();
				break;
			}
		}
		
		Assert.assertFalse(loadChangedAfterTurnOn);
	}
	
	private boolean wasLoadTurnedOn(){
		return previousLoad == 0 && currentLoad < 0;
	}
	
	private boolean wasLoadChanged(){
		return currentLoad != previousLoad;
	}
	
	private void rememberCurrentLoadAsPreviousAndDoNextStep(){
		previousLoad = currentLoad;
		simulation.calculateNextStep();
		currentLoad = consumer.calculatePowerBalance();
	}
	
	@Test
	public void loadDischargesInstantlyAndThenConstant(){
		boolean loadChangedAfterTurnOff = true;
		
		for(int i = 0; i < 1_000_000; i++){
			rememberCurrentLoadAsPreviousAndDoNextStep();
			
			if(wasLoadTurnedOff()){
				rememberCurrentLoadAsPreviousAndDoNextStep();
				loadChangedAfterTurnOff = wasLoadChanged();
				break;
			}
		}
		
		Assert.assertFalse(loadChangedAfterTurnOff);
	}
	
	private boolean wasLoadTurnedOff(){
		return previousLoad < 0 &&  currentLoad == 0;
	}
	
	@Test
	public void WorkDurationConformsfromHalfToTheWholeOfSet(){
		for(int timesVerify = 0; timesVerify < 2; timesVerify++){//too much time if more
			findTurnOnTime();
			findTurnOffTime();
			long duration = getAbsSecondsBetweenTwoTimes();
	
			Assert.assertTrue(turnOnTime != null && turnOffTime != null);
			Assert.assertTrue(duration >= HALF_OF_WORK_TIME && duration <= WORK_TIME);
		}
	}
	
	private void findTurnOnTime(){
		for(int i = 0; i < 1_000_000; i++){
			rememberCurrentLoadAsPreviousAndDoNextStep();
			if(wasLoadTurnedOn()){
				turnOnTime = simulation.getDateTimeInSimulation();
				break;
			}
		}
	}
	
	private void findTurnOffTime(){
		for(int i = 0; i < 1_000_000; i++){
			rememberCurrentLoadAsPreviousAndDoNextStep();
			if(wasLoadTurnedOff()){
				turnOffTime = simulation.getDateTimeInSimulation();
				break;
			}
		}
	}
	
	private long getAbsSecondsBetweenTwoTimes(){
		Duration duration = Duration.between(turnOnTime, turnOffTime);
		long seconds = Math.abs(duration.getSeconds());
		return seconds;
	}
	
	@Test
	public void pauseBetweenWorksConformsFromHalfToWholeOfSet(){
		for(int timesVerify = 0; timesVerify < 2; timesVerify++){//too much time if more
			findTurnOffTime();
			findTurnOnTime();
			long duration = getAbsSecondsBetweenTwoTimes();
	
			Assert.assertTrue(turnOnTime != null && turnOffTime != null);
			Assert.assertTrue(duration >= HALF_OF_PAUSE_TIME && duration <= PAUSE_TIME);
		}
	}
	
	@Test
	public void LoadDependsOnFrequency(){
		findTurnOnTime();
		prepareMockSimulationWithDecreasingFrequency();
		rememberCurrentLoadAsPreviousAndDoNextStep();
		
		Assert.assertTrue(turnOnTime != null);
		Assert.assertTrue(previousLoad < currentLoad);
	}
	
	public void prepareMockSimulationWithDecreasingFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(49.99f);
	}
	
	@Test
	public void consumerStateContainsCorrectData(){
		findTurnOnTime();
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
		LocalDateTime actualInState = state.getSimulationTimeStamp();
		float actualLoad = state.getLoadInMW();
		
		Assert.assertEquals(CONSUMER_NUMBER, actualConsumerNumber, 0);
		Assert.assertEquals(expectedTime, actualInState);
		Assert.assertEquals(expectedLoad, actualLoad, 0);
	}
}
