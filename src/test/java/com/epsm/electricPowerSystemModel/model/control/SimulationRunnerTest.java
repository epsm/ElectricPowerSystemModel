package com.epsm.electricPowerSystemModel.model.control;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SimulationRunnerTest{
	private SimulationRunner runner = new SimulationRunner(); 
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionIfSimulationIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("SimulationRunner: simulation must not be null.");
		
		runner.runSimulation(null);
	}
	
}
