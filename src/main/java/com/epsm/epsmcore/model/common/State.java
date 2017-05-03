package com.epsm.epsmcore.model.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public abstract class State {

	private long powerObjectId;
	private LocalDateTime simulationTimeStamp;

	public long getPowerObjectId() {
		return powerObjectId;
	}

	public void setPowerObjectId(long powerObjectId) {
		this.powerObjectId = powerObjectId;
	}

	public LocalDateTime getSimulationTimeStamp() {
		return simulationTimeStamp;
	}

	public void setSimulationTimeStamp(LocalDateTime simulationTimeStamp) {
		this.simulationTimeStamp = simulationTimeStamp;
	}
}
