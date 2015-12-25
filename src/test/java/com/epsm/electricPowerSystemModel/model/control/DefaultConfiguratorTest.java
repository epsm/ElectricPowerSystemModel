package com.epsm.electricPowerSystemModel.model.control;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.control.DefaultConfigurator;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.electricPowerSystemModel.model.generalModel.SimulationException;

public class DefaultConfiguratorTest {
	private DefaultConfigurator configurator = new DefaultConfigurator();
	private ElectricPowerSystemSimulation simulation;
	private Dispatcher dispatcher;
	
	@Before
	public void initialize(){
		simulation = new ElectricPowerSystemSimulationImpl();
		dispatcher = mock(Dispatcher.class);
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionIfSimulationIsNull(){
		expectedEx.expect(SimulationException.class);
	    expectedEx.expectMessage("DefaultConfigurer: Simulation must not be null.");
		
		configurator.initialize(null, dispatcher);
	}
	
	@Test
	public void exceptionIfDispatcherIsNull(){
		expectedEx.expect(SimulationException.class);
	    expectedEx.expectMessage("DefaultConfigurer: Dispatcher must not be null.");
		
		configurator.initialize(simulation, null);
	}
}
