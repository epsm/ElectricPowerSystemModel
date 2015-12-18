package main.java.com.yvhobby.epsm.model.dispatch;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PowerStationGenerationSchedule {
	private Map<Integer, GeneratorGenerationSchedule> generatorSchedule = 
			new HashMap<Integer, GeneratorGenerationSchedule>();

	public PowerStationGenerationSchedule(Map<Integer, GeneratorGenerationSchedule> schedule) {
		this.generatorSchedule = Collections.unmodifiableMap(schedule);
	}

	public GeneratorGenerationSchedule getGeneratorGenerationSchedule(int generatorId) {
		return generatorSchedule.get(generatorId);
	}
}