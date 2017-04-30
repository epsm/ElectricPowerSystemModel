package com.epsm.epsmcore.model.generation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
public class PowerStationGenerationSchedule {

	private long powerStationId;
	private final Map<Integer, GeneratorGenerationSchedule> generatorSchedules = new HashMap<>();

	public PowerStationGenerationSchedule(long powerStationId) {
		this.powerStationId = powerStationId;
	}

	public void getGeneratorSchedule(int generatorNumber) {
		generatorSchedules.get(generatorNumber);
	}
}