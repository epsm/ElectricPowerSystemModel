package com.epsm.electricPowerSystemModel.model.generation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.electricPowerSystemModel.model.generalModel.Constants;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;
import com.epsm.electricPowerSystemModel.model.generation.AstaticRegulator;
import com.epsm.electricPowerSystemModel.model.generation.StaticRegulator;
import com.epsm.electricPowerSystemModel.model.generation.Generator;

public class AstaticRegulatorTest {
	private ElectricPowerSystemSimulation simulation;
	private AstaticRegulator astaticRegulator;
	private StaticRegulator staticRegulator;
	private Generator generator;
	private final float GENERATOR_POWER_AT_REQUAIRED_FREQUENCY = 100;
	private final float GENERATOR_REGULATION_SPEED_IN_MW_PER_MINUTE = 2;
	
	@Before
	public void setUp(){
		TimeService timeService = new TimeService();
		Dispatcher dispatcher = mock(Dispatcher.class);
		simulation = spy(new ElectricPowerSystemSimulationImpl(timeService, dispatcher));
		generator = new Generator(simulation, 1);
		astaticRegulator = new AstaticRegulator(simulation, generator);
		staticRegulator = new StaticRegulator(simulation, generator);
		
		generator.setAstaticRegulator(astaticRegulator);
		generator.setStaticRegulator(staticRegulator);
		generator.setNominalPowerInMW(200);
		generator.setReugulationSpeedInMWPerMinute(GENERATOR_REGULATION_SPEED_IN_MW_PER_MINUTE);
		staticRegulator.setPowerAtRequiredFrequency(GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	@Test
	public void increasePowerIfFrequencyIsLow(){
		prepareMockSimulationWithLowFrequency();
		doNextStep();
		
		Assert.assertTrue(
				staticRegulator.getPowerAtRequiredFrequency() > GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	private void doNextStep(){
		simulation.calculateNextStep();
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
	}
	
	private void prepareMockSimulationWithLowFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(
				(float)(Constants.STANDART_FREQUENCY - 0.1));
	}
	
	@Test
	public void decreasePowerIfFrequencyIsHight(){
		prepareMockSimulationWithHighFrequency();
		doNextStep();
		
		Assert.assertTrue(
				staticRegulator.getPowerAtRequiredFrequency() < GENERATOR_POWER_AT_REQUAIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithHighFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(
				(float)(Constants.STANDART_FREQUENCY + 0.1));
	}
	
	@Test
	public void actualGeneratorRegulationSpeedNotMoreThanNominalForGenerator(){
		prepareMockSimulationWithHighFrequency();
		when(simulation.getTimeInSimulation()).thenReturn(LocalTime.NOON);
		simulation.calculateNextStep();
		when(simulation.getTimeInSimulation()).thenReturn(LocalTime.NOON.plusMinutes(1));
		simulation.calculateNextStep();
		float previousPowerAtRequiredFrequency = staticRegulator.getPowerAtRequiredFrequency();
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
		float curentPowerAtRequiredFrequency = staticRegulator.getPowerAtRequiredFrequency();
		
		Assert.assertNotEquals(previousPowerAtRequiredFrequency, curentPowerAtRequiredFrequency, 0);
		Assert.assertEquals(previousPowerAtRequiredFrequency, curentPowerAtRequiredFrequency, 
				GENERATOR_REGULATION_SPEED_IN_MW_PER_MINUTE);
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
		thenReturn((float)(Constants.STANDART_FREQUENCY - 0.02));
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
		thenReturn((float)(Constants.STANDART_FREQUENCY + 0.02));
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
