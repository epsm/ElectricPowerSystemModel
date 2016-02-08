package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.epsmCore.model.consumption.ConsumerParametersStub;
import com.epsm.epsmCore.model.consumption.ConsumerState;
import com.epsm.epsmCore.model.consumption.ConsumptionPermissionStub;
import com.epsm.epsmCore.model.consumption.ShockLoadConsumer;
import com.epsm.epsmCore.model.dispatch.Command;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.Parameters;
import com.epsm.epsmCore.model.dispatch.State;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.TimeService;
import com.epsm.epsmCore.model.generation.PowerStation;
import com.epsm.epsmCore.model.generation.PowerStationGenerationSchedule;
import com.epsm.epsmCore.model.generation.PowerStationParameters;
import com.epsm.epsmCore.model.generation.PowerStationState;

public class MessageFilterTest {
	private ElectricPowerSystemSimulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private PowerObject object;
	private MessageFilter filter;
	private Command command;
	private State state;
	private ConsumerParametersStub consumerParameters;
	private PowerStationParameters powerStationParameters;
	private final int POWER_OBJECT_ID = 0;
	private final int QUANTITY_OF_GENERATORS = 1;
	private final float FREQUENCY = 1;
	private final float LOAD = 0;
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.MIN;
	private final LocalDateTime REAL_TIMESTAMP = LocalDateTime.MIN;
	
	@Before
	public void setUp(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		timeService = mock(TimeService.class);
		dispatcher = mock(Dispatcher.class);
		consumerParameters = mock(ConsumerParametersStub.class);
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
		powerStationParameters 
			= new PowerStationParameters(POWER_OBJECT_ID, REAL_TIMESTAMP, LocalDateTime.MIN, 
					QUANTITY_OF_GENERATORS);

		object = new PowerStation(simulation, timeService, dispatcher, powerStationParameters);
		filter = new MessageFilter(object.getClass());
	}
	
	private void createPowerStationGenerationSchedule(){
		command = new PowerStationGenerationSchedule(POWER_OBJECT_ID, REAL_TIMESTAMP, 
				LocalDateTime.MIN, QUANTITY_OF_GENERATORS);
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
		state = new PowerStationState(POWER_OBJECT_ID, REAL_TIMESTAMP, SIMULATION_TIMESTAMP,
				QUANTITY_OF_GENERATORS, FREQUENCY);
	}

	@Test
	public void trueIfObjectIsInstanceOfConsumerAndCommandIsConsumptionPermission (){
		createConsumerAndFilterForIt();
		createConsumerPermisson();
		Assert.assertTrue(filter.isCommandTypeAppropriate(command));
	}
	
	private void createConsumerAndFilterForIt(){
		consumerParameters = new ConsumerParametersStub(POWER_OBJECT_ID, REAL_TIMESTAMP, SIMULATION_TIMESTAMP);
		
		object = new ShockLoadConsumer(simulation, timeService, dispatcher, consumerParameters);
		filter = new MessageFilter(object.getClass());
	}
	
	private void createConsumerPermisson(){
		command = new ConsumptionPermissionStub(POWER_OBJECT_ID, REAL_TIMESTAMP, SIMULATION_TIMESTAMP);
	}
	
	@Test
	public void trueIfObjectIsInstanceOfConsumerAndStateIsConsumerState(){
		createConsumerAndFilterForIt();
		createConsumerState();
		Assert.assertTrue(filter.isStateTypeAppropriate(state));
	}
	
	private void createConsumerState(){
		state = new ConsumerState(POWER_OBJECT_ID, REAL_TIMESTAMP, SIMULATION_TIMESTAMP, LOAD);
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
		public float calculateCurrentPowerBalance() {
			return 0;
		}

		@Override
		public State getState() {
			return null;
		}

		@Override
		protected void performDispatcherCommand(Command command) {
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
}
