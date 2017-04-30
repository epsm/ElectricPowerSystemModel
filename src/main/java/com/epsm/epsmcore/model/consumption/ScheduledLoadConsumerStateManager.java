package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.TimeService;

public class ScheduledLoadConsumerStateManager extends ConsumerStateManager<ScheduledLoadConsumerParameters> {

	public ScheduledLoadConsumerStateManager(TimeService timeService, Dispatcher dispatcher) {
		super(timeService, dispatcher);
	}

	@Override
	protected boolean doRegister(ScheduledLoadConsumerParameters parameters) {
		return dispatcher.registerScheduledLoadConsumer(parameters);
	}
}
