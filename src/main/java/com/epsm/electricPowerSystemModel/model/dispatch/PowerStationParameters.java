package com.epsm.electricPowerSystemModel.model.dispatch;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.epsm.electricPowerSystemModel.model.generation.GenerationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PowerStationParameters extends PowerObjectParameters{
	private Map<Integer, GeneratorParameters> generatorParameters;
	
	@JsonCreator
	public PowerStationParameters(
			@JsonProperty("powerStationNumber") long powerStationId,
			@JsonProperty("generatorsParameters") Map<Integer, GeneratorParameters> generatorsParameters) {
		
		super(powerStationId);
		
		if(generatorsParameters == null){
			throw new GenerationException("PowerStationParameters constructor: "
					+ "generatorsParameters can't be null.");
		}
	
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

	@Override
	public String toString() {
		return "PowerStationParameters toString() stub";
	}
}
