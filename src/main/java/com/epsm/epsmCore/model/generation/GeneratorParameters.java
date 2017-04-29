package com.epsm.epsmCore.model.generation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GeneratorParameters extends MessageInclusion{
	
	@JsonProperty("nominalPowerInMW")
	private float nominalPowerInMW;
	
	@JsonProperty("minimalTechnologyPower")
	private float minimalTechnologyPower;
	
	@JsonCreator
	public GeneratorParameters(
			@JsonProperty("generatorNumber") int generatorNumber,
			@JsonProperty("nominalPowerInMW") float nominalPowerInMW,
			@JsonProperty("minimalTechnologyPower") float minimalTechnologyPower) {
		
		super(generatorNumber);
		this.nominalPowerInMW = nominalPowerInMW;
		this.minimalTechnologyPower = minimalTechnologyPower;
	}

	public int getGeneratorNumber(){
		return getInclusionNumber();
	}
	
	public float getNominalPowerInMW() {
		return nominalPowerInMW;
	}

	public float getMinimalTechnologyPower() {
		return minimalTechnologyPower;
	}

	@Override
	public String toString() {
		return "GeneratorParameters toString() stub";
	}
}
