package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.dispatch.Command;

//There may be methods allows consumer to be turned on/of or limit consumption.
public class ConsumptionPermissionStub extends Command{
	public ConsumptionPermissionStub(long powerObjectId, LocalDateTime realTimeStamp,
			LocalTime simulationTimeStamp) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}

	@Override
	public String toString() {
		return "ConsumptionPermission toString() stub";
	}
}
