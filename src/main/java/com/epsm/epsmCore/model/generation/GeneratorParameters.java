package com.epsm.epsmCore.model.generation;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.MessageInclusion;

public class GeneratorParameters extends MessageInclusion{
	private final float nominalPowerInMW;
	private final float minimalTechnologyPower;
	private final float reugulationSpeedInMWPerMinute;

	public GeneratorParameters(int generatorNumber, float nominalPowerInMW,
			float minimalTechnologyPower, float reugulationSpeedInMWPerMinute) {
		
		super(generatorNumber);
		this.nominalPowerInMW = nominalPowerInMW;
		this.minimalTechnologyPower = minimalTechnologyPower;
		this.reugulationSpeedInMWPerMinute = reugulationSpeedInMWPerMinute;
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
	
	public float getReugulationSpeedInMWPerMinute(){
		return reugulationSpeedInMWPerMinute;
	}

	@Override
	public String toString() {
		return "GeneratorParameters toString() stub";
	}
}
