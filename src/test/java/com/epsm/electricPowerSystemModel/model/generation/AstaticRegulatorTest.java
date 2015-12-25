package com.epsm.electricPowerSystemModel.model.generation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;
import com.epsm.electricPowerSystemModel.model.generation.AstaticRegulator;
import com.epsm.electricPowerSystemModel.model.generation.StaticRegulator;
import com.epsm.electricPowerSystemModel.model.generation.Generator;

public class AstaticRegulatorTest {
	private ElectricPowerSystemSimulation simulation;
	private AstaticRegulator astaticRegulator;
	private StaticRegulator staticRegulator;
	private Generator generator;
	private final float GENERATOR_POWER_AT_REQUAIRED_FREQUENCY = 100;
	
	@Before
	public void initialize(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		generator = new Generator(1);
		astaticRegulator = new AstaticRegulator(simulation, generator);
		staticRegulator = new StaticRegulator(simulation, generator);
		
		generator.setAstaticRegulator(astaticRegulator);
		generator.setStaticRegulator(staticRegulator);
		generator.setNominalPowerInMW(200);
		staticRegulator.setPowerAtRequiredFrequency(GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	@Test
	public void increasePowerIfFrequencyIsLow(){
		prepareMockSimulationWithLowFrequency();
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				staticRegulator.getPowerAtRequiredFrequency() > GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithLowFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(
				(float)(GlobalConstatnts.STANDART_FREQUENCY - 0.1));
	}
	
	@Test
	public void decreasePowerIfFrequencyIsHight(){
		prepareMockSimulationWithHighFrequency();
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				staticRegulator.getPowerAtRequiredFrequency() < GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithHighFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(
				(float)(GlobalConstatnts.STANDART_FREQUENCY + 0.1));
	}
	
	@Test
	public void doNothingIfFrequencyInNonSensivityRangeAndLessThanZero(){
		prepareMockSimulationWithLittleLowerButPermissibleFrequency();
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				staticRegulator.getPowerAtRequiredFrequency() == GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);

	}
	
	private void prepareMockSimulationWithLittleLowerButPermissibleFrequency(){
		when(simulation.getFrequencyInPowerSystem()).
		thenReturn((float)(GlobalConstatnts.STANDART_FREQUENCY - 0.02));
	}
	
	@Test
	public void doNothingIfFrequencyInNonSensivityRangeAndMoreThanZero(){
		prepareMockSimulationWithLittleHigherButPermissibleFrequency();
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
			
		Assert.assertTrue(
				staticRegulator.getPowerAtRequiredFrequency() == GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithLittleHigherButPermissibleFrequency(){
		when(simulation.getFrequencyInPowerSystem()).
		thenReturn((float)(GlobalConstatnts.STANDART_FREQUENCY + 0.02));
	}
	
	@Test
	public void doNothingIfFrequencyIsLowAndGeneratorPowerIsMaximal(){
		prepareMockSimulationWithLowFrequency();
		generator.setNominalPowerInMW(100);
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				staticRegulator.getPowerAtRequiredFrequency() == GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	@Test
	public void doNothingIfFrequencyIsHighAndGeneratorPowerIsMinimal(){
		prepareMockSimulationWithHighFrequency();
		generator.setMinimalPowerInMW(100);
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				staticRegulator.getPowerAtRequiredFrequency() == GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
}
