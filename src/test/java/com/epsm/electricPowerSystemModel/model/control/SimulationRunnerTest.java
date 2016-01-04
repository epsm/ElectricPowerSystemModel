package com.epsm.electricPowerSystemModel.model.control;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.generalModel.SimulationException;

public class SimulationRunnerTest{
	private SimulationRunner runner = new SimulationRunner(); 
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionIfSimulationIsNull(){
		expectedEx.expect(SimulationException.class);
	    expectedEx.expectMessage("SimulationRunner: simulation must not be null.");
		
		runner.runSimulation(null);
	}
	
}
