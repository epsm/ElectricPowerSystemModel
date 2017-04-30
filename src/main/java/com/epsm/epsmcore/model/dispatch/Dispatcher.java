package com.epsm.epsmcore.model.dispatch;

import com.epsm.epsmcore.model.consumption.ConsumerState;
import com.epsm.epsmcore.model.consumption.RandomLoadConsumerParameters;
import com.epsm.epsmcore.model.consumption.ScheduledLoadConsumerParameters;
import com.epsm.epsmcore.model.generation.PowerStationParameters;
import com.epsm.epsmcore.model.generation.PowerStationState;

import java.util.List;

public interface Dispatcher {
	boolean registerRandomLoadConsumer(RandomLoadConsumerParameters parameters);
	boolean registerScheduledLoadConsumer(ScheduledLoadConsumerParameters parameters);
	boolean registerPowerStation(PowerStationParameters parameters);
	boolean acceptConsumerStates(List<ConsumerState> state);
	boolean acceptPowerStationStates(List<PowerStationState> state);
}
