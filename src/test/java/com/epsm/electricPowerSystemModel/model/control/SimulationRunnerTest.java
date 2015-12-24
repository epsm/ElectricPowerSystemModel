package test.java.com.epsm.electricPowerSystemModel.model.control;

import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.java.com.epsm.electricPowerSystemModel.model.control.SimulationRunner;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.SimulationException;

public class SimulationRunnerTest{
	private ElectricPowerSystemSimulation simulation = new ElectricPowerSystemSimulationImpl();
	private SimulationRunner runner = new SimulationRunner(); 
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test(timeout = 500)
	public void isTimeInSimulationRuns() throws InterruptedException{
		LocalTime startTime = null;
		LocalTime currentTimeInSimulation = null;
		
		startTime = simulation.getTime();
		runner.runSimulation(simulation);
		waitQarterSecond();
		currentTimeInSimulation = simulation.getTime();
		
		Assert.assertNotEquals(startTime, currentTimeInSimulation);
	}
	
	private void waitQarterSecond() throws InterruptedException{
		Thread.sleep(250);
	}
	
	@Test
	public void exceptionIfSimulationIsNull(){
		expectedEx.expect(SimulationException.class);
	    expectedEx.expectMessage("SimulationRunner: simulation must not be null.");
		
		runner.runSimulation(null);
	}
	
}
