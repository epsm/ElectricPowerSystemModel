package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.PowerObjectStateManager;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.TimeService;

import java.util.List;

public abstract class ConsumerStateManager<T extends ConsumerParameters> extends PowerObjectStateManager<T, ConsumerState> {

	public ConsumerStateManager(TimeService timeService, Dispatcher dispatcher) {
		super(timeService, dispatcher);
	}

	@Override
	protected boolean doSend(List<ConsumerState> states) {
		return dispatcher.acceptConsumerStates(states);
	}
}
