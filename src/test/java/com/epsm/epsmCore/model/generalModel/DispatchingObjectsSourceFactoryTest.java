package com.epsm.epsmCore.model.generalModel;

import static org.mockito.Mockito.mock;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.epsmCore.model.constantsForTests.TestsConstants;
import com.epsm.epsmCore.model.consumption.ScheduledLoadConsumer;
import com.epsm.epsmCore.model.consumption.ShockLoadConsumer;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.DispatchingObject;
import com.epsm.epsmCore.model.generation.PowerStation;

public class DispatchingObjectsSourceFactoryTest {
	private TimeService timeService;
	private Dispatcher dispatcher;
	private DispatchingObjectsSourceFactory factory;
	private DispatchingObjectsSource source;
	private Map<Long, DispatchingObject> objects;
	
	@Before
	public void setUp(){
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		factory = new DispatchingObjectsSourceFactory(timeService, dispatcher,
				TestsConstants.START_DATETIME);
		source = factory.createSource();
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void exceptionInConstructorIfTimeServiceIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("DispatchingObjectsSourceFactory constructor:"
	    		+ " timeService can't be null.");
	
	    new DispatchingObjectsSourceFactory(null, dispatcher, TestsConstants.START_DATETIME);
	}
	
	@Test
	public void exceptionInConstructorIfDispatcherIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("DispatchingObjectsSourceFactory constructor:"
	    		+ " dispatcher can't be null.");
	
	    new DispatchingObjectsSourceFactory(timeService, null, TestsConstants.START_DATETIME);
	}
	
	@Test
	public void exceptionInConstructorIfStartDateTimeIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("DispatchingObjectsSourceFactory constructor:"
	    		+ " startDateTime can't be null.");
	
	    new DispatchingObjectsSourceFactory(timeService, dispatcher, null);
	}
	
	
	@Test
	public void sourceContainsAllObjects(){
		int powerStation = 1;
		int shockLoadConsumer = 4;
		int scheduledLoadConsumer = 8;
		int total = powerStation + shockLoadConsumer + scheduledLoadConsumer;
		
		Assert.assertEquals(total, source.getDispatchingObjects().size());
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
		Assert.assertTrue(objectContainsClass(ShockLoadConsumer.class));
	}
	
	@Test
	public void sourceKeepsScheduledLoadConsumer(){
		Assert.assertTrue(objectContainsClass(ScheduledLoadConsumer.class));
	}
}
