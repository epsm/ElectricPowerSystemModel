package com.epsm.epsmCore.model.consumption;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.epsmCore.model.dispatch.Parameters;
import com.epsm.epsmCore.model.utils.json.ConsumerParametersStubDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ConsumerParametersStubDeserializer.class)
public class ConsumerParametersStub extends Parameters{
	
	public ConsumerParametersStub(long powerObjectId, LocalDateTime realTimeStamp, 
			LocalTime simulationTimeStamp){
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}

	@Override
	public String toString() {
		return String.format("ConsumerParametersStub#%d toString() stub", powerObjectId);
	}
}
