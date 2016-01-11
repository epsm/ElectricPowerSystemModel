package com.epsm.electricPowerSystemModel.model.generalModel;

import static org.mockito.Mockito.mock;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.consumption.ScheduledLoadConsumer;
import com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumer;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingObject;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;

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
		factory = new DispatchingObjectsSourceFactory(timeService, dispatcher);
		source = factory.createSource();
	}
	
	@Test
	public void sourceKeepsThreeObjects(){
		Assert.assertEquals(3, source.getDispatchingObjects().size());
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
