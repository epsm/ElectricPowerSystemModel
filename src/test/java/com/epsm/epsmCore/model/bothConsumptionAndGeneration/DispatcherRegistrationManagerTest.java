package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;

import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.Parameters;

public class DispatcherRegistrationManagerTest {
	private DispatcherRegistrationManager manager;
	private Dispatcher dispatcher;
	private Parameters parameters;
	
	@Before
	public void setUp(){
		dispatcher = mock(Dispatcher.class);
		parameters = mock(Parameters.class);
		manager = new DispatcherRegistrationManager(dispatcher, parameters);
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void exceptionInConstructorIfDispatcherIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("DispatcherRegistrationManager constructor: dispatcher can't be null.");
	
	    new DispatcherRegistrationManager(null, parameters);
	}
	
	@Test
	public void exceptionInConstructorIfTimeServiseIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("DispatcherRegistrationManager constructor: parameters can't be null.");
	
	    new DispatcherRegistrationManager(dispatcher, null);
	}
	
	@Test
	public void registerWithDispatcherMethodReturnsTrueIfDispatcherAnswerTrue(){
		when(dispatcher.registerObject(parameters)).thenReturn(true);
		
		Assert.assertTrue(manager.registerWithDispatcher());
	}
	
	@Test
	public void registerWithDispatcherMethodReturnsFalseIfDispatcherAnswerFalse(){
		when(dispatcher.registerObject(parameters)).thenReturn(false);
		
		Assert.assertFalse(manager.registerWithDispatcher());
	}
}
