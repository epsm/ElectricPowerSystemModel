package main.java.com.yvhobby.epsm.model.dispatch;

import java.util.Set;

public class PowerStationParameters {
	private Set<GeneratorParameters> generatorParameters;
	
	public PowerStationParameters(Set<GeneratorParameters> generatorsStates) {
		this.generatorParameters = generatorsStates;
	}

	public Set<GeneratorParameters> getGeneratorsParameters() {
		return generatorParameters;
	}
}
