package com.epsm.electricPowerSystemModel.model.generation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.Constants;

public class StaticRegulatorTest {
	private ElectricPowerSystemSimulation simulation;
	private Generator generator;
	private StaticRegulator staticRegulator;
	private final float GENERATOR_POWER_AT_REQUAIRED_FREQUENCY = 100;
	
	@Before
	public void setUp(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		generator = new Generator(simulation, 1);
		staticRegulator = new StaticRegulator(simulation, generator);
		
		staticRegulator.setPowerAtRequiredFrequency(GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
		
		generator.setStaticRegulator(staticRegulator);
		generator.setMinimalPowerInMW(50);
		generator.setNominalPowerInMW(150);
	}

	@Test
	public void PowerIncreasesWhenFrequencyIsLow(){
		prepareMockSimulationWithLowFrequency();
		
		for(int i = 0; i < 3; i++){
			Assert.assertTrue(staticRegulator.getGeneratorPowerInMW() > GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
		}
	}
	
	private void prepareMockSimulationWithLowFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(49.9f).thenReturn(49f).thenReturn(40f);
	}
	
	@Test
	public void PowerDecreasesWhenFrequencyIsHight(){
		prepareMockSimulationWithHighFrequency();
		
		for(int i = 0; i < 3; i++){
			Assert.assertTrue(staticRegulator.getGeneratorPowerInMW() < GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
		}
	}
	
	private void prepareMockSimulationWithHighFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(50.1f).thenReturn(55f).thenReturn(60f);
	}
	
	@Test
	public void PowerIsEqualsToPowerAtRequiredFrequencyWhenFrequencyIsEqualToRequired(){
		prepareMockSimulationWithNormalFrequency();
		
		Assert.assertTrue(staticRegulator.getGeneratorPowerInMW() == GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithNormalFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(Constants.STANDART_FREQUENCY);
	}
	
	@Test 
	public void DoesPowerNotLessThanGeneratorMinimalPower(){
		prepareMockSimulationWithTooHightFrequency();
		
		Assert.assertTrue(staticRegulator.getGeneratorPowerInMW() >= generator.getMinimalPowerInMW());
	}
	
	private void prepareMockSimulationWithTooHightFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(1000f);
	}
	
	@Test 
	public void DoesPowerNotHigherThanGeneratorNomimalPower(){
		prepareMockSimulationWithTooLowtFrequency();
			
		Assert.assertTrue(staticRegulator.getGeneratorPowerInMW() <= generator.getNominalPowerInMW());
	}
	
	private void prepareMockSimulationWithTooLowtFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(0.00000000001f);
	}
}
