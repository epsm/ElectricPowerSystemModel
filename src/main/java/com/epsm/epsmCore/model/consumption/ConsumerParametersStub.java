package com.epsm.epsmCore.model.consumption;

import java.time.LocalDateTime;

import com.epsm.epsmCore.model.dispatch.Parameters;
import com.epsm.epsmCore.model.utils.json.ConsumerParametersStubJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ConsumerParametersStubJsonDeserializer.class)
public class ConsumerParametersStub extends Parameters{
	
	public ConsumerParametersStub(long powerObjectId, LocalDateTime realTimeStamp, 
			LocalDateTime simulationTimeStamp){
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}

	@Override
	public String toString() {
		return String.format("ConsumerParametersStub#%d toString() stub", powerObjectId);
	}
}
