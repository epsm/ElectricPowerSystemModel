package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
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
public class PowerObjectMessageManagerTest{
	private TimeService timeService;
	private Dispatcher dispatcher;
	private ElectricPowerSystemSimulation simulation;
	private ConsumerParametersStub parameters;
	private State state;
	private PowerObject powerObject;
	private Command command;
	private SendingMessageManager<State> sendingManager;
	private DispatcherRegistrationManager registrationManager;
	private PowerObjectMessageManager messageManager;
	private final LocalDateTime START_TIME = LocalDateTime.of(2000, 01, 01, 00, 00);
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		timeService = mock(TimeService.class);
		when(timeService.getCurrentDateTime()).thenReturn(START_TIME);
		dispatcher = mock(Dispatcher.class);
		simulation = new ElectricPowerSystemSimulationImpl(timeService, dispatcher,	START_TIME);
		parameters = new ConsumerParametersStub(0, START_TIME, LocalDateTime.MIN);
		state = new ConsumerState(0, START_TIME, LocalDateTime.MIN, 0);
		powerObject = PowerMockito.mock(ShockLoadConsumer.class);
		when(powerObject.getParameters()).thenReturn(parameters);
		when(powerObject.getState()).thenReturn(state);
		command = new ConsumptionPermissionStub(0, LocalDateTime.MIN, simulation.getDateTimeInSimulation());
		sendingManager = spy(new SendingMessageManager<State>(dispatcher, timeService));
		registrationManager = spy(new DispatcherRegistrationManager(dispatcher, parameters));
		messageManager = new PowerObjectMessageManager(timeService, dispatcher, powerObject);
		setPrivateFielsd();
	}
	
	private void setPrivateFielsd() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		Field sendingManagerField = messageManager.getClass().getDeclaredField("sendingManager");
		Field registrationField = messageManager.getClass().getDeclaredField("registrationManager");
		
		sendingManagerField.setAccessible(true);
		sendingManagerField.set(messageManager, sendingManager);
		registrationField.setAccessible(true);
		registrationField.set(messageManager, registrationManager);
	}

	@Test
	public void exceptionInConstructorIfTimeServiseIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("ObjectConnectionManager constructor: timeService must not be null.");
	
	    messageManager = new PowerObjectMessageManager(null, dispatcher, powerObject);
	}
	
	@Test
	public void exceptionInConstructorIfPowerObjectIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("ObjectConnectionManager constructor: powerObject must not be null.");
	
	    messageManager = new PowerObjectMessageManager(timeService, dispatcher, null);
	}
	
	@Test
	public void doNothingIfAcceptedCommandIsNull(){
		acceptNullCommand();
		
		verify(powerObject, never()).executeCommand(any());
	}
	
	private void acceptNullCommand(){
		messageManager.executeCommand(null);
	}
	
	@Test
	public void doNothingIfAcceptedCommandClassIsNotExpected(){
		acceptWrongCommand();
		
		verify(powerObject, never()).executeCommand(any());
	}
	
	private void acceptWrongCommand(){
		messageManager.executeCommand(new PowerStationGenerationSchedule(0, START_TIME, START_TIME, 1));
	}
	
	@Test
	public void passesRightCommandToObject(){
		acceptRigtCommand();
		
		verify(powerObject).performDispatcherCommand(command);
	}
	
	private void acceptRigtCommand(){
		messageManager.executeCommand(command);
	}
	
	@Test
	public void invocesRegisterWithDispatcherOnDispatcherRegistrationManagerIfItIsNotRegistered(){
		messageManager.manageConnection();
		
		verify(registrationManager).registerWithDispatcher();
	}
	
	@Test
	public void doesNotTryRegisterWithDispatcherIfAlreadyRegistered(){
		makeRegistration();
		addToSystemTimeValueMoreThanPauseBetweenSendingMessages();
		messageManager.manageConnection();
		
		verify(registrationManager).registerWithDispatcher();
	}
	
	private void makeRegistration(){
		when(dispatcher.registerObject(parameters)).thenReturn(true);
		messageManager.manageConnection();
	}
	
	private void addToSystemTimeValueMoreThanPauseBetweenSendingMessages(){
		when(timeService.getCurrentDateTime()).thenReturn(START_TIME.plusSeconds(
			(Constants.PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS + 1)));
	}
	
	@Test
	public void invokeSendStatesOnSendingMessageManagerIfPauseBetweenSendingMoreThenSet(){
		makeObjectSendState();

		verify(sendingManager).sendStates();
	}
	
	private void makeObjectSendState(){
		makeRegistration();
		messageManager.acceptState(state);
		addToSystemTimeValueMoreThanPauseBetweenSendingMessages();
		messageManager.manageConnection();
	}
	
	@Test
	public void doesNotInvokeSendStatesOnSendingMessageManagerIfPauseBetweenSendingLessThenSet(){
		makeRegistration();
		messageManager.acceptState(state);
		addToSystemTimeValueLessThanPauseBetweenSending();
		messageManager.manageConnection();
		
		verify(sendingManager, never()).sendStates();
	}
	
	private void addToSystemTimeValueLessThanPauseBetweenSending(){
		when(timeService.getCurrentDateTime()).thenReturn(START_TIME.plusSeconds((long)
			(Constants.PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS * 0.9)));
	}
	
	@Test
	public void exceptionInAcceptStateMethodIfStateTypeIsWrong(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("PowerStationState received instead ConsumerState.");
	    
	    state = new PowerStationState(0, START_TIME, START_TIME, 1, 0);
	    when(powerObject.getState()).thenReturn(state);
	    
	    makeObjectSendState();
	}
}
