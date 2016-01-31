package com.epsm.epsmCore.model.generation;

import com.epsm.epsmCore.model.generalModel.Constants;

public class SpeedController {
	private Generator generator;
	private float coefficientOfStatism;
	private final float givenFrequency;
	private float generationAtGivenFrequency;
	private float currentFrequency;

	public SpeedController(Generator generator) {
		if(generator == null){
			String message = "SpeedController: generator must not be null.";
			throw new IllegalArgumentException(message);
		}
		
		this.generator = generator;
		givenFrequency = Constants.STANDART_FREQUENCY;
		coefficientOfStatism = 0.01f;
	}

	public float getGenerationInMW(float frequency){
		saveFrequency(frequency);
		return calculateGeneratorPowerInMW();
	}
	
	private void saveFrequency(float frequency){
		currentFrequency = frequency;
	}
		
	private float calculateGeneratorPowerInMW(){
		if(generationAtGivenFrequency == 0){
			return 0;
		}
		
		float powerAccordingToStaticCharacteristic = countGeneratorPowerWithStaticCharacteristic();
		
		if(isPowerMoreThanGeneratorNominal(powerAccordingToStaticCharacteristic)){
			return generator.getNominalPowerInMW();
		}
		
		if(isPowerLessThanGeneratorMinimalTechnology(powerAccordingToStaticCharacteristic)){
			return generator.getMinimalPowerInMW();
		}
		
		return powerAccordingToStaticCharacteristic;
	}
	
	private float countGeneratorPowerWithStaticCharacteristic(){		
		return generationAtGivenFrequency + (givenFrequency - currentFrequency) / coefficientOfStatism;
	}
	
	private boolean isPowerMoreThanGeneratorNominal(float power){
		return power > generator.getNominalPowerInMW();
	}
	
	private boolean isPowerLessThanGeneratorMinimalTechnology(float power){
		return power < generator.getMinimalPowerInMW();
	}

	public float getGenerationAtGivenFrequency() {
		return generationAtGivenFrequency;
	}
	
	public void setGenerationAtGivenFrequency(float generationAtGivenFrequency) {
		this.generationAtGivenFrequency = generationAtGivenFrequency;
	}
}
