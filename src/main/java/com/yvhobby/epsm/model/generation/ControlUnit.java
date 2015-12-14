package main.java.com.yvhobby.epsm.model.generation;

import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;

public class ControlUnit {
	private ElectricPowerSystemSimulation powerSystemSimulation;
	private AstaticRegulatioUnit regulationUnit;
	private float coefficientOfStatism;
	private float requiredFrequency;
	private float powerAtRequiredFrequency;
	private Generator generator;
	private boolean isAstaticRegulationTurnedOn;
	private float frequencyInPowerSystem;
	
	public float getGeneratorPowerInMW(){
		frequencyInPowerSystem = powerSystemSimulation.getFrequencyInPowerSystem();
		
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

	public void setRequiredFrequency(float requiredFrequency) {
		this.requiredFrequency = requiredFrequency;
	}

	public void setPowerAtRequiredFrequency(float powerAtRequiredFrequency) {
		this.powerAtRequiredFrequency = powerAtRequiredFrequency;
	}

	public boolean isAstaticRegulationTurnedOn() {
		return isAstaticRegulationTurnedOn;
	}

	public void TurneOnAstaticRegulation(){
		isAstaticRegulationTurnedOn = true;
	}
	
	public void TurneOffAstaticRegulation(){
		isAstaticRegulationTurnedOn = false;
	}

	public float getRequiredFrequency() {
		return requiredFrequency;
	}

	public float getPowerAtRequiredFrequency() {
		return powerAtRequiredFrequency;
	}

	public void setElectricPowerSystemSimulation(ElectricPowerSystemSimulation powerSystemSimulation) {
		this.powerSystemSimulation = powerSystemSimulation;
	}

	public void setAstaticRegulationUnit(AstaticRegulatioUnit regulationUnit) {
		this.regulationUnit = regulationUnit;
	}
}
