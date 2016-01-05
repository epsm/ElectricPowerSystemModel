package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;

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
