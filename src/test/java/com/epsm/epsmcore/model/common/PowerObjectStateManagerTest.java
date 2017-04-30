package com.epsm.epsmcore.model.common;

import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Constants;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmcore.model.simulation.TimeService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PowerObjectStateManagerTest {

	private TimeService timeService;
	private Dispatcher dispatcher;
	private Simulation simulation;
	private PowerObjectStateManager stateManager;
	private Parameters parameters;
	private State state;
	private LocalDateTime timeInTest = LocalDateTime.of(2000, 01, 01, 00, 00);

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Before
	public void setUp() throws Exception{
		timeService = mock(TimeService.class);
		when(timeService.getCurrentDateTime()).thenReturn(timeInTest);
		dispatcher = mock(Dispatcher.class);
		simulation = new Simulation(timeInTest);
		parameters = mock(Parameters.class);
		stateManager = spy(new StateManager(timeService, dispatcher));
		new TetsPoweObject(simulation, dispatcher, parameters, stateManager);
		state = mock(State.class);
	}

	private class StateManager extends PowerObjectStateManager {

		public StateManager(TimeService timeService, Dispatcher dispatcher) {
			super(timeService, dispatcher);
		}

		@Override
		protected boolean doSend(List states) {
			return false;
		}

		@Override
		protected boolean doRegister(Parameters parameters) {
			return false;
		}
	}

	private  class TetsPoweObject extends PowerObject {

		public TetsPoweObject(Simulation simulation, Dispatcher dispatcher, Parameters parameters, PowerObjectStateManager stateManager) {
			super(simulation, dispatcher, parameters, stateManager);
		}

		@Override
		protected float calculatPowerBalance() {
			return 0;
		}

		@Override
		public State getState() {
			return state;
		}
	}

	@Test
	public void registersWithDispatcherIfItIsNotRegistered(){
		stateManager.manageStates();
		
		verify(stateManager).doRegister(parameters);
	}
	
	@Test
	public void doesNotTryRegisterWithDispatcherIfAlreadyRegistered(){
		makeRegistration();
		makeSendNextMessage();

		verify(stateManager).doRegister(parameters);
	}
	
	private void makeRegistration(){
		when(stateManager.doRegister(parameters)).thenReturn(true);
		stateManager.manageStates();
	}
	
	private void makeSendNextMessage(){
		addToSystemTimeValueMoreThanPauseBetweenSendingMessages();
		stateManager.manageStates();
	}
	
	private void addToSystemTimeValueMoreThanPauseBetweenSendingMessages(){
		timeInTest = timeInTest.plusSeconds(Constants.PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS + 1);
		when(timeService.getCurrentDateTime()).thenReturn(timeInTest);
	}
	
	@Test
	public void sendsStatesToDispatcherIfPauseBetweenSendingMoreThenSet(){
		makeSendState();

		verify(stateManager).doSend(asList(state));
	}
	
	private void makeSendState(){
		makeRegistration();
		stateManager.acceptState(state);
		addToSystemTimeValueMoreThanPauseBetweenSendingMessages();
		stateManager.manageStates();
	}
	
	@Test
	public void doesNotSendStatesIfPauseBetweenSendingLessThenSet(){
		makeRegistration();
		prepareManagerToSendStateButMakePauseLessThenNecessaryForSending();
				
		verify(stateManager, never()).doSend(any());
	}
	
	private void prepareManagerToSendStateButMakePauseLessThenNecessaryForSending(){
		stateManager.acceptState(state);
		addToSystemTimeValueLessThanPauseBetweenSending();
		stateManager.manageStates();
	}
	
	private void addToSystemTimeValueLessThanPauseBetweenSending(){
		timeInTest = timeInTest.plusSeconds(
				Constants.PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS - 1);
		when(timeService.getCurrentDateTime()).thenReturn(timeInTest);
	}
	
	@Test
	public void doesNotSendPreviouslySentMessageAgain(){
		makeSendState();
		makeSendStateButNotAddNewStates();
		
		verify(stateManager).doSend(any());
	}
	
	private void makeSendStateButNotAddNewStates(){
		addToSystemTimeValueMoreThanPauseBetweenSendingMessages();
		stateManager.manageStates();
	}
}
