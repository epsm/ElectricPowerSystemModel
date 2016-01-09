package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

//There may be methods allows consumer to be turned on/of or limit consumption.
public class ConsumptionPermissionStub extends Command{
	
	@JsonCreator
	public ConsumptionPermissionStub(
			@JsonProperty("powerObjectId")long powerObjectId,
			@JsonProperty("realTimeStamp")LocalDateTime realTimeStamp,
			@JsonProperty("simulationTimeStamp")LocalTime simulationTimeStamp) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}

	@Override
	public String toString() {
		return "ConsumptionPermission toString() stub";
	}
}
