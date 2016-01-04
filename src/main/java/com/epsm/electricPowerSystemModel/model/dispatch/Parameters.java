package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.Message;

public abstract class Parameters extends Message{
	public Parameters(long powerObjectId, LocalDateTime realTimeStamp, LocalTime simulationTimeStamp) {
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}
}