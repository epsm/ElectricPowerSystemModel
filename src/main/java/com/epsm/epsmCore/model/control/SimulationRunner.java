package com.epsm.epsmCore.model.control;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.RealTimeOperations;

public class SimulationRunner{
	private ElectricPowerSystemSimulation simulation;
	private long realTimeStepCounter;
	private long simulationTimeStepCounter;
	private final int PAUSE_BETWEEN_CALCULATING_STEPS_IN_MS = 1;
	private final int PAUSE_BETWEEN_REAL_TIME_STEPS_IN_MS = 500;
	private Logger logger;
	
	public void runSimulation(ElectricPowerSystemSimulation simulation){
		logger = LoggerFactory.getLogger(SimulationRunner.class);
		
		if(simulation == null){
			logger.error("Attempt to run null model.");
			throw new IllegalArgumentException("SimulationRunner: simulation must not be null.");
		}
		
		this.simulation = simulation;
		
		runSimulation();
		
		logger.info("Simulation run.");
	}
	
	private void runSimulation(){
		Runnable simulationToRun = new SimulationTimeRunner();
		Runnable realTimeOperations = new RealTimeRunner();
		Thread runningSimulation = new Thread(simulationToRun);
		Thread runningRealTime = new Thread(realTimeOperations);
		
		runningSimulation.start();
		runningRealTime.start();
	}

	private class SimulationTimeRunner implements Runnable{
		
		@Override
		public void run() {
			Thread.currentThread().setName("Sim. time");
			
			while(true){
				simulation.calculateNextStep();
				
				if(simulationTimeStepCounter++ > 100){
					logger.debug("100  simulation step performed.");
					resetCounter();
				}

				pause();
			}
		}
		
		private void resetCounter(){
			simulationTimeStepCounter = 0;
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
			Thread.currentThread().setName("Real time");
			
			while(true){
				if(isModelInitialized()){
					objects = simulation.getRealTimeDependingObjects();
					
					for(RealTimeOperations operations: objects.values()){
						operations.doRealTimeDependingOperations();
					}
				}
				
				if(realTimeStepCounter++ > 10){
					logger.debug("10 real time step performed.");
					resetCounter();
				}
				
				pause();
			}
		}
		
		private boolean isModelInitialized(){
			return realTimeStepCounter > 1;
		}
		
		private void resetCounter(){
			realTimeStepCounter = 1;
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
