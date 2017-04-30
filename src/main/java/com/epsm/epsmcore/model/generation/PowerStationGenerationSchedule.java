package com.epsm.epsmcore.model.generation;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class PowerStationGenerationSchedule {

	private final long powerStationId;
	private final Map<Integer, GeneratorGenerationSchedule> generatorSchedules = new HashMap<>();

	public PowerStationGenerationSchedule(long powerStationId) {
		this.powerStationId = powerStationId;
	}

	public void getGeneratorSchedule(int generatorNumber) {
		generatorSchedules.get(generatorNumber);
	}
}