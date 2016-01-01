package com.epsm.electricPowerSystemModel.model.generalModel;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;
import com.epsm.electricPowerSystemModel.model.dispatch.Message;

public class PowerObjectTest{
	private ElectricPowerSystemSimulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void initialize(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		timeService = mock(TimeService.class);
		dispatcher = mock(Dispatcher.class);
	}

	@Test
	public void exceptionInConstructorIfSimulationIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerObject constructor: simulation can't be null.");
	
	    new AbstractImpl(null, timeService, dispatcher);
	}
	
	@Test
	public void exceptionInConstructorIfTimeServiseIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerObject constructor: timeService can't be null.");
	
	    new AbstractImpl(simulation, null, dispatcher);
	}
	
	@Test
	public void exceptionInConstructorIfDispatcherIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerObject constructor: dispatcher can't be null.");
	
	    new AbstractImpl(simulation, timeService, null);
	}

	private class AbstractImpl extends PowerObject{
		public AbstractImpl(ElectricPowerSystemSimulation simulation, TimeService timeService,
				Dispatcher dispatcher) {
			
			super(simulation, timeService, dispatcher);
		}

		@Override
		public void executeCommand(Message message) {
		}

		@Override
		public Message getState() {
			return null;
		}

		@Override
		public Message getParameters() {
			return null;
		}
	}
}
