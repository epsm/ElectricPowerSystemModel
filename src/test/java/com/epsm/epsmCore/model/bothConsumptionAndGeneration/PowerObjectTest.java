package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import com.epsm.epsmCore.model.consumption.ConsumerParametersStub;
import com.epsm.epsmCore.model.consumption.ShockLoadConsumer;
import com.epsm.epsmCore.model.dispatch.Command;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.Parameters;
import com.epsm.epsmCore.model.dispatch.State;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.TimeService;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PowerObjectMessageManager.class)
public class PowerObjectTest{
	private ElectricPowerSystemSimulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private ConsumerParametersStub parameters;
	private State state;
	private PowerObjectMessageManager messageManager;
	private PowerObject powerObject;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException{
		
		simulation = mock(ElectricPowerSystemSimulation.class);
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		parameters = new ConsumerParametersStub(0, LocalDateTime.MIN, LocalDateTime.MIN);
		state = mock(State.class);
		messageManager = PowerMockito.mock(PowerObjectMessageManager.class);
		powerObject = new ShockLoadConsumer(simulation, timeService, dispatcher, parameters);
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
		public AbstractImpl(ElectricPowerSystemSimulation simulation, TimeService timeService,
				Dispatcher dispatcher, Parameters parameters) {
			super(simulation, timeService, dispatcher, parameters);
		}

		@Override
		public float calculateCurrentPowerBalance() {
			return 0;
		}

		@Override
		public State getState() {
			return state;
		}

		@Override
		protected void performDispatcherCommand(Command command) {
		}
	}
	
	@Test
	public void passesStateToMessageManagerIfItIsTime(){
		prepareTimeForWorkingWithState();
		powerObject.calculatePowerBalance();
		
		verify(messageManager).acceptState(any());
	}
	
	private void prepareTimeForWorkingWithState(){
		when(simulation.getDateTimeInSimulation()).thenReturn(LocalDateTime.of(2000, 01, 01, 13, 40, 00, 000));
	}
	
	@Test
	public void doesNotPassStateToMessageManagerIfItIsNotTime(){
		when(simulation.getDateTimeInSimulation()).thenReturn(LocalDateTime.of(2000, 01, 01, 13, 0, 0, 1));
		powerObject.calculatePowerBalance();
		verify(messageManager, never()).acceptState(any());
		
		when(simulation.getDateTimeInSimulation()).thenReturn(LocalDateTime.of(2000, 01, 01, 13, 0, 1, 0));
		powerObject.calculatePowerBalance();
		verify(messageManager, never()).acceptState(any());
		
		when(simulation.getDateTimeInSimulation()).thenReturn(LocalDateTime.of(2000, 01, 01, 13, 1, 0, 0));
		powerObject.calculatePowerBalance();
		verify(messageManager, never()).acceptState(any());
	}
}
