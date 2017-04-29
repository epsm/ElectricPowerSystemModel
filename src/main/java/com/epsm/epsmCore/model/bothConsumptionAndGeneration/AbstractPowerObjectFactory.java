package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.TimeService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractPowerObjectFactory {
	protected ElectricPowerSystemSimulation simulation;
	protected TimeService timeService;
	protected Dispatcher dispatcher;
}
