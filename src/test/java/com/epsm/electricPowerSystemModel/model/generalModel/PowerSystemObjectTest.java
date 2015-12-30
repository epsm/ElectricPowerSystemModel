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

import com.epsm.electricPowerSystemModel.model.dispatch.ConsumerParametersStub;
import com.epsm.electricPowerSystemModel.model.dispatch.ConsumerState;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatcherMessage;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;
import com.epsm.electricPowerSystemModel.model.dispatch.ConsumptionPermissionStub;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerObjectParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerObjectState;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerStationGenerationSchedule;

public class PowerSystemObjectTest{
	private ElectricPowerSystemSimulation simulation;
	private PowerObject object;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private Class<PowerStationGenerationSchedule> expectedMessageType;
	private DispatcherMessage message;
	private final LocalDateTime START_TIME = LocalDateTime.of(2000, 01, 01, 00, 00);
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void initialize(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		timeService = mock(TimeService.class);
		dispatcher = mock(Dispatcher.class);
		expectedMessageType = PowerStationGenerationSchedule.class;
		object = new AbstractImpl(simulation, timeService, dispatcher, expectedMessageType);
		message = new PowerStationGenerationSchedule(0);

		when(timeService.getCurrentTime()).thenReturn(START_TIME);
	}

	@Test
	public void triesConnectToDispatcher(){
		object.doRealTimeDependOperation();
		
		verify(dispatcher).establishConnection(any(PowerObjectParameters.class));
	}
	
	@Test
	public void doesNotTryConnectToDispatcherIfConnectionEstablishedAndActive(){
		object.doRealTimeDependOperation();
		object.acceptMessage(message);
		addToSystemTimeValueLessThanAcceptablePauseBetweenDispatcherMessages();
		object.doRealTimeDependOperation();
		
		verify(dispatcher).establishConnection(any(PowerObjectParameters.class));
	}
	
	private void addToSystemTimeValueLessThanAcceptablePauseBetweenDispatcherMessages(){
		when(timeService.getCurrentTime()).thenReturn(START_TIME.plusSeconds(
				(long)(GlobalConstants.ACCEPTABLE_PAUSE_BETWEEN_MESSAGES_FROM_DISPATCHER_IN_SECCONDS * 0.9)));
	}
	
	@Test
	public void TriesConnectToDispatcherAgainIfConnectionLost(){
		object.doRealTimeDependOperation();
		object.acceptMessage(message);
		addToSystemTimeValueMoreThanAcceptablePauseBetweenDispatcherMessages();
		object.doRealTimeDependOperation();
		
		verify(dispatcher, times(2)).establishConnection(any());
	}
	
	private void addToSystemTimeValueMoreThanAcceptablePauseBetweenDispatcherMessages(){
		when(timeService.getCurrentTime()).thenReturn(START_TIME.plusSeconds(
				(GlobalConstants.ACCEPTABLE_PAUSE_BETWEEN_MESSAGES_FROM_DISPATCHER_IN_SECCONDS * 2)));
	}
	
	@Test
	public void sendsMessagesToDispatcherIfPauseBetweenSendingMoreThenSet(){
		object.doRealTimeDependOperation();
		object.acceptMessage(message);
		object.doRealTimeDependOperation();
		
		verify(dispatcher).acceptReport(any());
	}
	
	@Test
	public void doesNotSendsMessagesToDispatcherIfPauseBetweenSendingLessThenSet(){
		object.doRealTimeDependOperation();
		object.acceptMessage(message);
		object.doRealTimeDependOperation();
		addToSystemTimeValueLessThanPauseBetweenSending();
		object.doRealTimeDependOperation();
		
		verify(dispatcher).acceptReport(any());
	}
	
	private void addToSystemTimeValueLessThanPauseBetweenSending(){
		when(timeService.getCurrentTime()).thenReturn(START_TIME.plusSeconds(
				(long) (GlobalConstants.PAUSE_BETWEEN_SENDING_MESSAGES_TO_DISPATCHER_IN_SECCONDS * 0.9)));
	}
	
	@Test
	public void doNothingIfAcceptedMessageClassIsNotExpected(){
		message = new  ConsumptionPermissionStub(1);
		object = new AbstractImpl(simulation, timeService, dispatcher, expectedMessageType);
		
		object.doRealTimeDependOperation();
		object.acceptMessage(message);
		object.doRealTimeDependOperation();
		
		verify(dispatcher, never()).acceptReport(any());
	}
	
	@Test
	public void doNothingIfAcceptedMessageIsNull(){
		message = null;
		object = new ImplExceptionIfInteractWithOverridenMethods(
				simulation, timeService, dispatcher, expectedMessageType);
		
		object.doRealTimeDependOperation();
		object.acceptMessage(message);
		object.doRealTimeDependOperation();
		
		verify(dispatcher, never()).acceptReport(any());
	}
	
	@Test
	public void exceptionIfOverridenGetStateReturnsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerObjectState can't be null.");
	    
	    object = new ImplReturnsNullWithGetState(simulation, timeService, dispatcher, expectedMessageType);
	    
	    object.doRealTimeDependOperation();
		object.acceptMessage(message);
		object.doRealTimeDependOperation();
	}
	
	@Test
	public void exceptionIfOverridenGetParametersReturnsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("PowerObjectParameters can't be null.");
	    
	    object = new ImplReturnsNullWithGetParameters(
	    		simulation, timeService, dispatcher, expectedMessageType);
	    
	    object.doRealTimeDependOperation();
		object.acceptMessage(message);
		object.doRealTimeDependOperation();
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
	
	private class AbstractImpl extends PowerObject{
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

		@Override
		public PowerObjectParameters getParameters() {
			return new ConsumerParametersStub(1);
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
	
	private class ImplReturnsNullWithGetParameters extends AbstractImpl{

		public ImplReturnsNullWithGetParameters(ElectricPowerSystemSimulation simulation,
				TimeService timeService, Dispatcher dispatcher,
				Class<? extends DispatcherMessage> expectedMessageType) {
			
			super(simulation, timeService, dispatcher, expectedMessageType);
		}
		
		@Override
		public PowerObjectParameters getParameters(){
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
