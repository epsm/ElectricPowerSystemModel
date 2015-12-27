package com.epsm.electricPowerSystemModel.model.generalModel;

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

import com.epsm.electricPowerSystemModel.model.dispatch.ConsumerState;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatcherMessage;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerObjectState;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerStationGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;
import com.epsm.electricPowerSystemModel.model.generalModel.PowerSystemObject;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class PowerSystemObjectTest{
	private ElectricPowerSystemSimulation simulation;
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
		simulation = mock(ElectricPowerSystemSimulation.class);
		timeService = mock(TimeService.class);
		dispatcher = mock(Dispatcher.class);
		expectedMessageType = DispatcherMessage.class;
		object = new AbstractImpl(simulation, timeService, dispatcher, expectedMessageType);
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
		object = new ImplExceptionIfInteractWithOverridenMethods(
				simulation, timeService, dispatcher, expectedMessageType);
		
		object.interactWithDisparcher();
		object.acceptMessage(message);
		object.interactWithDisparcher();
		
		verify(dispatcher, never()).acceptReport(any());
	}
	
	@Test
	public void doNothingIfAcceptedMessageIsNull(){
		message = null;
		object = new ImplExceptionIfInteractWithOverridenMethods(
				simulation, timeService, dispatcher, expectedMessageType);
		
		object.interactWithDisparcher();
		object.acceptMessage(message);
		object.interactWithDisparcher();
		
		verify(dispatcher, never()).acceptReport(any());
	}
	
	@Test
	public void exceptionIfOverridenGetStateReturnsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerObjectState must not be null.");
	    
	    object = new ImplReturnsNullWithGetState(simulation, timeService, dispatcher, expectedMessageType);
	    
	    object.interactWithDisparcher();
		object.acceptMessage(message);
		object.interactWithDisparcher();
	}
	
	@Test
	public void exceptionInConstructorIfSimulationIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerSystem object constructor: simulation must not be null.");
	
	    object = new AbstractImpl(null, timeService, dispatcher, expectedMessageType);
	}
	
	@Test
	public void exceptionInConstructorIfTimeServiseIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerSystem object constructor: timeService must not be null.");
	
	    object = new AbstractImpl(simulation, null, dispatcher, expectedMessageType);
	}
	
	@Test
	public void exceptionInConstructorIfDispatcherIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerSystem object constructor: dispatcher must not be null.");
	
	    object = new AbstractImpl(simulation, timeService, null, expectedMessageType);
	}
	
	@Test
	public void exceptionInConstructorIfExpectedMessageTypeIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerSystem object constructor: expectedMessageType must not be null.");
	
	    object = new AbstractImpl(simulation, timeService, dispatcher, null);
	}
	
	private class AbstractImpl extends PowerSystemObject{
		public AbstractImpl(ElectricPowerSystemSimulation simulation, TimeService timeService,
				Dispatcher dispatcher, 	Class<? extends DispatcherMessage> expectedMessageType) {

			super(simulation, timeService, dispatcher, expectedMessageType);
		}

		@Override
		protected void processDispatcherMessage(DispatcherMessage message) {
		}

		@Override
		protected PowerObjectState getState() {
			return new ConsumerState(1, 1, null);
		}
	}
	
	private class ImplReturnsNullWithGetState extends AbstractImpl{

		public ImplReturnsNullWithGetState(ElectricPowerSystemSimulation simulation,
				TimeService timeService, Dispatcher dispatcher,
				Class<? extends DispatcherMessage> expectedMessageType) {
			
			super(simulation, timeService, dispatcher, expectedMessageType);
		}
		
		@Override
		protected PowerObjectState getState() {
			return null;
		}
	}
	
	private class ImplExceptionIfInteractWithOverridenMethods extends AbstractImpl{
		public ImplExceptionIfInteractWithOverridenMethods(ElectricPowerSystemSimulation simulation,
				TimeService timeService, Dispatcher dispatcher,
				Class<? extends DispatcherMessage> expectedMessageType) {

			super(simulation, timeService, dispatcher, expectedMessageType);
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
