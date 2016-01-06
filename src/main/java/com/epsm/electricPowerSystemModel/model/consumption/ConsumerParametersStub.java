package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsumerParametersStub extends Parameters{
	
	@JsonCreator
	public ConsumerParametersStub(
			@JsonProperty("powerObjectId")long powerObjectId,
			@JsonProperty("realTimeStamp")LocalDateTime realTimeStamp, 
			@JsonProperty("simulationTimeStamp")LocalTime simulationTimeStamp){
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}

	@Override
	public String toString() {
		return String.format("ConsumerParametersStub#%d toString() stub", powerObjectId);
	}
}
