package com.epsm.epsmCore.model.dispatch;

import java.time.LocalDateTime;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.Message;

public abstract class Command extends Message{
	public Command(long powerObjectId, LocalDateTime realTimeStamp, LocalDateTime simulationTimeStamp) {
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}
}
