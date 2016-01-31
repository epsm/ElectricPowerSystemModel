package com.epsm.epsmCore.model.generation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.epsmCore.model.generalModel.Constants;

public class SpeedControllerTest {
	private Generator generator;
	private SpeedController controller;
	private final float POWER_AT_GIVEN_FREQUENCY = 200;
	private final float GENERATOR_NOMINAL_POWER = 300;
	private final float GENERATOR_MINIMAL_TECHNOLOGY_POWER = 100;
	
	@Before
	public void setUp(){
		generator = mock(Generator.class);
		when(generator.getMinimalPowerInMW()).thenReturn(GENERATOR_MINIMAL_TECHNOLOGY_POWER);
		when(generator.getNominalPowerInMW()).thenReturn(GENERATOR_NOMINAL_POWER);
		controller = new SpeedController(generator);
		controller.setGenerationAtGivenFrequency(POWER_AT_GIVEN_FREQUENCY);
	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionInConstructorIfGeneratorIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("SpeedController: generator must not be null");
		
		new SpeedController(null);
	}
	
	@Test
	public void getGenerationInMWReturnsZeroIfGenerationAtGivenFrequencyIsZero(){
		controller.setGenerationAtGivenFrequency(0);
		
		float power = controller.getGenerationInMW(Constants.STANDART_FREQUENCY);
		
		Assert.assertEquals(0, power, 0);
	}
	
	@Test
	public void generationIncreasesWhenFrequencyIsLow(){
		float lowFrequency = getLowFrequency();
		
		float power = controller.getGenerationInMW(lowFrequency);
		
		Assert.assertTrue(power > POWER_AT_GIVEN_FREQUENCY);
	}
	
	private float getLowFrequency(){
		return Constants.STANDART_FREQUENCY - 1;
	}
	
	@Test
	public void generationDecreasesWhenFrequencyIsHight(){
		float highFrequency = getHighFrequency();
		
		float power = controller.getGenerationInMW(highFrequency);
		
		Assert.assertTrue(power < POWER_AT_GIVEN_FREQUENCY);
	}
	
	private float getHighFrequency(){
		return Constants.STANDART_FREQUENCY + 1;
	}
	
	
	@Test
	public void PgenerationIsEqualsToGenerationAtGifenFrequencyWhenFrequencyNormal(){
		float normalFrequency = getNormalFrequency();
		
		float power = controller.getGenerationInMW(normalFrequency);
		
		Assert.assertTrue(power == POWER_AT_GIVEN_FREQUENCY);
	}
	
	private float getNormalFrequency(){
		return Constants.STANDART_FREQUENCY;
	}
	
	@Test 
	public void powerNotLessThanGeneratorMinimalTechnology(){
		float tooHighFrequency = getTooHightFrequency();
		
		float power = controller.getGenerationInMW(tooHighFrequency);
		
		Assert.assertTrue(power >= GENERATOR_MINIMAL_TECHNOLOGY_POWER);
	}
	
	private float getTooHightFrequency(){
		return Float.MAX_VALUE;
	}
	
	@Test 
	public void powerNotHigherThanGeneratorMinimalTechnology(){
		float tooLowFrequency = getTooLowtFrequency();
		
		float power = controller.getGenerationInMW(tooLowFrequency);

		Assert.assertTrue(power <= GENERATOR_NOMINAL_POWER);
	}
	
	private float getTooLowtFrequency(){
		return 0.000000001f;
	}
}
