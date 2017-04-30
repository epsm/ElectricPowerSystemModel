package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.TimeService;

public class RandomLoadConsumerStateManager extends ConsumerStateManager<RandomLoadConsumerParameters> {

	public RandomLoadConsumerStateManager(TimeService timeService, Dispatcher dispatcher) {
		super(timeService, dispatcher);
	}

	@Override
	protected boolean doRegister(RandomLoadConsumerParameters parameters) {
		return dispatcher.registerRandomLoadConsumer(parameters);
	}
}
