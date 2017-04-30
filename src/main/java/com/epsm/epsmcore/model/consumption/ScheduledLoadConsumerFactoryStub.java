package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.AbstractPowerObjectFactory;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmcore.model.simulation.TimeService;

import java.util.List;

import static java.util.Arrays.asList;

public class ScheduledLoadConsumerFactoryStub extends AbstractPowerObjectFactory {

	public final List<Float> LOAD_BY_HOURS_IN_PERCENT = asList(
			 69f,  68f,  66f,  65f,	 62f,  65f,
			 72f,  83f,  91f,  96f,  95f,  93f,
			 91f,  92f,  92f,  93f,  94f,  98f,
			100f,  98f,  95f,  93f,  88f,  75f);

	public ScheduledLoadConsumerFactoryStub(Simulation simulation, TimeService timeService, Dispatcher dispatcher) {
		super(simulation, dispatcher, timeService);
	}

	public synchronized Consumer createConsumer() {
		return new ScheduledLoadConsumer(simulation, dispatcher, createParemeters(), createStateManager());
	}

	private ScheduledLoadConsumerParameters createParemeters(){
		float degreeOnDependingOfFrequency = 2f;
		float maxLoadWithoutFluctuationsInMW = 95f;
		float randomFluctuationsInPercent = 5f;

		return new ScheduledLoadConsumerParameters(
				getId(),
				degreeOnDependingOfFrequency,
				LOAD_BY_HOURS_IN_PERCENT,
				maxLoadWithoutFluctuationsInMW,
				randomFluctuationsInPercent);
	}

	private ConsumerStateManager createStateManager() {
		return new ConsumerStateManager(timeService, dispatcher);
	}
}