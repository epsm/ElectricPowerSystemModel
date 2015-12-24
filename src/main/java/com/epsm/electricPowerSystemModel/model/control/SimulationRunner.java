package main.java.com.epsm.electricPowerSystemModel.model.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.SimulationException;

public class SimulationRunner{
	private ElectricPowerSystemSimulation simulation;
	private Logger logger = LoggerFactory.getLogger(SimulationRunner.class);
	private final int PAUSE_BETWEEN_CALCULATING_STEPS_IN_MS = 100;
	
	public void runSimulation(ElectricPowerSystemSimulation simulation){
		this.simulation = simulation;
		
		if(simulation == null){
			logger.error("Attempt to run null model.");
			throw new SimulationException("SimulationRunner: simulation must not be null.");
		}
		
		runSimulation();
		
		logger.info("Simulation was created and run.");
	}
	
	private void runSimulation(){
		Runnable simulationToRun = new PassingOfTime();
		Thread runningSimulation = new Thread(simulationToRun);
		
		runningSimulation.start();
	}

	private class PassingOfTime implements Runnable{
		
		@Override
		public void run() {
			Thread.currentThread().setName("Simulation");
			
			while(true){
				simulation.calculateNextStep();
				pause();
			}
		}
		
		private void pause(){
			if(PAUSE_BETWEEN_CALCULATING_STEPS_IN_MS != 0){
				try {
					Thread.sleep(PAUSE_BETWEEN_CALCULATING_STEPS_IN_MS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
