package com.epsm.electricPowerSystemModel.model.consumption;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.generalModel.Constants;

public class ConsumerTest {
	private Consumer consumer;
	private final float DEGREE_DEPENDENCY_ON_FREQUENCY = 3;
	private final float FREQUENCY = 49.5f;
	private final float LOAD = 100;
	
	@Before
	public void setUp(){
		consumer = mock(Consumer.class);

		when(consumer.calculateLoadCountingFrequency(anyFloat(), anyFloat())).thenCallRealMethod();
		consumer.degreeOnDependingOfFrequency = DEGREE_DEPENDENCY_ON_FREQUENCY;
	}
	
	@Test
	public void isCalculatedLoadCountingFrequencyEqualsToExpected(){
		float expectedLoad = calculateLoadCountingFrequency(LOAD, FREQUENCY);
		float calculatedLoad = consumer.calculateLoadCountingFrequency(LOAD, FREQUENCY);
		
		Assert.assertEquals(expectedLoad, calculatedLoad, 0);
	}
	
	private float calculateLoadCountingFrequency(float load, float frequency){
		return (float)Math.pow((frequency / Constants.STANDART_FREQUENCY),
				DEGREE_DEPENDENCY_ON_FREQUENCY) * load;
	}
}
