package com.epsm.epsmCore.model.consumption;

import java.time.LocalDateTime;

import com.epsm.epsmCore.model.dispatch.Parameters;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsumerParametersStub extends Parameters{
	
	@JsonCreator
	public ConsumerParametersStub(
			@JsonProperty("powerObjectId") long powerObjectId,
			@JsonProperty("realTimeStamp") LocalDateTime realTimeStamp, 
			@JsonProperty("simulationTimeStamp") LocalDateTime simulationTimeStamp){
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}

	@Override
	public String toString() {
		return String.format("ConsumerParametersStub#%d toString() stub", powerObjectId);
	}
}
