package com.epsm.electricPowerSystemModel.model.dispatch;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class PowerSystemObjectTest{
	private PowerSystemObject object;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private Class<? extends DispatcherMessage> expectedMessageType;
	private DispatcherMessage message;
	private final LocalDateTime START_TIME = LocalDateTime.of(2000, 01, 01, 00, 00);
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void initialize(){
		timeService = mock(TimeService.class);
		dispatcher = mock(Dispatcher.class);
		expectedMessageType = DispatcherMessage.class;
		object = new Child(timeService, dispatcher, expectedMessageType, "№1");
		message = new DispatcherMessage(START_TIME);

		when(timeService.getCurrentTime()).thenReturn(START_TIME);
	}

	@Test
	public void triesConnectToDispatcher(){
		object.interactWithDisparcher();
		
		verify(dispatcher).connectToPowerObject(any());
	}
	
	@Test
	public void doesNotTryConnectToDispatcherIfConnectionEstablishedAndActive(){
		object.interactWithDisparcher();
		object.acceptMessage(message);
		addToSystemTimeValueLessThanAcceptablePauseBetweenDispatcherMessages();
		object.interactWithDisparcher();
		
		verify(dispatcher).connectToPowerObject(any());
	}
	
	private void addToSystemTimeValueLessThanAcceptablePauseBetweenDispatcherMessages(){
		when(timeService.getCurrentTime()).thenReturn(START_TIME.plusSeconds(
				(long)(GlobalConstants.ACCEPTABLE_PAUSE_BETWEEN_MESSAGES_FROM_DISPATCHER_IN_SECCONDS * 0.9)));
	}
	
	@Test
	public void TriesConnectToDispatcherAgainIfConnectionLost(){
		object.interactWithDisparcher();
		object.acceptMessage(message);
		addToSystemTimeValueMoreThanAcceptablePauseBetweenDispatcherMessages();
		object.interactWithDisparcher();
		
		verify(dispatcher, times(2)).connectToPowerObject(any());
	}
	
	private void addToSystemTimeValueMoreThanAcceptablePauseBetweenDispatcherMessages(){
		when(timeService.getCurrentTime()).thenReturn(START_TIME.plusSeconds(
				(GlobalConstants.ACCEPTABLE_PAUSE_BETWEEN_MESSAGES_FROM_DISPATCHER_IN_SECCONDS * 2)));
	}
	
	@Test
	public void sendsMessagesToDispatcherIfPauseBetweenSendingMoreThenSet(){
		object.interactWithDisparcher();
		object.acceptMessage(message);
		object.interactWithDisparcher();
		
		verify(dispatcher).acceptReport(any());
	}
	
	@Test
	public void doesNotSendsMessagesToDispatcherIfPauseBetweenSendingLessThenSet(){
		object.interactWithDisparcher();
		object.acceptMessage(message);
		object.interactWithDisparcher();
		addToSystemTimeValueLessThanPauseBetweenSending();
		object.interactWithDisparcher();
		
		verify(dispatcher).acceptReport(any());
	}
	
	private void addToSystemTimeValueLessThanPauseBetweenSending(){
		when(timeService.getCurrentTime()).thenReturn(START_TIME.plusSeconds(
				(long) (GlobalConstants.PAUSE_BETWEEN_SENDING_MESSAGES_TO_DISPATCHER_IN_SECCONDS * 0.9)));
	}
	
	@Test
	public void doNothingIfAcceptedMessageClassIsNotExpected(){
		message = new  PowerStationGenerationSchedule(1);
		object = new ChildExceptionIfInteractWithOverridenMethods(
				timeService, dispatcher, expectedMessageType, "1");
		
		object.interactWithDisparcher();
		object.acceptMessage(message);
		object.interactWithDisparcher();
		
		verify(dispatcher, never()).acceptReport(any());
	}
	
	@Test
	public void doNothingIfAcceptedMessageIsNull(){
		message = null;
		object = new ChildExceptionIfInteractWithOverridenMethods(
				timeService, dispatcher, expectedMessageType, "1");
		
		object.interactWithDisparcher();
		object.acceptMessage(message);
		object.interactWithDisparcher();
		
		verify(dispatcher, never()).acceptReport(any());
	}
	
	@Test
	public void exceptionIfOverridenGetStateReturnsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerObjectState must not be null.");
	    
	    object = new ChildReturnsNullWithGetState(timeService, dispatcher, expectedMessageType, "1");
	    
	    object.interactWithDisparcher();
		object.acceptMessage(message);
		object.interactWithDisparcher();
	}
	
	@Test
	public void exceptionInConstructorIfTimeServiseIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerSystem object constructor: timeService must not be null.");
	
	    object = new ChildReturnsNullWithGetState(null, dispatcher, expectedMessageType, "1");
	}
	
	@Test
	public void exceptionInConstructorIfDispatcherIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerSystem object constructor: dispatcher must not be null.");
	
	    object = new ChildReturnsNullWithGetState(timeService, null, expectedMessageType, "1");
	}
	
	@Test
	public void exceptionInConstructorIfExpectedMessageTypeIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerSystem object constructor: expectedMessageType must not be null.");
	
	    object = new ChildReturnsNullWithGetState(timeService, dispatcher, null, "1");
	}
	
	@Test
	public void exceptionInConstructorIfChildNameForLoggingIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerSystem object constructor: childNameForLogging must not be null.");
	
	    object = new ChildReturnsNullWithGetState(timeService, dispatcher, expectedMessageType, null);
	}
	
	@Test
	public void exceptionInConstructorIfChildNameForLoggingIsEmpty(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerSystem object constructor: childNameForLogging must not be empty.");
	
	    object = new ChildReturnsNullWithGetState(timeService, dispatcher, expectedMessageType, "   ");
	}
	
	private class Child extends PowerSystemObject{
		public Child(TimeService timeService, Dispatcher dispatcher,
				Class<? extends DispatcherMessage> expectedMessageType, String childNameForLogging) {

			super(timeService, dispatcher, expectedMessageType, childNameForLogging);
		}

		@Override
		protected void processDispatcherMessage(DispatcherMessage message) {
		}

		@Override
		protected PowerObjectState getState() {
			return new ConsumerState(1, 1, null);
		}
	}
	
	private class ChildReturnsNullWithGetState extends Child{

		public ChildReturnsNullWithGetState(TimeService timeService, Dispatcher dispatcher,
				Class<? extends DispatcherMessage> expectedMessageType, String childNameForLogging) {
			super(timeService, dispatcher, expectedMessageType, childNameForLogging);
		}
		
		@Override
		protected PowerObjectState getState() {
			return null;
		}
	}
	
	private class ChildExceptionIfInteractWithOverridenMethods extends Child{
		public ChildExceptionIfInteractWithOverridenMethods(TimeService timeService, Dispatcher dispatcher,
				Class<? extends DispatcherMessage> expectedMessageType, String childNameForLogging) {

			super(timeService, dispatcher, expectedMessageType, childNameForLogging);
		}

		@Override
		protected void processDispatcherMessage(DispatcherMessage message) {
			throw new DispatchingException("PowerObject test implementation with exceptions: "
					+ "overriden processDispatcherMessage() was called.");
		}

		@Override
		protected PowerObjectState getState() {
			throw new DispatchingException("PowerObject test implementation with exceptions: "
					+ "overriden getState() was called.");
		}
	}
}
