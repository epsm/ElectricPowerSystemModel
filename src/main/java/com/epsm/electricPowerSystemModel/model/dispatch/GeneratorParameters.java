package com.epsm.electricPowerSystemModel.model.dispatch;

public class GeneratorParameters extends GeneratorInclusion{

	public GeneratorParameters(int generatorNumber, float nominalPowerInMW, float minimalTechnologyPower) {
		super(generatorNumber);
		this.nominalPowerInMW = nominalPowerInMW;
		this.minimalTechnologyPower = minimalTechnologyPower;
	}

	private float nominalPowerInMW;
	private float minimalTechnologyPower;

	public float getNominalPowerInMW() {
		return nominalPowerInMW;
	}

	public float getMinimalTechnologyPower() {
		return minimalTechnologyPower;
	}
}
