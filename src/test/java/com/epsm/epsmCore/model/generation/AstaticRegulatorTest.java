package com.epsm.epsmCore.model.generation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.generalModel.Constants;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.epsmCore.model.generalModel.TimeService;

public class AstaticRegulatorTest {
	private ElectricPowerSystemSimulation simulation;
	private AstaticRegulator astaticRegulator;
	private SpeedController staticRegulator;
	private Generator generator;
	private LocalDateTime startDateTime = LocalDateTime.of(2000, 01, 01, 00,00);
	private final float GENERATOR_POWER_AT_REQUIRED_FREQUENCY = 100;
	private final float GENERATOR_REGULATION_SPEED_IN_MW_PER_MINUTE = 2;
	
	@Before
	public void setUp(){
		TimeService timeService = new TimeService();
		Dispatcher dispatcher = mock(Dispatcher.class);
		simulation = spy(new ElectricPowerSystemSimulationImpl(timeService, dispatcher, startDateTime));
		generator = new Generator(simulation, 1);
		astaticRegulator = new AstaticRegulator(simulation, generator);
		staticRegulator = new SpeedController(simulation, generator);
		
		generator.setAstaticRegulator(astaticRegulator);
		generator.setStaticRegulator(staticRegulator);
		generator.setNominalPowerInMW(200);
		generator.setReugulationSpeedInMWPerMinute(GENERATOR_REGULATION_SPEED_IN_MW_PER_MINUTE);
		staticRegulator.setGenerationAtGivenFrequency(GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
	}
	
	@Test
	public void increasePowerIfFrequencyIsLow(){
		prepareMockSimulationWithLowFrequency();
		doNextStep();
		
		Assert.assertTrue(
				staticRegulator.getGenerationAtGivenFrequency() > GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
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
				staticRegulator.getGenerationAtGivenFrequency() < GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithHighFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(
				(float)(Constants.STANDART_FREQUENCY + 0.1));
	}
	
	@Test
	public void actualGeneratorRegulationSpeedNotMoreThanNominalForGenerator(){
		prepareMockSimulationWithHighFrequency();
		
		float powerAtRequiredFrequency = staticRegulator.getGenerationAtGivenFrequency();
		passOneMinute();
		float nextPowerAtRequiredFrequency = staticRegulator.getGenerationAtGivenFrequency();
		
		Assert.assertNotEquals(powerAtRequiredFrequency, nextPowerAtRequiredFrequency, 0);
		Assert.assertEquals(powerAtRequiredFrequency, nextPowerAtRequiredFrequency, 
				GENERATOR_REGULATION_SPEED_IN_MW_PER_MINUTE);
	}
	
	private void passOneMinute(){
		when(simulation.getDateTimeInSimulation()).thenReturn(startDateTime.plusMinutes(1));
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
	}
	
	@Test
	public void doNothingIfFrequencyInNonSensivityRangeAndLessThanZero(){
		prepareMockSimulationWithLittleLowerButPermissibleFrequency();
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(staticRegulator.getGenerationAtGivenFrequency() 
				== GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
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
				staticRegulator.getGenerationAtGivenFrequency() == GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
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
				staticRegulator.getGenerationAtGivenFrequency() == GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
	}
	
	@Test
	public void doNothingIfFrequencyIsHighAndGeneratorPowerIsMinimal(){
		prepareMockSimulationWithHighFrequency();
		generator.setMinimalPowerInMW(100);
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				staticRegulator.getGenerationAtGivenFrequency() == GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
	}
}
