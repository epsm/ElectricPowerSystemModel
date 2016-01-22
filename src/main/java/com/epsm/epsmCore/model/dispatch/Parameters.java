package com.epsm.epsmCore.model.dispatch;

import java.time.LocalDateTime;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.Message;

public abstract class Parameters extends Message{
	public Parameters(long powerObjectId, LocalDateTime realTimeStamp, LocalDateTime simulationTimeStamp) {
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}
}