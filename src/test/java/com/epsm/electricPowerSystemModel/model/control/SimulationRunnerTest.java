package com.epsm.electricPowerSystemModel.model.control;

import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.control.SimulationRunner;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.electricPowerSystemModel.model.generalModel.SimulationException;

public class SimulationRunnerTest{
	private ElectricPowerSystemSimulation simulation = new ElectricPowerSystemSimulationImpl();
	private SimulationRunner runner = new SimulationRunner(); 
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void isTimeInSimulationRuns() throws InterruptedException{
		LocalTime startTime = null;
		LocalTime currentTimeInSimulation = null;
		
		startTime = simulation.getTime();
		runner.runSimulation(simulation);
		doPause();
		currentTimeInSimulation = simulation.getTime();
		
		Assert.assertNotEquals(startTime, currentTimeInSimulation);
	}
	
	private void doPause() throws InterruptedException{
		Thread.sleep(2000);//too many because test will be failed under maven test otherwise.
	}
	
	@Test
	public void exceptionIfSimulationIsNull(){
		expectedEx.expect(SimulationException.class);
	    expectedEx.expectMessage("SimulationRunner: simulation must not be null.");
		
		runner.runSimulation(null);
	}
	
}
