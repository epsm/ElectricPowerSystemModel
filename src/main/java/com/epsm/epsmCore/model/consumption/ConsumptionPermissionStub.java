package com.epsm.epsmCore.model.consumption;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.epsmCore.model.dispatch.Command;
import com.epsm.epsmCore.model.utils.json.ConsumptionPermissionStubJsonDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

//There may be methods allows consumer to be turned on/of or limit consumption.
@JsonDeserialize(using = ConsumptionPermissionStubJsonDeserializer.class)
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
		return String.format("ConsumptionPermission#%d toString() stub", powerObjectId);
	}
}
