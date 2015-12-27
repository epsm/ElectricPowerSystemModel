package com.epsm.electricPowerSystemModel.model.consumption;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.consumption.Consumer;
import com.epsm.electricPowerSystemModel.model.consumption.ConsumptionException;
import com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumer;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;

public class ConsumerTest {
	private ElectricPowerSystemSimulation simulation;
	private Dispatcher dispatcher;
	private Consumer consumer;
	
	@Before
	public void initialize(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		dispatcher = mock(Dispatcher.class);
		
		consumer = new ShockLoadConsumer(1, simulation);
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void consumerRegisteresWithDispacherRightAfterSetIt(){
		//as method setDispatcher(...) defined in Consumer as final I can test only one any implementation
		consumer.registerWithDispatcher(dispatcher);
		
		verify(dispatcher).connectToPowerObject(any());
	}
	
	@Test
	public void exceptionIfSimulationInConstructorIsNull(){
		expectedEx.expect(ConsumptionException.class);
	    expectedEx.expectMessage("Consumer: simulation must not be null.");
		
		new ShockLoadConsumer(1, null);
	}
}
