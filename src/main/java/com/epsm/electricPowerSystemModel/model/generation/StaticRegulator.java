package com.epsm.electricPowerSystemModel.model.generation;

import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.Constants;

public class StaticRegulator {
	private ElectricPowerSystemSimulation simulation;
	private Generator generator;
	private float coefficientOfStatism;
	private float requiredFrequency;
	private float powerAtRequiredFrequency;
	private float frequencyInPowerSystem;

	public StaticRegulator(ElectricPowerSystemSimulation simulation, Generator generator) {
		this.simulation = simulation;
		this.generator = generator;
		requiredFrequency = Constants.STANDART_FREQUENCY;
		coefficientOfStatism = 0.01f;
		
		generator.setStaticRegulator(this);
	}

	public float getGeneratorPowerInMW(){
		frequencyInPowerSystem = simulation.getFrequencyInPowerSystem();
		return calculateGeneratorPowerInMW();
	}
		
	private float calculateGeneratorPowerInMW(){	
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
		return powerAtRequiredFrequency + (requiredFrequency - 
				frequencyInPowerSystem) / coefficientOfStatism;
	}
	
	private boolean isPowerMoreThanGeneratorNominal(float power){
		return power > generator.getNominalPowerInMW();
	}
	
	private boolean isPowerLessThanGeneratorMinimalTechnology(float power){
		return power < generator.getMinimalPowerInMW();
	}

	public float getPowerAtRequiredFrequency() {
		return powerAtRequiredFrequency;
	}
	
	public void setPowerAtRequiredFrequency(float powerAtRequiredFrequency) {
		this.powerAtRequiredFrequency = powerAtRequiredFrequency;
	}
}
