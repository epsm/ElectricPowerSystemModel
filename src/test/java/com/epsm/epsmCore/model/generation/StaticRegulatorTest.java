package com.epsm.epsmCore.model.generation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.Constants;

public class StaticRegulatorTest {
	private ElectricPowerSystemSimulation simulation;
	private Generator generator;
	private StaticRegulator staticRegulator;
	private final float GENERATOR_POWER_AT_REQUAIRED_FREQUENCY = 100;
	private final float NOMINAL_POWER_IN_MW = 150;
	private final float MINIMAL_POWER_IN_MW = 50;
	private final float POWER_LESS_THAN_NOMINAL_IN_MW = 10;
	private final float LOW_FREQUENCY_1 = Constants.STANDART_FREQUENCY - 0.1f;
	private final float LOW_FREQUENCY_2 = Constants.STANDART_FREQUENCY - 2f;
	private final float LOW_FREQUENCY_3 = Constants.STANDART_FREQUENCY - 10f;
	private final float HIGH_FREQUENCY_1 = Constants.STANDART_FREQUENCY + 0.1f;
	private final float HIGH_FREQUENCY_2 = Constants.STANDART_FREQUENCY + 2f;
	private final float HIGH_FREQUENCY_3 = Constants.STANDART_FREQUENCY + 10f;
	private final float TOO_HIGH_FREQUENCY = Constants.STANDART_FREQUENCY + 100000f;
	private final float TOO_LOW_FREQUENCY = 0.0000001f;
	
	@Before
	public void setUp(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		generator = new Generator(simulation, 1);
		staticRegulator = new StaticRegulator(simulation, generator);
		
		staticRegulator.setPowerAtRequiredFrequency(GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
		
		generator.setStaticRegulator(staticRegulator);
		generator.setMinimalPowerInMW(MINIMAL_POWER_IN_MW);
		generator.setNominalPowerInMW(NOMINAL_POWER_IN_MW);
	}

	@Test
	public void getGeneratorPowerInMWReturnsZeroIfPowerAtRequiredFrequencyLessThanGeneratorMinimal(){
		staticRegulator.setPowerAtRequiredFrequency(POWER_LESS_THAN_NOMINAL_IN_MW);
		
		Assert.assertEquals(0, staticRegulator.getGeneratorPowerInMW(), 0);
	}
	
	@Test
	public void PowerIncreasesWhenFrequencyIsLow(){
		prepareMockSimulationWithLowFrequency();

		Assert.assertTrue(staticRegulator.getGeneratorPowerInMW() > GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithLowFrequency(){
		when(simulation.getFrequencyInPowerSystem())
			.thenReturn(LOW_FREQUENCY_1)
			.thenReturn(LOW_FREQUENCY_2)
			.thenReturn(LOW_FREQUENCY_3);
	}
	
	@Test
	public void PowerDecreasesWhenFrequencyIsHight(){
		prepareMockSimulationWithHighFrequency();

		Assert.assertTrue(staticRegulator.getGeneratorPowerInMW() < GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithHighFrequency(){
		when(simulation.getFrequencyInPowerSystem())
			.thenReturn(HIGH_FREQUENCY_1)
			.thenReturn(HIGH_FREQUENCY_2)
			.thenReturn(HIGH_FREQUENCY_3);
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
		when(simulation.getFrequencyInPowerSystem()).thenReturn(TOO_HIGH_FREQUENCY);
	}
	
	@Test 
	public void DoesPowerNotHigherThanGeneratorNomimalPower(){
		prepareMockSimulationWithTooLowtFrequency();
			
		Assert.assertTrue(staticRegulator.getGeneratorPowerInMW() <= generator.getNominalPowerInMW());
	}
	
	private void prepareMockSimulationWithTooLowtFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(TOO_LOW_FREQUENCY);
	}
}
