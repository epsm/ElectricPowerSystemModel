package com.epsm.electricPowerSystemModel.model.control;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.RealTimeOperations;

public class SimulationRunner{
	private ElectricPowerSystemSimulation simulation;
	private Logger logger = LoggerFactory.getLogger(SimulationRunner.class);
	private final int PAUSE_BETWEEN_CALCULATING_STEPS_IN_MS = 100;
	private final int PAUSE_BETWEEN_REAL_TIME_STEPS_IN_MS = 500;
	
	public void runSimulation(ElectricPowerSystemSimulation simulation){
		this.simulation = simulation;
		
		if(simulation == null){
			logger.error("SimulationRunner: attempt to run null model.");
			throw new IllegalArgumentException("SimulationRunner: simulation must not be null.");
		}
		
		runSimulation();
		
		logger.info("Simulation was created and run.");
	}
	
	private void runSimulation(){
		Runnable simulationToRun = new SimulationTimeRunner();
		Runnable realTimeOperaations = new RealTimeRunner();
		Thread runningSimulation = new Thread(simulationToRun);
		Thread runningRealTime = new Thread(realTimeOperaations);
		
		runningSimulation.start();
		runningRealTime.start();
	}

	private class SimulationTimeRunner implements Runnable{
		
		@Override
		public void run() {
			Thread.currentThread().setName("Simulation time");
			
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
	
	private class RealTimeRunner implements Runnable{
		private Map<Long, RealTimeOperations> objects;
		
		@Override
		public void run() {
			Thread.currentThread().setName("RealTime");
			
			while(true){
				objects = simulation.getRealTimeDependingObjects();
				
				for(RealTimeOperations operations: objects.values()){
					operations.doRealTimeDependingOperations();
				}
				
				pause();
			}
		}
		
		private void pause(){
			if(PAUSE_BETWEEN_REAL_TIME_STEPS_IN_MS != 0){
				try {
					Thread.sleep(PAUSE_BETWEEN_REAL_TIME_STEPS_IN_MS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
