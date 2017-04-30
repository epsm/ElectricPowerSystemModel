package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.AbstractPowerObjectFactory;
import com.epsm.epsmcore.model.common.PowerObjectStateManager;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmcore.model.simulation.TimeService;

public class RandomLoadConsumerFactory extends AbstractPowerObjectFactory {

	public RandomLoadConsumerFactory(Simulation simulation, TimeService timeService, Dispatcher dispatcher) {
		super(simulation, dispatcher, timeService);
	}

	public synchronized Consumer createConsumer() {
		return new RandomLoadConsumer(simulation, dispatcher, createParameters(), createStateManager());
	}

	private RandomLoadConsumerParameters createParameters() {
		int maxWorkDurationInSeconds = 60 * 60 * 2;
		int maxPauseBetweenWorkInSeconds = 60 * 60 * 2;
		int maxLoadInMW = 5;
		int degreeOnDependingOfFrequency = 2;

		return new RandomLoadConsumerParameters(
				getId(),
				maxWorkDurationInSeconds,
				maxPauseBetweenWorkInSeconds,
				maxLoadInMW,
				degreeOnDependingOfFrequency);
	}

	private ConsumerStateManager createStateManager() {
		return new ConsumerStateManager(timeService, dispatcher);
	}
}