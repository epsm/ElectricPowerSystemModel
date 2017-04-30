package com.epsm.epsmcore.model.common;

import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmcore.model.simulation.TimeService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractPowerObjectFactory {
	protected Simulation simulation;
	protected Dispatcher dispatcher;
}
