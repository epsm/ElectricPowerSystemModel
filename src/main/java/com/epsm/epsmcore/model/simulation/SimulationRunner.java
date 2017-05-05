package com.epsm.epsmcore.model.simulation;

import com.epsm.epsmcore.model.common.PowerObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;

public class SimulationRunner {

	private Simulation simulation;
	private long realTimeStepCounter;
	private long simulationTimeStepCounter;
	private static final Logger logger = LoggerFactory.getLogger(SimulationRunner.class);

	public void runSimulation(Simulation simulation) {
		this.simulation = simulation;

		runSimulation();

		logger.info("Simulation run.");
	}

	private void runSimulation() {
		Runnable simulationToRun = new SimulationTimeRunner();
		Runnable realTimeOperations = new RealTimeRunner();
		Thread runningSimulation = new Thread(simulationToRun);
		Thread runningRealTime = new Thread(realTimeOperations);

		runningSimulation.start();
		runningRealTime.start();
	}

	private class SimulationTimeRunner implements Runnable {

		@Override
		public void run() {
			Thread.currentThread().setName("Sim. time");

			while (simulation.getDateTimeInSimulation().isBefore(LocalDateTime.of(2000, 01, 03, 0, 0, 0))) {
				try {
					simulation.doNextStep();
				} catch (Exception e) {
					logger.error("Error while executing simulation step", e);
				}

				if (simulationTimeStepCounter++ > Constants.LOG_EVERY_SIMULATION_STEPS_DONE) {
					logger.debug("{}  simulation step performed.", Constants.LOG_EVERY_SIMULATION_STEPS_DONE);
					resetCounter();
				}

				if(Constants.PAUSE_BETWEEN_SIMULATION_STEPS_IN_MS != 0) {
					pause(Constants.PAUSE_BETWEEN_SIMULATION_STEPS_IN_MS);
				}
			}
		}

		private void resetCounter() {
			simulationTimeStepCounter = 0;
		}
	}

	private void pause(int pauseInSeconds) {
		try {
			Thread.sleep(pauseInSeconds);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private class RealTimeRunner implements Runnable {
		private Map<Long, PowerObject> objects;

		@Override
		public void run() {
			Thread.currentThread().setName("Real time");

			while (true) {
				if (isModelInitialized()) {
					try {
						simulation.sendStatesToDispatcher();
					} catch (Exception e) {
						logger.error("Error while sending states ro dispatcher", e);
					}
				}

				if (realTimeStepCounter++ > Constants.LOG_EVERY_REALTIME_STEPS_DONE) {
					logger.debug("{} real time step performed.", Constants.LOG_EVERY_REALTIME_STEPS_DONE);
					resetCounter();
				}

				pause(Constants.PAUSE_BETWEEN_REAL_TIME_STEPS_IN_MS);
			}
		}

		private boolean isModelInitialized() {
			return realTimeStepCounter > 1;
		}

		private void resetCounter() {
			realTimeStepCounter = 1;
		}
	}
}
