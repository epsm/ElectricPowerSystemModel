package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.PowerObjectStateManager;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.TimeService;

import java.util.List;

public class PowerStationStateManager extends PowerObjectStateManager<PowerStationParameters, PowerStationState> {

	public PowerStationStateManager(TimeService timeService, Dispatcher dispatcher) {
		super(timeService, dispatcher);
	}

	@Override
	protected boolean doSend(List<PowerStationState> states) {
		return dispatcher.acceptPowerStationStates(states);
	}

	@Override
	protected boolean doRegister(PowerStationParameters parameters) {
		return dispatcher.registerPowerStation(parameters);
	}
}
