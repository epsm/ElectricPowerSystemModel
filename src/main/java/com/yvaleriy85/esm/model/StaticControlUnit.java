package main.java.com.yvaleriy85.esm.model;

public class StaticControlUnit {
	private float coefficientOfStatism;
	private float requiredFrequency;
	private float powerAtRequiredFrequency;
	private Generator generator;
	
	public float getGeneratorPower(){
		float powerAccordingToStaticCharacteristic = countGeneratorPowerWithStaticCharacteristic();
		
		if(isPowerMoreThanGeneratorNominal(powerAccordingToStaticCharacteristic)){
			return generator.getNominalPowerInMW();
		}
		
		if(isPowerLessThanGeneratorMinimalTechnology(powerAccordingToStaticCharacteristic)){
			return generator.getMinimalTechnologyPower();
		}
			
		return powerAccordingToStaticCharacteristic;
	}
	
	private float countGeneratorPowerWithStaticCharacteristic(){
		float previousPowerSysytemFrequency = EnergySystem.getFrequencyInPowerSystem();

		return powerAtRequiredFrequency + (requiredFrequency - 
				previousPowerSysytemFrequency) / coefficientOfStatism;
	}
	
	private boolean isPowerMoreThanGeneratorNominal(float countetPower){
		return countetPower > generator.getNominalPowerInMW();
	}
	
	private boolean isPowerLessThanGeneratorMinimalTechnology(float countetPower){
		return countetPower < generator.getMinimalTechnologyPower();
	}

	public void setGenerator(Generator generator) {
		this.generator = generator;
	}

	public float getCoefficientOfStatism() {
		return coefficientOfStatism;
	}

	public void setCoefficientOfStatism(float coefficientOfStatism) {
		this.coefficientOfStatism = coefficientOfStatism;
	}

	public float getRequiredFrequency() {
		return requiredFrequency;
	}

	public void setRequiredFrequency(float requiredFrequency) {
		this.requiredFrequency = requiredFrequency;
	}

	public float getPowerAtRequiredFrequency() {
		return powerAtRequiredFrequency;
	}

	public void setPowerAtRequiredFrequency(float powerAtRequiredFrequency) {
		this.powerAtRequiredFrequency = powerAtRequiredFrequency;
	}
}
