package main.java.com.epsm.electricPowerSystemModel.model.dispatch;

public class GeneratorParameters {
	private int generatorNumber;
	private float nominalPowerInMW;
	private float minimalTechnologyPower;
	
	public GeneratorParameters(int generatorNumber, float nominalPowerInMW, float minimalTechnologyPower) {
		this.generatorNumber = generatorNumber;
		this.nominalPowerInMW = nominalPowerInMW;
		this.minimalTechnologyPower = minimalTechnologyPower;
	}

	public int getGeneratorNumber() {
		return generatorNumber;
	}

	public float getNominalPowerInMW() {
		return nominalPowerInMW;
	}

	public float getMinimalTechnologyPower() {
		return minimalTechnologyPower;
	}
}
