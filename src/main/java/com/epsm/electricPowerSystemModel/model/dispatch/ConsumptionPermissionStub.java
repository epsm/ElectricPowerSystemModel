package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.LocalTime;

//There may be methods allows consumer to be turned on/of or limit consumption.
public class ConsumptionPermissionStub extends DispatcherMessage{
	public ConsumptionPermissionStub(long powerObjectId, LocalDateTime realTimeStamp,
			LocalTime simulationTimeStamp) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}

	@Override
	public String toString() {
		return "ConsumptionPermission toString() stub";
	}
}
