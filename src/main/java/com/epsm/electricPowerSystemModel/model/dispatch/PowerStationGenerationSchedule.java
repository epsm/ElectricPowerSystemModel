package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class PowerStationGenerationSchedule extends MessageInclusionsContainer{
	public PowerStationGenerationSchedule(long powerObjectId, LocalDateTime realTimeStamp,
			LocalTime simulationTimeStamp, int quantityOfGeneratorSchedules) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp, quantityOfGeneratorSchedules);
	}

	@Override
	public String toString() {
		return "PowerStationGenerationSchedule toString() stub.";
	}
}