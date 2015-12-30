package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class PowerObjectState extends Message{
	public PowerObjectState(long powerObjectId, LocalDateTime realTimeStamp, LocalTime simulationTimeStamp) {
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}
}
