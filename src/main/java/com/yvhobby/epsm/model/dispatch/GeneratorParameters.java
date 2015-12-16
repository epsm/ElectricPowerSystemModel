package main.java.com.yvhobby.epsm.model.dispatch;

public class GeneratorParameters {
	private int generatorId;
	private float nominalPowerInMW;
	private float minimalTechnologyPower;
	
	public GeneratorParameters(int generatorId, float nominalPowerInMW, float minimalTechnologyPower) {
		this.generatorId = generatorId;
		this.nominalPowerInMW = nominalPowerInMW;
		this.minimalTechnologyPower = minimalTechnologyPower;
	}

	public int getGeneratorId() {
		return generatorId;
	}

	public float getNominalPowerInMW() {
		return nominalPowerInMW;
	}

	public float getMinimalTechnologyPower() {
		return minimalTechnologyPower;
	}
}
