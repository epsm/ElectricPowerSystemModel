package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.PowerObjectStateManager;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.TimeService;

import java.util.List;

public class ConsumerStateManager extends PowerObjectStateManager<ConsumerParameters, ConsumerState> {

	public ConsumerStateManager(TimeService timeService, Dispatcher dispatcher) {
		super(timeService, dispatcher);
	}

	@Override
	protected boolean doSend(List<ConsumerState> states) {
		return dispatcher.acceptConsumerStates(states);
	}

	@Override
	protected boolean doRegister(ConsumerParameters parameters) {
		return dispatcher.registerConsumer(parameters);
	}
}
