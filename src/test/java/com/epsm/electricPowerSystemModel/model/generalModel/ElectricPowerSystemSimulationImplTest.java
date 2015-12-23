package test.java.com.epsm.electricPowerSystemModel.model.generalModel;

import static org.mockito.Mockito.*;

import java.time.LocalTime;

import org.junit.*;

import main.java.com.epsm.electricPowerSystemModel.model.consumption.Consumer;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.SimulationState;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;
import main.java.com.epsm.electricPowerSystemModel.model.generation.PowerStation;

public class ElectricPowerSystemSimulationImplTest {
	private ElectricPowerSystemSimulation simulation;
	private PowerStation station_1;
	private PowerStation station_2;
	private Consumer consumer_1;
	private Consumer consumer_2;
	private SimulationState report;
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
	
	@Test
	public void SummaryPowerGenerationFromPowerStationEqualsToShownByModel(){
		prepareSecondConsumerAndSecondPowerStation();
		float generation = station_1.calculateGenerationInMW() + station_2.calculateGenerationInMW();
		
		report = simulation.calculateNextStep();
		
		Assert.assertEquals(generation, report.getTotalGeneration(), 0);
	}
	
	private void prepareSecondConsumerAndSecondPowerStation(){
		station_2 = mock(PowerStation.class);
		consumer_2 = mock(Consumer.class);
		
		when(station_1.calculateGenerationInMW()).thenReturn(60f);
		when(station_2.calculateGenerationInMW()).thenReturn(70f);
		when(consumer_1.calculateCurrentLoadInMW()).thenReturn(80f);
		when(consumer_2.calculateCurrentLoadInMW()).thenReturn(40f);
		
		simulation.addPowerStation(station_2);
		simulation.addPowerConsumer(consumer_2);
	}
	
	@Test
	public void SummaryPowerLoadFromConsumerEqualsToShownByModel(){
		prepareSecondConsumerAndSecondPowerStation();
		float load = consumer_1.calculateCurrentLoadInMW() + consumer_2.calculateCurrentLoadInMW();
		
		report = simulation.calculateNextStep();
		
		Assert.assertEquals(load, report.getTotalLoad(), 0);
	}
}
