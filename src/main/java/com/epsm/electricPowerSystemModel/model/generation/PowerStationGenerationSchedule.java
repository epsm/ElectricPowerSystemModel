package com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.MessageInclusionsContainer;
import com.epsm.electricPowerSystemModel.model.dispatch.Command;

public class PowerStationGenerationSchedule extends Command{
	public PowerStationGenerationSchedule(long powerObjectId, LocalDateTime realTimeStamp,
			LocalTime simulationTimeStamp, int quantityOfGeneratorSchedules) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp, quantityOfGeneratorSchedules);
	}

	@Override
	public String toString() {
		return "PowerStationGenerationSchedule toString() stub.";
	}
}