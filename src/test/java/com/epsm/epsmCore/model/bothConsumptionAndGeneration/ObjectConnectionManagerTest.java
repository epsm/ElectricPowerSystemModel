package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.epsm.epsmCore.model.constantsForTests.TestsConstants;
import com.epsm.epsmCore.model.consumption.ConsumerParametersStub;
import com.epsm.epsmCore.model.consumption.ConsumerState;
import com.epsm.epsmCore.model.consumption.ConsumptionPermissionStub;
import com.epsm.epsmCore.model.consumption.ShockLoadConsumer;
import com.epsm.epsmCore.model.dispatch.Command;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.State;
import com.epsm.epsmCore.model.generalModel.Constants;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.epsmCore.model.generalModel.TimeService;
import com.epsm.epsmCore.model.generation.PowerStationGenerationSchedule;
import com.epsm.epsmCore.model.generation.PowerStationState;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ShockLoadConsumer.class)
public class ObjectConnectionManagerTest{
	private ObjectConnectionManager manager;
	private ElectricPowerSystemSimulation simulation;
	private PowerObject object;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private Command command;
	private ConsumerParametersStub parameters;
	private ArgumentCaptor<State> captor;
	private final LocalDateTime START_TIME = LocalDateTime.of(2000, 01, 01, 00, 00);
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp(){
		State state = new ConsumerState(0, START_TIME, LocalDateTime.MIN, 0);
		timeService = mock(TimeService.class);
		when(timeService.getCurrentDateTime()).thenReturn(START_TIME);
		dispatcher = mock(Dispatcher.class);
		simulation = new ElectricPowerSystemSimulationImpl(timeService, dispatcher,
				TestsConstants.START_DATETIME);
		parameters = new ConsumerParametersStub(0, START_TIME, LocalDateTime.MIN);
		object = PowerMockito.spy(new ShockLoadConsumer(simulation, timeService, dispatcher, parameters));
		when(object.getState()).thenReturn(state);
		command = new ConsumptionPermissionStub(0, LocalDateTime.MIN, 
				simulation.getDateTimeInSimulation());
		manager = new ObjectConnectionManager(timeService, dispatcher, object);
		captor = ArgumentCaptor.forClass(State.class);
	}

	@Test
	public void exceptionInConstructorIfTimeServiseIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("ObjectConnectionManager constructor: timeService must not be null.");
	
	    manager = new ObjectConnectionManager(null, dispatcher, object);
	}
	
	@Test
	public void exceptionInConstructorIfDispatcherIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("ObjectConnectionManager constructor: dispatcher must not be null.");
	
	    manager = new ObjectConnectionManager(timeService, null, object);
	}
	
	@Test
	public void exceptionInConstructorIfPowerObjectIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("ObjectConnectionManager constructor: PowerObject must not be null.");
	
	    manager = new ObjectConnectionManager(timeService, dispatcher, null);
	}
	
	@Test
	public void triesConnectToDispatcher(){
		manager.manageConnection();
		
		verify(dispatcher).establishConnection((isA(ConsumerParametersStub.class)));
	}
	
	@Test
	public void doesNotTryConnectToDispatcherIfConnectionEstablishedAndActive(){
		makeConnectionEstablished();
		addToSystemTimeValueLessThanAcceptablePauseBetweenDispatcherMessages();
		manager.manageConnection();
		
		verify(dispatcher).establishConnection(isA(ConsumerParametersStub.class));
	}
	
	private void makeConnectionEstablished(){
		manager.manageConnection();
		manager.executeCommand(command);
	}
	
	private void addToSystemTimeValueLessThanAcceptablePauseBetweenDispatcherMessages(){
		when(timeService.getCurrentDateTime()).thenReturn(START_TIME.plusSeconds(
				(long)(Constants.CONNECTION_TIMEOUT_IN_SECONDS * 0.9)));
	}
	
	@Test
	public void sendsStatesToDispatcherIfPauseBetweenSendingMoreThenSet(){
		makeObjectSendState();
		
		verify(dispatcher).acceptState(captor.capture());
		Assert.assertTrue(captor.getValue().getClass() == ConsumerState.class);
	}
	
	private void makeObjectSendState(){
		makeConnectionEstablished();
		addToSystemTimeValueMoreThanPauseBetweenSendingMessages();
		manager.manageConnection();
	}
	
	private void addToSystemTimeValueMoreThanPauseBetweenSendingMessages(){
		when(timeService.getCurrentDateTime()).thenReturn(START_TIME.plusSeconds(
			(Constants.PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS + 1)));
	}
	
	@Test
	public void doesNotSendsStatesToDispatcherIfPauseBetweenSendingLessThenSet(){
		makeConnectionEstablished();
		addToSystemTimeValueLessThanPauseBetweenSending();
		manager.manageConnection();
		
		verify(dispatcher, never()).acceptState(any());
	}
	
	private void addToSystemTimeValueLessThanPauseBetweenSending(){
		when(timeService.getCurrentDateTime()).thenReturn(START_TIME.plusSeconds((long)
			(Constants.PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS * 0.9)));
	}
	
	@Test
	public void TriesConnectToDispatcherAgainIfConnectionLost(){
		makeConnectionEstablished();
		addToSystemTimeValueMoreThanAcceptablePauseBetweenDispatcherMessages();
		manager.manageConnection();
		
		verify(dispatcher, times(2)).establishConnection(isA(ConsumerParametersStub.class));
	}
	
	private void addToSystemTimeValueMoreThanAcceptablePauseBetweenDispatcherMessages(){
		when(timeService.getCurrentDateTime()).thenReturn(START_TIME.plusSeconds(
			(Constants.CONNECTION_TIMEOUT_IN_SECONDS * 2)));
	}
	
	@Test
	public void passesRightCommandToObject(){
		makeConnectionEstablished();
		
		verify(object).performDispatcheCommand(isA(ConsumptionPermissionStub.class));
	}
	
	@Test
	public void doNothingIfAcceptedMessageClassIsNotExpected(){
		command = new  PowerStationGenerationSchedule(0, LocalDateTime.MIN, LocalDateTime.MIN, 1);
		
		makeConnectionEstablished();
		
		verify(object, never()).executeCommand(isA(ConsumptionPermissionStub.class));
	}
	
	@Test
	public void doNothingIfAcceptedMessageIsNull(){
		command = null;
		
		makeConnectionEstablished();
		
		verify(object, never()).executeCommand(isA(ConsumptionPermissionStub.class));
	}
	
	@Test
	public void exceptionIfObjectGetStateReturnedNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("returned null instead ConsumerState.");
	    
	    when(object.getState()).thenReturn(null);
	    
	    makeObjectSendState();
	}
	
	@Test
	public void exceptionIfObjectGetStateReturnedWrongStateClass(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("returned PowerStationState instead ConsumerState.");
	    
	    PowerStationState state = new PowerStationState(0, START_TIME, LocalDateTime.MIN, 1, 0);
	    when(object.getState()).thenReturn(state);
	    
	    makeObjectSendState();
	}
}
