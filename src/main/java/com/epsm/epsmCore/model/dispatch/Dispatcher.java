package com.epsm.epsmCore.model.dispatch;

import com.epsm.epsmCore.model.consumption.ConsumerParameters;
import com.epsm.epsmCore.model.consumption.ConsumerState;
import com.epsm.epsmCore.model.generation.PowerStationParameters;
import com.epsm.epsmCore.model.generation.PowerStationState;

public interface Dispatcher {
	void registerConsumer(ConsumerParameters parameters);
	void registerPowerStation(PowerStationParameters parameters);
	void acceptConsumerState(ConsumerState state);
	void acceptPowerStationState(PowerStationState state);
}
