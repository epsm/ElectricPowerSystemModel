package main.java.com.yvhobby.epsm.model.dispatch;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class PowerStationParameters{
	private int powerStationId;
	private Map<Integer, GeneratorParameters> generatorParameters;
	
	public PowerStationParameters(int powerStationId, Map<Integer, GeneratorParameters> generatorsParameters) {
		this.powerStationId = powerStationId;
		this.generatorParameters = Collections.unmodifiableMap(generatorsParameters);
	}

	public Collection <GeneratorParameters> getGeneratorsParameters() {
		return generatorParameters.values();
	}

	public int getPowerStationId() {
		return powerStationId;
	}
	
	public int getNumbersOfGenerators(){
		return generatorParameters.size();
	}
	
	public Collection<Integer> getGeneratorsId(){
		return generatorParameters.keySet();
	}
}
