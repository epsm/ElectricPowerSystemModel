package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;

public abstract class ConsumerParameters extends Parameters{
	public ConsumerParameters(long powerObjectId, LocalDateTime realTimeStamp, 
			LocalTime simulationTimeStamp){
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
	}
}
