package test.java.com.epsm.electricPowerSystemModel.model.generation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;
import main.java.com.epsm.electricPowerSystemModel.model.generation.StaticRegulator;
import main.java.com.epsm.electricPowerSystemModel.model.generation.Generator;

public class ControlUnitTest {
	private ElectricPowerSystemSimulation simulation;
	private Generator generator;
	private StaticRegulator controlUnit;
	private final float GENERATOR_POWER_AT_REQUAIRED_FREQUENCY = 100;
	
	@Before
	public void init(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		generator = new Generator(1);
		controlUnit = new StaticRegulator(simulation, generator);
		
		controlUnit.setCoefficientOfStatism(0.1f);
		controlUnit.setPowerAtRequiredFrequency(GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
		
		generator.setStaticRegulator(controlUnit);
		generator.setMinimalPowerInMW(50);
		generator.setNominalPowerInMW(150);
	}

	@Test
	public void PowerIncreasesWhenFrequencyIsLow(){
		prepareMockSimulationWithLowFrequency();
		
		for(int i = 0; i < 3; i++){
			Assert.assertTrue(controlUnit.getGeneratorPowerInMW() > GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
		}
	}
	
	private void prepareMockSimulationWithLowFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(49.9f).thenReturn(49f).thenReturn(40f);
	}
	
	@Test
	public void PowerDecreasesWhenFrequencyIsHight(){
		prepareMockSimulationWithHighFrequency();
		
		for(int i = 0; i < 3; i++){
			Assert.assertTrue(controlUnit.getGeneratorPowerInMW() < GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
		}
	}
	
	private void prepareMockSimulationWithHighFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(50.1f).thenReturn(55f).thenReturn(60f);
	}
	
	@Test
	public void PowerIsEqualsToPowerAtRequiredFrequencyWhenFrequencyIsEqualToRequired(){
		prepareMockSimulationWithNormalFrequency();
		
		Assert.assertTrue(controlUnit.getGeneratorPowerInMW() == GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithNormalFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(GlobalConstatnts.STANDART_FREQUENCY);
	}
	
	@Test 
	public void DoesPowerNotLessThanGeneratorMinimalTechnilogyPower(){
		prepareMockSimulationWithTooHightFrequency();
		
		Assert.assertTrue(controlUnit.getGeneratorPowerInMW() >= generator.getMinimalPowerInMW());
	}
	
	private void prepareMockSimulationWithTooHightFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(1000f);
	}
	
	@Test 
	public void DoesPowerNotHigherThanGeneratorNomimalPower(){
		prepareMockSimulationWithTooLowtFrequency();
			
		Assert.assertTrue(controlUnit.getGeneratorPowerInMW() <= generator.getNominalPowerInMW());
	}
	
	private void prepareMockSimulationWithTooLowtFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(0.00000000001f);
	}
}
