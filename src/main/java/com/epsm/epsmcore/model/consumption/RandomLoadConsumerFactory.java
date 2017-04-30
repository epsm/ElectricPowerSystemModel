package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.AbstractPowerObjectFactory;
import com.epsm.epsmcore.model.common.PowerObjectStateManager;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmcore.model.simulation.TimeService;

public class RandomLoadConsumerFactory extends AbstractPowerObjectFactory {

	public RandomLoadConsumerFactory(Simulation simulation, TimeService timeService, Dispatcher dispatcher) {
		super(simulation, dispatcher);
	}

	public synchronized Consumer createConsumer(RandomLoadConsumerParameters parameters, PowerObjectStateManager stateManager) {
		return new RandomLoadConsumer(simulation, dispatcher, parameters, stateManager);
	}
}