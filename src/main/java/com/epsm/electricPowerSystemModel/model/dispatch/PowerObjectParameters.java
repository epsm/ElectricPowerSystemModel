package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class PowerObjectParameters extends Message{
	public PowerObjectParameters(long powerObjectId, LocalDateTime realTimeStamp, LocalTime simulationTimeStamp) {
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}
}
