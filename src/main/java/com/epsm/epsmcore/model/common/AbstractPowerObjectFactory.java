package com.epsm.epsmcore.model.common;

import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmcore.model.simulation.TimeService;

import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractPowerObjectFactory {
	protected Simulation simulation;
	protected Dispatcher dispatcher;
	protected TimeService timeService;
	private AtomicLong id = new AtomicLong();

	public AbstractPowerObjectFactory(Simulation simulation, Dispatcher dispatcher, TimeService timeService) {
		this.simulation = simulation;
		this.dispatcher = dispatcher;
		this.timeService = timeService;
	}

	protected long getId() {
		return id.incrementAndGet();
	}
}
