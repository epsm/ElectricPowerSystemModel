package com.epsm.epsmcore.model.generation;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class PowerStationGenerationSchedule {

	private final long powerStationId;
	private final List<GeneratorGenerationSchedule> generatorSchedules = new ArrayList<>();

	public PowerStationGenerationSchedule(long powerStationId) {
		this.powerStationId = powerStationId;
	}
}