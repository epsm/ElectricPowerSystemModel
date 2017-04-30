package com.epsm.epsmcore.model.common;

import com.epsm.epsmcore.model.consumption.RandomLoadConsumer;
import com.epsm.epsmcore.model.consumption.RandomLoadConsumerParameters;
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

import java.time.LocalDateTime;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PowerObjectStateManager.class)
public class PowerObjectTest{
	private Simulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private RandomLoadConsumerParameters parameters;
	private PowerObjectStateManager stateManager;
	private PowerObject powerObject;
	private State state;
	private final int POWER_OBJECT_ID = 0;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception{
		simulation = mock(Simulation.class);
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		parameters = mock(RandomLoadConsumerParameters.class);
		state = mock(State.class);
		stateManager = PowerMockito.mock(PowerObjectStateManager.class);
		powerObject = new RandomLoadConsumer(simulation, dispatcher, parameters, stateManager);
	}
	
	@Test
	public void passesStateToMessageManagerIfItIsTime(){
		prepareRightTimeForWorkingWithState();
		powerObject.calculatePowerBalance();
		
		verify(stateManager).acceptState(any());
	}
	
	private void prepareRightTimeForWorkingWithState(){
		when(simulation.getDateTimeInSimulation()).thenReturn(LocalDateTime.of(2000, 01, 01, 13, 40, 00, 000));
	}
	
	@Test
	public void doesNotPassStateToMessageManagerIfTimeCorrectExceptNanos(){
		when(simulation.getDateTimeInSimulation()).thenReturn(LocalDateTime.of(2000, 01, 01, 13, 0, 0, 1));
		powerObject.calculatePowerBalance();
		verify(stateManager, never()).acceptState(any());
	}
	
	@Test
	public void doesNotPassStateToMessageManagerIfTimeCorrectExceptSeconds(){
		when(simulation.getDateTimeInSimulation()).thenReturn(LocalDateTime.of(2000, 01, 01, 13, 0, 1, 0));
		powerObject.calculatePowerBalance();
		verify(stateManager, never()).acceptState(any());
	}
	
	@Test
	public void doesNotPassStateToMessageManagerIfTimeCorrectExceptMinutes(){
		when(simulation.getDateTimeInSimulation()).thenReturn(LocalDateTime.of(2000, 01, 01, 13, 1, 0, 0));
		powerObject.calculatePowerBalance();
		verify(stateManager, never()).acceptState(any());
	}
}
