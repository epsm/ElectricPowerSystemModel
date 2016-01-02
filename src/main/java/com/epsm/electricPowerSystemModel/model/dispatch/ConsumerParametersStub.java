package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.LocalTime;

//Later there will be consumption mode, load curve and etc... 
public class ConsumerParametersStub extends Message implements Parameters{
	public ConsumerParametersStub(long powerObjectId, LocalDateTime realTimeStamp,
			LocalTime simulationTimeStamp){
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}

	@Override
	public String toString() {
		return "ConsumerParameters toString() stub";
	}
}
