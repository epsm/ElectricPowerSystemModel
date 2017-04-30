package com.epsm.epsmcore.model.simulation;

import com.epsm.epsmcore.model.constantsForTests.TestsConstants;
import com.epsm.epsmcore.model.consumption.Consumer;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.generation.PowerStation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PowerStation.class)
public class SimulationImplTest {
	private ElectricPowerSystemSimulationImpl simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private PowerStation station;
	private Consumer consumer;
	private float previousFrequency;
	private float currentFrequency;
	
	@Before
	public void setUp(){
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		simulation = new ElectricPowerSystemSimulationImpl(timeService, dispatcher,
				TestsConstants.START_DATETIME);
		station = PowerMockito.mock(PowerStation.class);
		consumer = PowerMockito.mock(Consumer.class);
		when(station.getId()).thenReturn(1L);
		when(consumer.getId()).thenReturn(2L);
		
		simulation.addPowerStation(station);
		simulation.addPowerConsumer(consumer);
	}
	
	@Test
	public void timeGoesInTheSimulation(){
		LocalDateTime previousTime;
		LocalDateTime nextTime;
		
		for(int verifyTimes = 0; verifyTimes < 10 ;verifyTimes++){
			previousTime = simulation.getDateTimeInSimulation();
			simulation.calculateNextStep();
			nextTime = simulation.getDateTimeInSimulation();
			
			Assert.assertTrue(previousTime.isBefore(nextTime));
		}
	}
	
	@Test
	public void FrequencyDecreasesIfLoadHigherThanGeneration(){
		when(station.calculatePowerBalance()).thenReturn(99f);
		when(consumer.calculatePowerBalance()).thenReturn(-100f);

		for(int verifyTimes = 0; verifyTimes < 100; verifyTimes++){			
			rememberOldFrequencyAndDoNextStep();
			
			Assert.assertTrue(previousFrequency > currentFrequency);
		}
	}
	
	private void rememberOldFrequencyAndDoNextStep(){
		previousFrequency = simulation.getFrequencyInPowerSystem();
		simulation.calculateNextStep();
		currentFrequency = simulation.getFrequencyInPowerSystem();
	}
	
	@Test
	public void FrequencyIncreasesIfLoadLessThanGeneration(){
		when(station.calculatePowerBalance()).thenReturn(100f);
		when(consumer.calculatePowerBalance()).thenReturn(99f);
		
		for(int verifyTimes = 0; verifyTimes < 100; verifyTimes++){			
			rememberOldFrequencyAndDoNextStep();
			
			Assert.assertTrue(previousFrequency < currentFrequency);
		}
	}
	
	@Test
	public void FrequencyIsConstantIfLoadEqualsToGeneration(){
		when(station.calculatePowerBalance()).thenReturn(100f);
		when(consumer.calculatePowerBalance()).thenReturn(-100f);
		
		for(int verifyTimes = 0; verifyTimes < 100; verifyTimes++){			
			rememberOldFrequencyAndDoNextStep();
			
			Assert.assertTrue(previousFrequency == currentFrequency);
		}
	}
}
