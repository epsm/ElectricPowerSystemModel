package test.java.com.yvhobby.epsm.model.generalModel;

import static org.mockito.Mockito.*;

import java.time.LocalTime;

import org.junit.*;

import main.java.com.yvhobby.epsm.model.consumption.PowerConsumer;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulationImpl;
import main.java.com.yvhobby.epsm.model.generalModel.SimulationParameters;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;

public class ElectricPowerSystemSimulationImplTest {
	private ElectricPowerSystemSimulation simulation;
	private PowerStation station_1;
	private PowerStation station_2;
	private PowerConsumer consumer_1;
	private PowerConsumer consumer_2;
	private SimulationParameters parameters;
	private float previousFrequency;
	private float nextFrequency;
	
	@Before
	public void initialize(){
		simulation = new ElectricPowerSystemSimulationImpl();
		station_1 = mock(PowerStation.class);
		consumer_1 = mock(PowerConsumer.class);
		
		simulation.addPowerStation(station_1);
		simulation.addPowerConsumer(consumer_1);
	}
	
	@Test
	public void TimeGoesInTheSimulation(){
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
	public void FrequencyDecreasesIfConsumptionHigherThanGeneration(){
		when(station_1.getCurrentGenerationInMW()).thenReturn(99f);
		when(consumer_1.getCurrentLoadInMW()).thenReturn(100f);

		for(int i = 0; i < 1000; i++){			
			getPreviousAndNextFrequency();
			
			Assert.assertTrue(previousFrequency > nextFrequency);
		}
	}
	
	private void getPreviousAndNextFrequency(){
		previousFrequency = simulation.getFrequencyInPowerSystem();
		SimulationParameters parameters = simulation.calculateNextStep();
		nextFrequency = simulation.getFrequencyInPowerSystem();
	}
	
	@Test
	public void FrequencyIncreasesIfConsumptionLessThanGeneration(){
		when(station_1.getCurrentGenerationInMW()).thenReturn(100f);
		when(consumer_1.getCurrentLoadInMW()).thenReturn(99f);
		
		for(int i = 0; i < 1000; i++){			
			getPreviousAndNextFrequency();
			
			Assert.assertTrue(previousFrequency < nextFrequency);
		}
	}
	
	@Test
	public void FrequencyIsConstantIfConsumptionEqualsGeneration(){
		when(station_1.getCurrentGenerationInMW()).thenReturn(100f);
		when(consumer_1.getCurrentLoadInMW()).thenReturn(100f);
		
		for(int i = 0; i < 1000; i++){			
			getPreviousAndNextFrequency();
			
			Assert.assertTrue(previousFrequency == nextFrequency);
		}
	}
	
	@Test
	public void SummaryPowerGenerationByPowerStationEqualsToShownByModel(){
		prepareSecondConsumerAndSecondPowerStation();
		float generation = station_1.getCurrentGenerationInMW() + station_2.getCurrentGenerationInMW();
		
		parameters = simulation.calculateNextStep();
		
		Assert.assertEquals(generation, parameters.getTotalGenerations(), 0);
	}
	
	private void prepareSecondConsumerAndSecondPowerStation(){
		station_2 = mock(PowerStation.class);
		consumer_2 = mock(PowerConsumer.class);
		
		when(station_1.getCurrentGenerationInMW()).thenReturn(60f);
		when(station_2.getCurrentGenerationInMW()).thenReturn(70f);
		when(consumer_1.getCurrentLoadInMW()).thenReturn(80f);
		when(consumer_2.getCurrentLoadInMW()).thenReturn(40f);
		
		simulation.addPowerStation(station_2);
		simulation.addPowerConsumer(consumer_2);
	}
	
	@Test
	public void SummaryPowerConsumptionByConsumerEqualsToShownByModel(){
		prepareSecondConsumerAndSecondPowerStation();
		float consumption = consumer_1.getCurrentLoadInMW() + consumer_2.getCurrentLoadInMW();
		
		parameters = simulation.calculateNextStep();
		
		Assert.assertEquals(consumption, parameters.getTotalConsumption(), 0);
	}
}
