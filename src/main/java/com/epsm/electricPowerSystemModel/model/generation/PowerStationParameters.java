package com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.MessageInclusionsContainer;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;

public class PowerStationParameters extends Parameters{

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
