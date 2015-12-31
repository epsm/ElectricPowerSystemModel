package com.epsm.electricPowerSystemModel.model.dispatch;

import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumer;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.PowerObject;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;

public class MessageFilterTest {
	private ElectricPowerSystemSimulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private PowerStation station;
	private PowerObject object;
	private MessageFilter filter;
	private Message message;
	
	@Before
	public void init(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		timeService = mock(TimeService.class);
		dispatcher = mock(Dispatcher.class);
		station = mock(PowerStation.class);
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionInConstructorIfObjectClassIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("MessageFilter constructor: objectClass can't be null.");
	    
	    filter = new MessageFilter(null);
	}
	
	@Test
	public void exceptionInVerifyCommandMessageMethodIfMessageIsNull(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("MessageFilter isCommandMessageValid(Message message) method:"
	    		+ " message can't be null.");
	    
	    createMainControlPanelAndFilterForIt();
		createPowerStationGenerationSchedule();
		filter.isCommandMessageValid(null);
	}
	
	private void createMainControlPanelAndFilterForIt(){
		object = new MainControlPanel(simulation, timeService, dispatcher, station);
		filter = new MessageFilter(object.getClass());
	}
	
	private void createPowerStationGenerationSchedule(){
		message = new PowerStationGenerationSchedule(0, LocalDateTime.MIN, LocalTime.MIN, 1);
	}
	
	@Test
	public void exceptionInverifyParametersMessageMethodIfMessageIsNull(){
		expectedEx.expect(DispatchingException.class);
		expectedEx.expectMessage("MessageFilter isParametersMessageValid(Message message) method:"
	    		+ " message can't be null.");
	    
	    createMainControlPanelAndFilterForIt();
		createPowerStationGenerationSchedule();
		filter.isParametersMessageValid(null);
	}
	
	@Test
	public void exceptionInVerifyStateMessageMethodIfMessageIsNull(){
		expectedEx.expect(DispatchingException.class);
		expectedEx.expectMessage("MessageFilter isStateMessageValid(Message message) method:"
	    		+ " message can't be null.");
	    
	    createMainControlPanelAndFilterForIt();
		createPowerStationGenerationSchedule();
		filter.isStateMessageValid(null);
	}

	@Test
	public void trueIfObjectIsPowerStationAndCommandIsPowerStationGenerationSchedule(){
		createMainControlPanelAndFilterForIt();
		createPowerStationGenerationSchedule();
		Assert.assertTrue(filter.isCommandMessageValid(message));
	}
	
	@Test
	public void trueIfObjectIsPowerStationAndStateIsPowerStationState(){
		createMainControlPanelAndFilterForIt();
		createPowerStationState();
		Assert.assertTrue(filter.isStateMessageValid(message));
	}
	
	private void createPowerStationState(){
		message = new PowerStationState(0, LocalDateTime.MIN, LocalTime.MIN, 1, 0);
	}
	
	@Test
	public void trueIfObjectIsPowerStationAndParametersIsPowerStationParameters(){
		createMainControlPanelAndFilterForIt();
		createPowerStationParameters();
		Assert.assertTrue(filter.isParametersMessageValid(message));
	}
	
	private void createPowerStationParameters(){
		message = new PowerStationParameters(0, LocalDateTime.MIN, LocalTime.MIN, 1);
	}

	@Test
	public void trueIfObjectIsInstanceOfConsumerAndCommandIsConsumptionPermission (){
		createConsumerAndFilterForIt();
		createConsumerPermisson();
		Assert.assertTrue(filter.isCommandMessageValid(message));
	}
	
	private void createConsumerAndFilterForIt(){
		object = new ShockLoadConsumer(simulation, timeService, dispatcher);
		filter = new MessageFilter(object.getClass());
	}
	
	private void createConsumerPermisson(){
		message = new ConsumptionPermissionStub(0, LocalDateTime.MIN, LocalTime.MIN);
	}
	
	@Test
	public void trueIfObjectIsInstanceOfConsumerAndStateIsConsumerState(){
		createConsumerAndFilterForIt();
		createConsumerState();
		Assert.assertTrue(filter.isStateMessageValid(message));
	}
	
	private void createConsumerState(){
		message = new ConsumerState(0, LocalDateTime.MIN, LocalTime.MIN, 0);
	}
	
	@Test
	public void trueIfObjectIsInstanceOfConsumerAndParametersIsConsumerParameters(){
		createConsumerAndFilterForIt();
		createConsumerParameters();
		Assert.assertTrue(filter.isParametersMessageValid(message));
	}
	
	private void createConsumerParameters(){
		message = new ConsumerParametersStub(0, LocalDateTime.MIN, LocalTime.MIN);
	}
	
	@Test
	public void exceptionInConstructorIfObjectClassIsNotSupported(){
		expectedEx.expect(DispatchingException.class);
		expectedEx.expectMessage("MessageFilter constructor: UnsuportedPowerObject.class is not supported.");
	    
	    filter = new MessageFilter(UnsuportedPowerObject.class);
	}
	
	private class UnsuportedPowerObject extends PowerObject{
		public UnsuportedPowerObject(ElectricPowerSystemSimulation simulation,
				TimeService timeService,Dispatcher dispatcher) {
			super(simulation, timeService, dispatcher);

		}
		
		@Override
		public void processDispatcherMessage(Message message) {
		}
		
		@Override
		public Message getState() {
			return null;
		}
		
		@Override
		public Message getParameters() {
			return null;
		}
	}
	
	@Test
	public void falseIfObjectIsPowerStationAndCommandIsConsumptionPermission (){
		createMainControlPanelAndFilterForIt();
		createConsumerPermisson();
		Assert.assertFalse(filter.isCommandMessageValid(message));
	}
	
	@Test
	public void falseIfObjectIsPowerStationAndStateIsConsumerState(){
		createMainControlPanelAndFilterForIt();
		createConsumerState();
		Assert.assertFalse(filter.isStateMessageValid(message));
	}
	
	@Test
	public void falseIfObjectIsPowerStationAndParametersIsConsumerParameters(){
		createMainControlPanelAndFilterForIt();
		createConsumerParameters();
		Assert.assertFalse(filter.isParametersMessageValid(message));
	}
}
