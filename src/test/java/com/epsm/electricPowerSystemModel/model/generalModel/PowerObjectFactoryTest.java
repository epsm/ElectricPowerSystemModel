package com.epsm.electricPowerSystemModel.model.generalModel;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.PowerObject;
import com.epsm.electricPowerSystemModel.model.consumption.ScheduledLoadConsumer;
import com.epsm.electricPowerSystemModel.model.consumption.ScheduledLoadConsumerCreationParametersStub;
import com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumer;
import com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumerCreationParametersStub;
import com.epsm.electricPowerSystemModel.model.dispatch.CreationParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationCreationParametersStub;

public class PowerObjectFactoryTest {
	private PowerObjectFactory factory;
	private Map<Long, PowerObject> powerSystemObjects;
	private ElectricPowerSystemSimulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private PowerObject object;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp(){
		powerSystemObjects = new HashMap<Long, PowerObject>();
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		simulation = new ElectricPowerSystemSimulationImpl(timeService, dispatcher);
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
		object = powerSystemObjects.get(0L);
		
		Assert.assertTrue(object instanceof PowerStation);
	}
	
	@Test
	public void factoryCreatesShockLoadConsumerFromPowerStationCreationParametersStub(){
		factory.create(new ShockLoadConsumerCreationParametersStub());
		object = powerSystemObjects.get(0L);
		
		Assert.assertTrue(object instanceof ShockLoadConsumer);
	}
	
	@Test
	public void factoryCreatesScheduledLoadConsumerFromPowerStationCreationParametersStub(){
		factory.create(new ScheduledLoadConsumerCreationParametersStub());
		object = powerSystemObjects.get(0L);
		
		Assert.assertTrue(object instanceof ScheduledLoadConsumer);
	}
	
	@Test
	public void exceptionInCreateMethodIfCreationParametersUnknown(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("PowerObjectFactory: UncnownPowerObjectCreationParameters"
	    		+ " is unsupported.");
	
	    factory.create(new UncnownPowerObjectCreationParameters());
	}
	
	private class UncnownPowerObjectCreationParameters extends CreationParameters{
	}
}
