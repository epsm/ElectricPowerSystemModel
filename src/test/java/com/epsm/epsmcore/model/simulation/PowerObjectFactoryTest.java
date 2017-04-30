package com.epsm.epsmcore.model.simulation;

import com.epsm.epsmcore.model.common.PowerObject;
import com.epsm.epsmcore.model.constantsForTests.TestsConstants;
import com.epsm.epsmcore.model.consumption.RandomLoadConsumer;
import com.epsm.epsmcore.model.consumption.RandomLoadConsumerParameters;
import com.epsm.epsmcore.model.consumption.ScheduledLoadConsumer;
import com.epsm.epsmcore.model.consumption.ScheduledLoadConsumerParameters;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.generation.PowerStation;
import com.epsm.epsmcore.model.generation.PowerStationCreationParametersStub;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class PowerObjectFactoryTest {
	private PowerObjectFactory factory;
	private Map<Long, PowerObject> powerSystemObjects;
	private Simulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private PowerObject object;
	private final long DEFAULT_POWER_OBJECT_NUMBER = 0;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp(){
		powerSystemObjects = new HashMap<Long, PowerObject>();
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		simulation = new ElectricPowerSystemSimulationImpl(timeService, dispatcher,
				TestsConstants.START_DATETIME);
		factory = new PowerObjectFactory(powerSystemObjects, simulation, timeService, dispatcher);
	}

	@Test
	public void exceptionInConstructorIfParametersIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("PowerObjectFactory constructor: powerSystemObjects can't"
	    		+ " be null.");
	
	    new PowerObjectFactory(null, simulation, timeService, dispatcher);
	}
	
	@Test
	public void exceptionInConstructorIfSimulationIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("PowerObjectFactory constructor: simulation can't be null.");
	
	    new PowerObjectFactory(powerSystemObjects, null, timeService, dispatcher);
	}
	
	@Test
	public void exceptionInConstructorIfTimeServiseIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("PowerObjectFactory constructor: timeService can't be null.");
	
	    new PowerObjectFactory(powerSystemObjects, simulation, null, dispatcher);
	}
	
	@Test
	public void exceptionInConstructorIfDispatcherIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("PowerObjectFactory constructor: dispatcher can't be null.");
	
	    new PowerObjectFactory(powerSystemObjects, simulation, timeService, null);
	}
	
	@Test
	public void factoryCreatesPowerStationFromPowerStationCreationParametersStub(){
		factory.create(new PowerStationCreationParametersStub());
		object = powerSystemObjects.get(DEFAULT_POWER_OBJECT_NUMBER);
		
		Assert.assertTrue(object instanceof PowerStation);
	}
	
	@Test
	public void factoryCreatesShockLoadConsumerFromPowerStationCreationParametersStub(){
		factory.create(new RandomLoadConsumerParameters());
		object = powerSystemObjects.get(DEFAULT_POWER_OBJECT_NUMBER);
		
		Assert.assertTrue(object instanceof RandomLoadConsumer);
	}
	
	@Test
	public void factoryCreatesScheduledLoadConsumerFromPowerStationCreationParametersStub(){
		factory.create(new ScheduledLoadConsumerParameters());
		object = powerSystemObjects.get(DEFAULT_POWER_OBJECT_NUMBER);
		
		Assert.assertTrue(object instanceof ScheduledLoadConsumer);
	}
	
	@Test
	public void exceptionInCreateMethodIfCreationParametersUnknown(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("PowerObjectFactory: UncnownPowerObjectCreationParameters"
	    		+ " is unsupported.");
	
	    factory.create(new UncnownPowerObjectCreationParameters());
	}
	
	private class UncnownPowerObjectCreationParameters {
	}
}
