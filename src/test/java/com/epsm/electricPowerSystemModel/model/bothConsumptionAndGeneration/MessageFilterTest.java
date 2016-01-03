package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.consumption.ConsumerParametersStub;
import com.epsm.electricPowerSystemModel.model.consumption.ConsumerState;
import com.epsm.electricPowerSystemModel.model.consumption.ConsumptionPermissionStub;
import com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumer;
import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;
import com.epsm.electricPowerSystemModel.model.dispatch.State;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationParameters;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationState;

public class MessageFilterTest {
	private ElectricPowerSystemSimulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private PowerObject object;
	private MessageFilter filter;
	private Command command;
	private State state;
	private Parameters parameters;
	
	@Before
	public void init(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		timeService = mock(TimeService.class);
		dispatcher = mock(Dispatcher.class);
		parameters = mock(Parameters.class);
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionInConstructorIfObjectClassIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("MessageFilter constructor: object class can't be null.");
	    
	    filter = new MessageFilter(null);
	}
	
	@Test
	public void exceptionInIsCommandTypeAppropriateMethodIfCommandIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("MessageFilter isCommandTypeAppropriate(...) method: command"
	    		+ " can't be null.");
	    
	    createPowerStationAndFilterForIt();
		createPowerStationGenerationSchedule();
		filter.isCommandTypeAppropriate(null);
	}
	
	private void createPowerStationAndFilterForIt(){
		object = new PowerStation(simulation, timeService, dispatcher, parameters);
		filter = new MessageFilter(object.getClass());
	}
	
	private void createPowerStationGenerationSchedule(){
		command = new PowerStationGenerationSchedule(0, LocalDateTime.MIN, LocalTime.MIN, 1);
	}
	
	@Test
	public void exceptionInIsStateTypeAppropriateMethodIfStateIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("MessageFilter isStateTypeAppropriate(...) method: state"
				+ " can't be null.");
	    
	    createPowerStationAndFilterForIt();
		createPowerStationGenerationSchedule();
		filter.isStateTypeAppropriate(null);
	}
	
	@Test
	public void exceptionInIsParametersTypeAppropriateMethodIfMessageIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("MessageFilter isParametersTypeAppropriate(...) method:"
	    		+ " parameters can't be null.");
	    
	    createPowerStationAndFilterForIt();
		createPowerStationGenerationSchedule();
		filter.isParametersTypeAppropriate(null);
	}

	@Test
	public void trueIfObjectIsPowerStationAndCommandIsPowerStationGenerationSchedule(){
		createPowerStationAndFilterForIt();
		createPowerStationGenerationSchedule();
		
		Assert.assertTrue(filter.isCommandTypeAppropriate(command));
	}
	
	@Test
	public void trueIfObjectIsPowerStationAndStateIsPowerStationState(){
		createPowerStationAndFilterForIt();
		createPowerStationState();
		
		Assert.assertTrue(filter.isStateTypeAppropriate(state));
	}
	
	private void createPowerStationState(){
		state = new PowerStationState(0, LocalDateTime.MIN, LocalTime.MIN, 1, 0);
	}
	
	@Test
	public void trueIfObjectIsPowerStationAndParametersIsPowerStationParameters(){
		createPowerStationAndFilterForIt();
		createPowerStationParameters();
		Assert.assertTrue(filter.isParametersTypeAppropriate(parameters));
	}
	
	private void createPowerStationParameters(){
		parameters = new PowerStationParameters(0, LocalDateTime.MIN, LocalTime.MIN, 1);
	}

	@Test
	public void trueIfObjectIsInstanceOfConsumerAndCommandIsConsumptionPermission (){
		createConsumerAndFilterForIt();
		createConsumerPermisson();
		Assert.assertTrue(filter.isCommandTypeAppropriate(command));
	}
	
	private void createConsumerAndFilterForIt(){
		object = new ShockLoadConsumer(simulation, timeService, dispatcher, parameters);
		filter = new MessageFilter(object.getClass());
	}
	
	private void createConsumerPermisson(){
		command = new ConsumptionPermissionStub(0, LocalDateTime.MIN, LocalTime.MIN);
	}
	
	@Test
	public void trueIfObjectIsInstanceOfConsumerAndStateIsConsumerState(){
		createConsumerAndFilterForIt();
		createConsumerState();
		Assert.assertTrue(filter.isStateTypeAppropriate(state));
	}
	
	private void createConsumerState(){
		state = new ConsumerState(0, LocalDateTime.MIN, LocalTime.MIN, 0);
	}
	
	@Test
	public void trueIfObjectIsInstanceOfConsumerAndParametersIsConsumerParameters(){
		createConsumerAndFilterForIt();
		createConsumerParameters();
		Assert.assertTrue(filter.isParametersTypeAppropriate(parameters));
	}
	
	private void createConsumerParameters(){
		parameters = new ConsumerParametersStub(0, LocalDateTime.MIN, LocalTime.MIN);
	}
	
	@Test
	public void exceptionInConstructorIfObjectClassIsNotSupported(){
		expectedEx.expect(IllegalArgumentException.class);
		expectedEx.expectMessage("MessageFilter constructor: UnsuportedPowerObject.class is"
				+ " not supported.");
	    
	    filter = new MessageFilter(UnsuportedPowerObject.class);
	}
	
	private class UnsuportedPowerObject extends PowerObject{
		public UnsuportedPowerObject(ElectricPowerSystemSimulation simulation, TimeService timeService,
				Dispatcher dispatcher, Parameters parameters) {
			
			super(simulation, timeService, dispatcher, parameters);
		}

		@Override
		public float calculatePowerBalance() {
			return 0;
		}

		@Override
		public void executeCommand(Command command) {
		}

		@Override
		protected State getState() {
			return null;
		}
	}
	
	@Test
	public void falseIfObjectIsPowerStationAndCommandIsConsumptionPermission (){
		createPowerStationAndFilterForIt();
		createConsumerPermisson();
		Assert.assertFalse(filter.isCommandTypeAppropriate(command));
	}
	
	@Test
	public void falseIfObjectIsPowerStationAndStateIsConsumerState(){
		createPowerStationAndFilterForIt();
		createConsumerState();
		Assert.assertFalse(filter.isStateTypeAppropriate(state));
	}
	
	@Test
	public void falseIfObjectIsPowerStationAndParametersIsConsumerParameters(){
		createPowerStationAndFilterForIt();
		createConsumerParameters();
		Assert.assertFalse(filter.isParametersTypeAppropriate(parameters));
	}
}
