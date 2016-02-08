package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
	private PowerObjectMessageManager messageManager;
	private LocalDateTime timeInTest = LocalDateTime.of(2000, 01, 01, 00, 00);
	private final int POWER_OBJECT_ID = 0;
	private final int QUANTITY_OF_GENERATORS = 1;
	private final float LOAD = 0;
	private final float FREQUENCY = 0;
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.MIN;
	private final LocalDateTime REAL_TIMESTAMP = LocalDateTime.MIN;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception{
		timeService = mock(TimeService.class);
		when(timeService.getCurrentDateTime()).thenReturn(timeInTest);
		dispatcher = mock(Dispatcher.class);
		simulation = new ElectricPowerSystemSimulationImpl(timeService, dispatcher,	timeInTest);
		parameters = new ConsumerParametersStub(POWER_OBJECT_ID, timeInTest, SIMULATION_TIMESTAMP);
		state = new ConsumerState(POWER_OBJECT_ID, timeInTest, SIMULATION_TIMESTAMP, LOAD);
		powerObject = PowerMockito.mock(ShockLoadConsumer.class);
		when(powerObject.getParameters()).thenReturn(parameters);
		when(powerObject.getState()).thenReturn(state);
		command = new ConsumptionPermissionStub(POWER_OBJECT_ID, REAL_TIMESTAMP, 
				simulation.getDateTimeInSimulation());
		messageManager = new PowerObjectMessageManager(timeService, dispatcher, powerObject);
	}

	@Test
	public void exceptionInConstructorIfTimeServiseIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("ObjectConnectionManager constructor: timeService must not be null.");
	
	    messageManager = new PowerObjectMessageManager(null, dispatcher, powerObject);
	}
	
	@Test
	public void exceptionInConstructorIfDispatcherIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("ObjectConnectionManager constructor: dispatcher must not be null.");
	
	    messageManager = new PowerObjectMessageManager(timeService, null, powerObject);
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
		messageManager.verifyCommand(null);
	}
	
	@Test
	public void doNothingIfAcceptedCommandClassIsNotExpected(){
		acceptWrongCommand();
		
		verify(powerObject, never()).executeCommand(any());
	}
	
	private void acceptWrongCommand(){
		messageManager.verifyCommand(new PowerStationGenerationSchedule(POWER_OBJECT_ID, timeInTest, 
				timeInTest, QUANTITY_OF_GENERATORS));
	}
	
	@Test
	public void passesRightCommandToObject(){
		acceptRigtCommand();
		
		verify(powerObject).performDispatcherCommand(command);
	}
	
	private void acceptRigtCommand(){
		messageManager.verifyCommand(command);
	}
	
	@Test
	public void registersWithDispatcherIfItIsNotRegistered(){
		messageManager.manageMessages();
		
		verify(dispatcher).registerObject(parameters);
	}
	
	@Test
	public void doesNotTryRegisterWithDispatcherIfAlreadyRegistered(){
		makeRegistration();
		makeSendNextMessage();
		
		verify(dispatcher).registerObject(parameters);
	}
	
	private void makeRegistration(){
		when(dispatcher.registerObject(parameters)).thenReturn(true);
		messageManager.manageMessages();
	}
	
	private void makeSendNextMessage(){
		addToSystemTimeValueMoreThanPauseBetweenSendingMessages();
		messageManager.manageMessages();
	}
	
	private void addToSystemTimeValueMoreThanPauseBetweenSendingMessages(){
		timeInTest = timeInTest.plusSeconds(
				Constants.PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS + 1);
		when(timeService.getCurrentDateTime()).thenReturn(timeInTest);
	}
	
	@Test
	public void sendsStatesToDispatcherIfPauseBetweenSendingMoreThenSet(){
		makeSendState();

		verify(dispatcher).acceptState(state);
	}
	
	private void makeSendState(){
		makeRegistration();
		messageManager.acceptState(state);
		addToSystemTimeValueMoreThanPauseBetweenSendingMessages();
		messageManager.manageMessages();
	}
	
	@Test
	public void doesNotSendStatesIfPauseBetweenSendingLessThenSet(){
		makeRegistration();
		prepareManagerToSendStateButMakePauseLessThenNecessaryForSending();
				
		verify(dispatcher, never()).acceptState(state);
	}
	
	private void prepareManagerToSendStateButMakePauseLessThenNecessaryForSending(){
		messageManager.acceptState(state);
		addToSystemTimeValueLessThanPauseBetweenSending();
		messageManager.manageMessages();
	}
	
	private void addToSystemTimeValueLessThanPauseBetweenSending(){
		timeInTest = timeInTest.plusSeconds(
				Constants.PAUSE_BETWEEN_SENDING_MESSAGES_IN_SECONDS - 1);
		when(timeService.getCurrentDateTime()).thenReturn(timeInTest);
	}
	
	@Test
	public void exceptionInAcceptStateMethodIfStateTypeIsWrong(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("PowerStationState received instead ConsumerState.");
	    
	    state = new PowerStationState(POWER_OBJECT_ID, timeInTest, timeInTest, 
	    		QUANTITY_OF_GENERATORS, FREQUENCY);
	    when(powerObject.getState()).thenReturn(state);
	    
	    makeSendState();
	}
	
	@Test
	public void doesNotSendPreviouslySentMessageAgain(){
		makeSendState();
		makeSendStateButNotAddNewStates();
		
		verify(dispatcher).acceptState(any());
	}
	
	private void makeSendStateButNotAddNewStates(){
		addToSystemTimeValueMoreThanPauseBetweenSendingMessages();
		messageManager.manageMessages();
	}
}
