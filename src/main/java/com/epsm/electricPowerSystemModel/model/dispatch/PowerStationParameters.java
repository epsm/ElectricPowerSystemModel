package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class PowerStationParameters extends MessageInclusionsContainer{

	public PowerStationParameters(long powerObjectId, LocalDateTime realTimeStamp,
			LocalTime simulationTimeStamp,	int quantityOfInclusions) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp, quantityOfInclusions);
	}

	public void addGeneratorParameters(GeneratorParameters parameters){
		addInclusion(parameters);
	}
	
	public GeneratorParameters getGeneratorParameters(int number){
		return (GeneratorParameters) getInclusion(number);
	}
	
	@Override
	public String toString() {
		return "PowerStationParameters toString() stub";
	}
}
