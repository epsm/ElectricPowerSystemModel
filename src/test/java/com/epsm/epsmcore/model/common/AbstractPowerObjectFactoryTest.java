package com.epsm.epsmcore.model.common;

import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmcore.model.simulation.TimeService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;

public class AbstractPowerObjectFactoryTest {
	private Simulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp(){
		simulation = mock(Simulation.class);
		timeService = mock(TimeService.class);
		dispatcher = mock(Dispatcher.class);
	}

	@Test
	public void exceptionInConstructorIfSimulationIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("AbstractPowerObjectFactory constructor: simulation can't be null.");
	
	    new AbstractImpl(null, timeService, dispatcher);
	}
	
	@Test
	public void exceptionInConstructorIfTimeServiseIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("AbstractPowerObjectFactory constructor: timeService can't be null.");
	
	    new AbstractImpl(simulation, null, dispatcher);
	}
	
	@Test
	public void exceptionInConstructorIfDispatcherIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("AbstractPowerObjectFactory constructor: dispatcher can't be null.");
	
	    new AbstractImpl(simulation, timeService, null);
	}
	
	private class AbstractImpl extends AbstractPowerObjectFactory{
		public AbstractImpl(Simulation simulation, TimeService timeService,
		                    Dispatcher dispatcher) {
			
			super(simulation, timeService, dispatcher);
		}
	}
}
