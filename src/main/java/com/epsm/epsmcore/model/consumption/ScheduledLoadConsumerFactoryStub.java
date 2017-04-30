package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.AbstractPowerObjectFactory;
import com.epsm.epsmcore.model.common.PowerObject;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmcore.model.simulation.TimeService;

public class ScheduledLoadConsumerFactoryStub extends AbstractPowerObjectFactory {

	public ScheduledLoadConsumerFactoryStub(Simulation simulation,
	                                        TimeService timeService, Dispatcher dispatcher) {
		
		super(simulation, timeService, dispatcher);
	}
	
	public synchronized PowerObject createConsumer(ScheduledLoadConsumerParameters parameters) {
		return new ScheduledLoadConsumer(simulation, timeService, dispatcher, parameters);
	}
}