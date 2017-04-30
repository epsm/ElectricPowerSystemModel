package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Constants;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.epsmcore.model.simulation.TimeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class AstaticRegulatorTest {
	private Simulation simulation;
	private AstaticRegulator astaticRegulator;
	private StaticRegulator staticRegulator;
	private Generator generator;
	private LocalDateTime startDateTime = LocalDateTime.of(2000, 01, 01, 00,00);
	private final float GENERATOR_POWER_AT_REQUIRED_FREQUENCY = 100;
	private final float GENERATOR_REGULATION_SPEED_IN_MW_PER_MINUTE = 2;
	private final int GENERATOR_NUMBER = 1;
	private final float NOMINAL_POWER_IN_MW = 200;
	private final float DELTA_MORE_THAN_DEAT_ZONE = Constants.ASTATIC_REGULATION_DEAD_ZONE + 0.1f;
	private final float DELTA_LESS_THAN_DEAT_ZONE = Constants.ASTATIC_REGULATION_DEAD_ZONE - 0.001f;
	
	@Before
	public void setUp(){
		TimeService timeService = new TimeService();
		Dispatcher dispatcher = mock(Dispatcher.class);
		simulation = spy(new ElectricPowerSystemSimulationImpl(timeService, dispatcher, startDateTime));
		generator = new Generator(simulation, GENERATOR_NUMBER);
		astaticRegulator = new AstaticRegulator(simulation, generator);
		staticRegulator = new StaticRegulator(simulation, generator);
		
		generator.setAstaticRegulator(astaticRegulator);
		generator.setStaticRegulator(staticRegulator);
		generator.setNominalPowerInMW(NOMINAL_POWER_IN_MW);
		generator.setReugulationSpeedInMWPerMinute(GENERATOR_REGULATION_SPEED_IN_MW_PER_MINUTE);
		staticRegulator.setPowerAtRequiredFrequency(GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
	}
	
	@Test
	public void increasePowerIfFrequencyIsLow(){
		prepareMockSimulationWithLowFrequency();
		doNextStep();
		
		Assert.assertTrue(
				staticRegulator.getPowerAtRequiredFrequency() > GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
	}
	
	private void doNextStep(){
		simulation.doNextStep();
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
	}
	
	private void prepareMockSimulationWithLowFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(
				(float)(Constants.STANDART_FREQUENCY - DELTA_MORE_THAN_DEAT_ZONE));
	}
	
	@Test
	public void decreasePowerIfFrequencyIsHight(){
		prepareMockSimulationWithHighFrequency();
		doNextStep();
		
		Assert.assertTrue(
				staticRegulator.getPowerAtRequiredFrequency() < GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithHighFrequency(){
		when(simulation.getFrequencyInPowerSystem()).thenReturn(
				(float)(Constants.STANDART_FREQUENCY + DELTA_MORE_THAN_DEAT_ZONE));
	}
	
	@Test
	public void actualGeneratorRegulationSpeedNotMoreThanNominalForGenerator(){
		prepareMockSimulationWithHighFrequency();
		
		float powerAtRequiredFrequency = staticRegulator.getPowerAtRequiredFrequency();
		passOneMinute();
		float nextPowerAtRequiredFrequency = staticRegulator.getPowerAtRequiredFrequency();
		
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
		
		Assert.assertTrue(staticRegulator.getPowerAtRequiredFrequency() 
				== GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithLittleLowerButPermissibleFrequency(){
		when(simulation.getFrequencyInPowerSystem()).
		thenReturn((float)(Constants.STANDART_FREQUENCY - DELTA_LESS_THAN_DEAT_ZONE));
	}
	
	@Test
	public void doNothingIfFrequencyInNonSensivityRangeAndMoreThanZero(){
		prepareMockSimulationWithLittleHigherButPermissibleFrequency();
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
			
		Assert.assertTrue(
				staticRegulator.getPowerAtRequiredFrequency() == GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
	}
	
	private void prepareMockSimulationWithLittleHigherButPermissibleFrequency(){
		when(simulation.getFrequencyInPowerSystem()).
		thenReturn((float)(Constants.STANDART_FREQUENCY + DELTA_LESS_THAN_DEAT_ZONE));
	}
	
	@Test
	public void doNothingIfFrequencyIsLowAndGeneratorPowerIsMaximal(){
		prepareMockSimulationWithLowFrequency();
		generator.setNominalPowerInMW(100);
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				staticRegulator.getPowerAtRequiredFrequency() == GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
	}
	
	@Test
	public void doNothingIfFrequencyIsHighAndGeneratorPowerIsMinimal(){
		prepareMockSimulationWithHighFrequency();
		generator.setMinimalPowerInMW(100);
		astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
		
		Assert.assertTrue(
				staticRegulator.getPowerAtRequiredFrequency() == GENERATOR_POWER_AT_REQUIRED_FREQUENCY);
	}
}
