package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class DispatcherMessage extends Message{
	public DispatcherMessage(long powerObjectId, LocalDateTime realTimeStamp, LocalTime simulationTimeStamp) {
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}
}
