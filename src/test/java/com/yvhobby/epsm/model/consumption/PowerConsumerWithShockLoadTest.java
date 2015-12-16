package test.java.com.yvhobby.epsm.model.consumption;

import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.java.com.yvhobby.epsm.model.consumption.PowerConsumerWithShockLoad;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulationImpl;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;

public class PowerConsumerWithShockLoadTest {
	private ElectricPowerSystemSimulationImpl simulation;
	private PowerConsumerWithShockLoad consumer;
	float previousLoad;
	float currentLoad;
	private LocalTime turnOnTime;
	private LocalTime turnOffTime;
	private final int WORK_TIME = 300; 
	private final int PAUSE_TIME = 500; 
	
	@Before
	public void initialize(){
		simulation = spy(new ElectricPowerSystemSimulationImpl());
		consumer = new PowerConsumerWithShockLoad();
		turnOnTime = null;
		turnOffTime = null;
		
		when(simulation.getFrequencyInPowerSystem()).thenReturn(GlobalConstatnts.STANDART_FREQUENCY);
		
		consumer.setMaxLoad(100f);
		consumer.setMaxWorkDurationInSeconds(WORK_TIME);
		consumer.setMaxPauseBetweenWorkInSeconds(PAUSE_TIME);
		consumer.setElectricalPowerSystemSimulation(simulation);
		consumer.setDegreeOfDependingOnFrequency(2);
	}
	
	@Test
	public void loadChargesInstantlyAndThenConstant(){
		boolean loadChangedAfterTurnOn = true;
		
		for(int i = 0; i < 1_000_000; i++){
			nextStep();
			
			if(wasLoadTurnedOn()){
				nextStep();
				loadChangedAfterTurnOn = wasLoadChanged();
				break;
			}
		}
		
		Assert.assertFalse(loadChangedAfterTurnOn);
	}
	
	private boolean wasLoadTurnedOn(){
		return previousLoad == 0 && previousLoad != currentLoad;
	}
	
	private boolean wasLoadChanged(){
		return currentLoad != previousLoad;
	}
	
	private void nextStep(){
		previousLoad = currentLoad;
		simulation.calculateNextStep();
		currentLoad = consumer.getCurrentLoadInMW();
	}
	
	@Test
	public void loadDischargesInstantlyAndThenConstant(){
		boolean loadChangedAfterTurnOff = true;
		
		for(int i = 0; i < 1_000_000; i++){
			nextStep();
			
			if(wasLoadTurnedOff()){
				nextStep();
				loadChangedAfterTurnOff = wasLoadChanged();
				break;
			}
		}
		
		Assert.assertFalse(loadChangedAfterTurnOff);
	}
	
	private boolean wasLoadTurnedOff(){
		return previousLoad > 0 &&  currentLoad == 0;
	}
	
	@Test
	public void loadDurationMatchesfromHalfToTheWholeOfSet(){
		for(int i = 0; i < 2; i++){//too much time if more
			findTurnOnTime();
			findTurnOffTime();
			long duration = getAbsSecondsBetweenTwoTimes();
	
			Assert.assertTrue(turnOnTime != null && turnOffTime != null);
			Assert.assertTrue(duration >= WORK_TIME / 2 && duration <= WORK_TIME);
		}
	}
	
	private void findTurnOnTime(){
		for(int i = 0; i < 1_000_000; i++){
			nextStep();
			if(wasLoadTurnedOn()){
				turnOnTime = simulation.getTime();
				break;
			}
		}
	}
	
	private void findTurnOffTime(){
		for(int i = 0; i < 1_000_000; i++){
			nextStep();
			if(wasLoadTurnedOff()){
				turnOffTime = simulation.getTime();
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
	public void pauseBetweenLoadsMatchesFromHalfToWholeOfSet(){
		for(int i = 0; i < 2; i++){//too much time if more
			findTurnOffTime();
			findTurnOnTime();
			long duration = getAbsSecondsBetweenTwoTimes();
	
			Assert.assertTrue(turnOnTime != null && turnOffTime != null);
			Assert.assertTrue(duration >= PAUSE_TIME / 2 && duration <= PAUSE_TIME);
		}
	}
	
	@Test
	public void dependencyConsumptionOnFrequency(){
		findTurnOnTime();
		prepareMockSimulationWithDecreasingFrequency();
		nextStep();
		
		Assert.assertTrue(turnOnTime != null);
		Assert.assertTrue(previousLoad > currentLoad);
	}
	
	public void prepareMockSimulationWithDecreasingFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(49.99f);
	}
}
