package com.epsm.electricPowerSystemModel.model.dispatch;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumer;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;
import com.epsm.electricPowerSystemModel.model.generalModel.PowerObject;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class ObjectConnectionManagerTest{
	private ObjectConnectionManager manager;
	private ElectricPowerSystemSimulation simulation;
	private PowerObject object;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private Message message;
	private ArgumentCaptor<Message> captor;
	private final LocalDateTime START_TIME = LocalDateTime.of(2000, 01, 01, 00, 00);
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void initialize(){
		simulation = new ElectricPowerSystemSimulationImpl();
		timeService = mock(TimeService.class);
		when(timeService.getCurrentTime()).thenReturn(START_TIME);
		dispatcher = mock(Dispatcher.class);
		object = spy(new ShockLoadConsumer(simulation, timeService, dispatcher));
		message = new ConsumptionPermissionStub(0, LocalDateTime.MIN, simulation.getTimeInSimulation());		
		manager = new ObjectConnectionManager(timeService, dispatcher, object);
		captor = ArgumentCaptor.forClass(Message.class);
	}

	@Test
	public void exceptionInConstructorIfTimeServiseIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("ObjectConnectionManager constructor: timeService must not be null.");
	
	    manager = new ObjectConnectionManager(null, dispatcher, object);
	}
	
	@Test
	public void exceptionInConstructorIfDispatcherIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("ObjectConnectionManager constructor: dispatcher must not be null.");
	
	    manager = new ObjectConnectionManager(timeService, null, object);
	}
	
	@Test
	public void exceptionInConstructorIfPowerObjectIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("ObjectConnectionManager constructor: PowerObject must not be null.");
	
	    manager = new ObjectConnectionManager(timeService, dispatcher, null);
	}
	
	@Test
	public void triesConnectToDispatcher(){
		manager.doRealTimeDependOperation();
		
		verify(dispatcher).acceptMessage((isA(ConsumerParametersStub.class)));
	}
	
	@Test
	public void doesNotTryConnectToDispatcherIfConnectionEstablishedAndActive(){
		manager.doRealTimeDependOperation();
		manager.acceptMessage(message);
		addToSystemTimeValueLessThanAcceptablePauseBetweenDispatcherMessages();
		manager.doRealTimeDependOperation();
		
		verify(dispatcher).acceptMessage(isA(ConsumerParametersStub.class));
	}
	
	private void addToSystemTimeValueLessThanAcceptablePauseBetweenDispatcherMessages(){
		when(timeService.getCurrentTime()).thenReturn(START_TIME.plusSeconds(
				(long)(GlobalConstants.ACCEPTABLE_PAUSE_BETWEEN_MESSAGES_FROM_DISPATCHER_IN_SECCONDS * 0.9)));
	}
	
	@Test
	public void sendsStatesToDispatcherIfPauseBetweenSendingMoreThenSet(){
		manager.doRealTimeDependOperation();
		manager.acceptMessage(message);
		addToSystemTimeValueMoreThanPauseBetweenSendingMessages();
		manager.doRealTimeDependOperation();
		
		verify(dispatcher, times(2)).acceptMessage(captor.capture());
		List<Message> arguments = captor.getAllValues();
		Assert.assertTrue(arguments.get(0).getClass() == ConsumerParametersStub.class);
		Assert.assertTrue(arguments.get(1).getClass() == ConsumerState.class);
	}
	
	private void addToSystemTimeValueMoreThanPauseBetweenSendingMessages(){
		when(timeService.getCurrentTime()).thenReturn(START_TIME.plusSeconds(
				(GlobalConstants.PAUSE_BETWEEN_SENDING_MESSAGES_TO_DISPATCHER_IN_SECCONDS + 1)));
	}
	
	@Test
	public void doesNotSendsStatesToDispatcherIfPauseBetweenSendingLessThenSet(){
		manager.doRealTimeDependOperation();
		manager.acceptMessage(message);
		addToSystemTimeValueLessThanPauseBetweenSending();
		manager.doRealTimeDependOperation();
		
		verify(dispatcher).acceptMessage(isA(ConsumerParametersStub.class));
	}
	
	private void addToSystemTimeValueLessThanPauseBetweenSending(){
		when(timeService.getCurrentTime()).thenReturn(START_TIME.plusSeconds(
				(long) (GlobalConstants.PAUSE_BETWEEN_SENDING_MESSAGES_TO_DISPATCHER_IN_SECCONDS * 0.9)));
	}
	
	@Test
	public void TriesConnectToDispatcherAgainIfConnectionLost(){
		manager.doRealTimeDependOperation();
		manager.acceptMessage(message);
		addToSystemTimeValueMoreThanAcceptablePauseBetweenDispatcherMessages();
		manager.doRealTimeDependOperation();
		
		verify(dispatcher, times(2)).acceptMessage(isA(ConsumerParametersStub.class));
	}
	
	private void addToSystemTimeValueMoreThanAcceptablePauseBetweenDispatcherMessages(){
		when(timeService.getCurrentTime()).thenReturn(START_TIME.plusSeconds(
				(GlobalConstants.ACCEPTABLE_PAUSE_BETWEEN_MESSAGES_FROM_DISPATCHER_IN_SECCONDS * 2)));
	}
	
	@Test
	public void passesRightCommandToObject(){
		manager.doRealTimeDependOperation();
		manager.acceptMessage(message);
		
		verify(object).processDispatcherMessage(isA(ConsumptionPermissionStub.class));
	}
	
	@Test
	public void doNothingIfAcceptedMessageClassIsNotExpected(){
		message = new  PowerStationGenerationSchedule(0, LocalDateTime.MIN, LocalTime.MIN, 1);
		
		manager.doRealTimeDependOperation();
		manager.acceptMessage(message);
		
		verify(object, never()).processDispatcherMessage(isA(ConsumptionPermissionStub.class));
	}
	
	@Test
	public void doNothingIfAcceptedMessageIsNull(){
		message = null;
		
		manager.doRealTimeDependOperation();
		manager.acceptMessage(message);
		
		verify(object, never()).processDispatcherMessage(isA(ConsumptionPermissionStub.class));
	}
	
	@Test
	public void exceptionIfObjectGetStateReturnedNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("returned null instead ConsumerState.");
	    
	    when(object.getState()).thenReturn(null);
	    
	    manager.doRealTimeDependOperation();
	    manager.acceptMessage(message);
	    addToSystemTimeValueMoreThanPauseBetweenSendingMessages();
	    manager.doRealTimeDependOperation();
	}
	
	@Test
	public void exceptionIfObjectGetStateReturnedWrongStateClass(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("returned PowerStationState instead ConsumerState.");
	    
	    PowerStationState state = new PowerStationState(0, START_TIME, LocalTime.MIN, 1, 0);
	    when(object.getState()).thenReturn(state);
	    
	    manager.doRealTimeDependOperation();
	    manager.acceptMessage(message);
	    addToSystemTimeValueMoreThanPauseBetweenSendingMessages();
	    manager.doRealTimeDependOperation();
	}
	
	@Test
	public void exceptionIfObjectGetParametersReturnsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("returned null instead ConsumerParametersStub.");
	    
	    /*
		 * It's look like a Mockito bug. when(timeService.getCurrentTime()).thenReturn(START_TIME) returns
		 * null for this test, although timeService object and START_TIME are not null. Doesn't matter use
		 * mock() or spy() on TimeService. All the other tests work perfectly, but if to add
		 * when(object.getParameters()).thenReturn(null) for example to exceptionIfObjectGetStateReturnedNull()
		 * test, it will get NPE on timeService.getCurrentTime too.
		 */
	    reinitializeObjects();
	    when(object.getParameters()).thenReturn(null);
	    manager.doRealTimeDependOperation();
	}
	
	private void reinitializeObjects(){
		TimeService timeService = new TimeService();
		object = spy(new ShockLoadConsumer(simulation, timeService, dispatcher));
		manager = new ObjectConnectionManager(timeService, dispatcher, object);
	}
	
	@Test
	public void exceptionIfObjectGetParametersReturnedWrongParametersClass(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("returned PowerStationParameters instead ConsumerParametersStub.");
	    
	    /*
	     * The same problem with when(object.getParameters())... as in
	     * exceptionIfObjectGetParametersReturnsNull() test;
	     */
	    reinitializeObjects();
	    PowerStationParameters parameters = new PowerStationParameters(0, START_TIME, LocalTime.MIN, 1);
	    when(object.getParameters()).thenReturn(parameters);
	    manager.doRealTimeDependOperation();
	}
}
