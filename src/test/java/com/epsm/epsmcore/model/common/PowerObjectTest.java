package com.epsm.epsmcore.model.common;

import com.epsm.epsmcore.model.consumption.ConsumerParameters;
import com.epsm.epsmcore.model.consumption.RandomLoadConsumer;
import com.epsm.epsmCore.model.dispatch.Command;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmcore.model.simulation.TimeService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PowerObjectStateManager.class)
public class PowerObjectTest{
	private Simulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private ConsumerParameters parameters;
	private State state;
	private PowerObjectStateManager messageManager;
	private PowerObject powerObject;
	private final int POWER_OBJECT_ID = 0;
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.MIN;
	private final LocalDateTime REAL_TIMESTAMP = LocalDateTime.MIN;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException{
		
		simulation = mock(Simulation.class);
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		parameters = new ConsumerParameters(POWER_OBJECT_ID, REAL_TIMESTAMP, SIMULATION_TIMESTAMP);
		state = mock(State.class);
		messageManager = PowerMockito.mock(PowerObjectStateManager.class);
		powerObject = new RandomLoadConsumer(simulation, timeService, dispatcher, parameters);
		setPowerObjectManager();
	}
	
	private void setPowerObjectManager() throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException{
		
		Field messageManagerField = PowerObject.class.getDeclaredField("manager");
		
		messageManagerField.setAccessible(true);
		messageManagerField.set(powerObject, messageManager);
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
		public AbstractImpl(Simulation simulation, TimeService timeService,
		                    Dispatcher dispatcher, Parameters parameters) {
			super(simulation, timeService, dispatcher, parameters);
		}

		@Override
		public float calculatPowerBalance() {
			return 0;
		}

		public State getState() {
			return state;
		}

		@Override
		protected void performDispatcherCommand(Command command) {
		}
	}
	
	@Test
	public void passesStateToMessageManagerIfItIsTime(){
		prepareRightTimeForWorkingWithState();
		powerObject.calculatePowerBalance();
		
		verify(messageManager).acceptState(any());
	}
	
	private void prepareRightTimeForWorkingWithState(){
		when(simulation.getDateTimeInSimulation()).thenReturn(LocalDateTime.of(2000, 01, 01, 13, 40, 00, 000));
	}
	
	@Test
	public void doesNotPassStateToMessageManagerIfTimeCorrectExceptNanos(){
		when(simulation.getDateTimeInSimulation()).thenReturn(LocalDateTime.of(2000, 01, 01, 13, 0, 0, 1));
		powerObject.calculatePowerBalance();
		verify(messageManager, never()).acceptState(any());
	}
	
	@Test
	public void doesNotPassStateToMessageManagerIfTimeCorrectExceptSeconds(){
		when(simulation.getDateTimeInSimulation()).thenReturn(LocalDateTime.of(2000, 01, 01, 13, 0, 1, 0));
		powerObject.calculatePowerBalance();
		verify(messageManager, never()).acceptState(any());
	}
	
	@Test
	public void doesNotPassStateToMessageManagerIfTimeCorrectExceptMinutes(){
		when(simulation.getDateTimeInSimulation()).thenReturn(LocalDateTime.of(2000, 01, 01, 13, 1, 0, 0));
		powerObject.calculatePowerBalance();
		verify(messageManager, never()).acceptState(any());
	}
}
