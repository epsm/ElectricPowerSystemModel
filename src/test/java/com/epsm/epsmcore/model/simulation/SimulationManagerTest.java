package com.epsm.epsmcore.model.simulation;

import com.epsm.epsmcore.model.constantsForTests.TestsConstants;
import com.epsm.epsmcore.model.consumption.RandomLoadConsumer;
import com.epsm.epsmcore.model.consumption.ScheduledLoadConsumer;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.generation.PowerStation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

import static org.mockito.Mockito.mock;

public class SimulationManagerTest {
	private TimeService timeService;
	private Dispatcher dispatcher;
	private SimulationManager factory;
	private DispatchingObjectsSource source;
	private Map<Long, DispatchingObject> objects;
	
	@Before
	public void setUp(){
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		factory = new SimulationManager(timeService, dispatcher,
				TestsConstants.START_DATETIME);
		source = factory.createSource();
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void exceptionInConstructorIfTimeServiceIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("SimulationManager constructor:"
	    		+ " timeService can't be null.");
	
	    new SimulationManager(null, dispatcher, TestsConstants.START_DATETIME);
	}
	
	@Test
	public void exceptionInConstructorIfDispatcherIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("SimulationManager constructor:"
	    		+ " dispatcher can't be null.");
	
	    new SimulationManager(timeService, null, TestsConstants.START_DATETIME);
	}
	
	@Test
	public void exceptionInConstructorIfStartDateTimeIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("SimulationManager constructor:"
	    		+ " startDateTime can't be null.");
	
	    new SimulationManager(timeService, dispatcher, null);
	}
	
	@Test
	public void sourceContainsAllObjects(){
		int powerStationQuantity = 1;
		int shockLoadConsumerQuantity = 4;
		int scheduledLoadConsumerQuantity = 8;
		int totalQuantity = powerStationQuantity + shockLoadConsumerQuantity + scheduledLoadConsumerQuantity;
		
		Assert.assertEquals(totalQuantity, source.getDispatchingObjects().size());
	}
	
	@Test
	public void sourceKeepsPowerStation(){
		Assert.assertTrue(objectContainsClass(PowerStation.class));
	}
	
	private boolean objectContainsClass(Class<?> targetClass){
		boolean containsTargetClass = false;
		objects = source.getDispatchingObjects();
		
		for(DispatchingObject dispatchingObject: objects.values()){
			if(dispatchingObject.getClass().equals(targetClass)){
				containsTargetClass = true;
				break;
			}
		}
		
		return containsTargetClass;
	}
	
	@Test
	public void sourceKeepsShockLoadConsumer(){
		Assert.assertTrue(objectContainsClass(RandomLoadConsumer.class));
	}
	
	@Test
	public void sourceKeepsScheduledLoadConsumer(){
		Assert.assertTrue(objectContainsClass(ScheduledLoadConsumer.class));
	}
}
