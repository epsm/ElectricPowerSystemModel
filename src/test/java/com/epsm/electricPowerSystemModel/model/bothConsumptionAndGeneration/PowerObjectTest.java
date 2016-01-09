package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;
import com.epsm.electricPowerSystemModel.model.dispatch.State;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class PowerObjectTest{
	private ElectricPowerSystemSimulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private Parameters parameters;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		timeService = mock(TimeService.class);
		dispatcher = mock(Dispatcher.class);
		parameters = mock(Parameters.class);
	}

	@Test
	public void exceptionInConstructorIfSimulationIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("PowerObject constructor: simulation can't be null.");
	
	    new AbstractImpl(null, timeService, dispatcher, parameters);
	}
	
	@Test
	public void exceptionInConstructorIfTimeServiseIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("PowerObject constructor: timeService can't be null.");
	
	    new AbstractImpl(simulation, null, dispatcher, parameters);
	}
	
	@Test
	public void exceptionInConstructorIfDispatcherIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("PowerObject constructor: dispatcher can't be null.");
	
	    new AbstractImpl(simulation, timeService, null, parameters);
	}
	
	@Test
	public void exceptionInConstructorIfParametersIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("PowerObject constructor: parameters can't be null.");
	
	    new AbstractImpl(simulation, timeService, dispatcher, null);
	}

	private class AbstractImpl extends PowerObject{
		public AbstractImpl(ElectricPowerSystemSimulation simulation, TimeService timeService, Dispatcher dispatcher,
				Parameters parameters) {
			super(simulation, timeService, dispatcher, parameters);
		}

		@Override
		public float calculatePowerBalance() {
			return 0;
		}

		@Override
		protected State getState() {
			return null;
		}

		@Override
		protected void performDispatcheCommand(Command command) {
		}
	}
}
