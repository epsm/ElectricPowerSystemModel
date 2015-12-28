package com.epsm.electricPowerSystemModel.model.dispatch;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PowerStationParameters extends PowerObjectParameters{
	private Map<Integer, GeneratorParameters> generatorParameters;
	
	@JsonCreator
	public PowerStationParameters(
			@JsonProperty("powerStationNumber") long powerStationId,
			@JsonProperty("generatorsParameters") Map<Integer, GeneratorParameters> generatorsParameters) {
		
		super(powerStationId);
		this.generatorParameters = Collections.unmodifiableMap(generatorsParameters);
	}

	public GeneratorParameters getGeneratorParameters(int generatorNumber){
		return generatorParameters.get(generatorNumber);
	}
	
	//Temporary solution to make JSON serialization work, must not be used anywhere
	public Map<Integer, GeneratorParameters> getGeneratorsParameters(){
		return generatorParameters;
	}
	
	public int getQuantityOfGenerators(){
		return generatorParameters.size();
	}
	
	public Collection<Integer> getGeneratorsNumbers(){
		return generatorParameters.keySet();
	}
}
