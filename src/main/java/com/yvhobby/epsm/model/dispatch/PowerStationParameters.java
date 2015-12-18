package main.java.com.yvhobby.epsm.model.dispatch;

import java.util.Collections;
import java.util.Set;

public class PowerStationParameters {
	private Set<GeneratorParameters> generatorParameters;
	
	public PowerStationParameters(Set<GeneratorParameters> generatorsParameters) {
		this.generatorParameters = Collections.unmodifiableSet(generatorsParameters);
	}

	public Set<GeneratorParameters> getGeneratorsParameters() {
		return generatorParameters;
	}
}
