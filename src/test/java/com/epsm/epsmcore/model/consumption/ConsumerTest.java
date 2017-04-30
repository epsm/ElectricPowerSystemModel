package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.PowerObjectStateManager;
import com.epsm.epsmcore.model.common.State;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.generation.PowerStationStateManager;
import com.epsm.epsmcore.model.simulation.Constants;
import com.epsm.epsmcore.model.simulation.Simulation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConsumerTest {

	private Consumer consumer;
	private final float DEGREE_DEPENDENCY_ON_FREQUENCY = 3;
	private final float FREQUENCY = 49.5f;
	private final float LOAD = 100;

	@Before
	public void setUp(){
		ConsumerParameters parameters = mock(ConsumerParameters.class);
		PowerObjectStateManager stateManager = mock(PowerStationStateManager.class);
		consumer = new TestConsumer(null, null, parameters, stateManager);
		when(parameters.getDegreeOnDependingOfFrequency()).thenReturn(DEGREE_DEPENDENCY_ON_FREQUENCY);
	}

	private class TestConsumer extends Consumer {

		public TestConsumer(Simulation simulation, Dispatcher dispatcher, ConsumerParameters parameters, PowerObjectStateManager stateManager) {
			super(simulation, dispatcher, parameters, stateManager);
		}

		@Override
		protected float calculateConsumerPowerBalance() {
			return 0;
		}

		@Override
		public State getState() {
			return null;
		}
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
