package com.epsm.electricPowerSystemModel.model.generalModel;

import static org.mockito.Mockito.*;

import java.time.LocalTime;

import org.junit.*;

import com.epsm.electricPowerSystemModel.model.consumption.Consumer;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;

public class ElectricPowerSystemSimulationImplTest {
	private ElectricPowerSystemSimulation simulation;
	private PowerStation station_1;
	private Consumer consumer_1;
	private float previousFrequency;
	private float currentFrequency;
	
	@Before
	public void initialize(){
		simulation = new ElectricPowerSystemSimulationImpl();
		station_1 = mock(PowerStation.class);
		consumer_1 = mock(Consumer.class);
		
		simulation.addPowerStation(station_1);
		simulation.addPowerConsumer(consumer_1);
	}
	
	@Test
	public void timeGoesInTheSimulation(){
		LocalTime previousTime;
		LocalTime nextTime;
		
		for(int i = 0; i < 1000 ;i++){
			previousTime = simulation.getTime();
			simulation.calculateNextStep();
			nextTime = simulation.getTime();
			
			Assert.assertTrue(previousTime.isBefore(nextTime));
		}
	}
	
	@Test
	public void FrequencyDecreasesIfLoadHigherThanGeneration(){
		when(station_1.calculateGenerationInMW()).thenReturn(99f);
		when(consumer_1.calculateCurrentLoadInMW()).thenReturn(100f);

		for(int i = 0; i < 1000; i++){			
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
		when(station_1.calculateGenerationInMW()).thenReturn(100f);
		when(consumer_1.calculateCurrentLoadInMW()).thenReturn(99f);
		
		for(int i = 0; i < 1000; i++){			
			rememberOldFrequencyAndDoNextStep();
			
			Assert.assertTrue(previousFrequency < currentFrequency);
		}
	}
	
	@Test
	public void FrequencyIsConstantIfLoadEqualsToGeneration(){
		when(station_1.calculateGenerationInMW()).thenReturn(100f);
		when(consumer_1.calculateCurrentLoadInMW()).thenReturn(100f);
		
		for(int i = 0; i < 1000; i++){			
			rememberOldFrequencyAndDoNextStep();
			
			Assert.assertTrue(previousFrequency == currentFrequency);
		}
	}
}
