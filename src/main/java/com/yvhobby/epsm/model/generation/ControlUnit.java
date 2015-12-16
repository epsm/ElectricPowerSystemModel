package main.java.com.yvhobby.epsm.model.generation;

import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;

public class ControlUnit {
	private ElectricPowerSystemSimulation simulation;
	private AstaticRegulatioUnit regulationUnit;
	private float coefficientOfStatism;
	private float requiredFrequency;
	private float powerAtRequiredFrequency;
	private Generator generator;
	private boolean isAstaticRegulationTurnedOn;
	private float frequencyInPowerSystem;
	
	public float getGeneratorPowerInMW(){
		frequencyInPowerSystem = simulation.getFrequencyInPowerSystem();
		
		if(isAstaticRegulationTurnedOn){
			regulationUnit.verifyAndAdjustPowerAtRequiredFrequency(frequencyInPowerSystem);
		}
		
		return calculateGeneratorPowerInMW();
	}	
		
	private float calculateGeneratorPowerInMW(){	
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
		return powerAtRequiredFrequency + (requiredFrequency - 
				frequencyInPowerSystem) / coefficientOfStatism;
	}
	
	private boolean isPowerMoreThanGeneratorNominal(float power){
		return power > generator.getNominalPowerInMW();
	}
	
	private boolean isPowerLessThanGeneratorMinimalTechnology(float power){
		return power < generator.getMinimalTechnologyPower();
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

	public boolean isAstaticRegulationTurnedOn() {
		return isAstaticRegulationTurnedOn;
	}

	public void TurnOnAstaticRegulation(){
		isAstaticRegulationTurnedOn = true;
	}
	
	public void TurnOffAstaticRegulation(){
		isAstaticRegulationTurnedOn = false;
	}

	public void setElectricPowerSystemSimulation(ElectricPowerSystemSimulation simulation) {
		this.simulation = simulation;
	}

	public void setAstaticRegulationUnit(AstaticRegulatioUnit regulationUnit) {
		this.regulationUnit = regulationUnit;
	}
}
