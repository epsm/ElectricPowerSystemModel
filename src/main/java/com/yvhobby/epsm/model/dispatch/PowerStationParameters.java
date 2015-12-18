package main.java.com.yvhobby.epsm.model.dispatch;

import java.util.Collections;
import java.util.Set;

public class PowerStationParameters{
	private int powerStationId;
	private Set<GeneratorParameters> generatorParameters;
	
	public PowerStationParameters(int powerStationId, Set<GeneratorParameters> generatorsParameters) {
		this.powerStationId = powerStationId;
		this.generatorParameters = Collections.unmodifiableSet(generatorsParameters);
	}

	public Set<GeneratorParameters> getGeneratorsParameters() {
		return generatorParameters;
	}

	public int getPowerStationId() {
		return powerStationId;
	}
}
