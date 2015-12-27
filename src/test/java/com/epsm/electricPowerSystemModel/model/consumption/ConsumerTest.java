package com.epsm.electricPowerSystemModel.model.consumption;

import static org.mockito.Mockito.mock;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatcherMessage;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class ConsumerTest {
	private ElectricPowerSystemSimulation simulation = null;
	private Dispatcher dispatcher = mock(Dispatcher.class);
	private TimeService timeService  = mock(TimeService.class);
	private Class<? extends DispatcherMessage> expectedMessageType = DispatcherMessage.class;
	private int consumerNumber = 1;
	private String childNameForLogging = "1";
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionIfSimulationInConstructorIsNull(){
		expectedEx.expect(ConsumptionException.class);
	    expectedEx.expectMessage("Consumer: simulation must not be null.");
		
	    new ShockLoadConsumer(timeService, dispatcher, expectedMessageType,
				childNameForLogging, consumerNumber, simulation);
	}
}
