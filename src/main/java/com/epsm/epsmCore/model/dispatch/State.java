package com.epsm.epsmCore.model.dispatch;

import java.time.LocalDateTime;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.Message;

public abstract class State extends Message{
	public State(long powerObjectId, LocalDateTime realTimeStamp, LocalDateTime simulationTimeStamp) {
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}
}
