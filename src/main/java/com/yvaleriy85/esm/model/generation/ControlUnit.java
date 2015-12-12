package main.java.com.yvaleriy85.esm.model.generation;

import main.java.com.yvaleriy85.esm.model.generalModel.ElectricPowerSystemSimulationImpl;

public class ControlUnit {
	private ElectricPowerSystemSimulationImpl powerSystemSimulation;
	private float coefficientOfStatism;
	private float requiredFrequency;
	private float powerAtRequiredFrequency;
	private Generator generator;
	private boolean isAstaticRegulationTurnedOn;
	private float frequencyInPowerSystem;
	private final float ASTATIC_REGULATION_SENSIVITY = 0.03f;
	private final float STANDART_FREQUENCY = 50;
	
	public float getGeneratorPowerInMW(){
		frequencyInPowerSystem = powerSystemSimulation.getFrequencyInPowerSystem();
		
		if(isAstaticRegulationTurnedOn){
			verifyAndAdjustPowerAtRequiredFrequency();
		}
		
		return calculateGeneratorPowerInMW();
	}	
		
	private void verifyAndAdjustPowerAtRequiredFrequency(){
		if(! isFrequencyInNonSensivityLimit()){
			adjustPowerAtRequiredFrequency();
		}
	}

	private boolean isFrequencyInNonSensivityLimit(){
		float deviation = Math.abs(frequencyInPowerSystem - STANDART_FREQUENCY);
		return deviation <=  ASTATIC_REGULATION_SENSIVITY;
	}

	private void adjustPowerAtRequiredFrequency(){
		if(frequencyInPowerSystem < STANDART_FREQUENCY){
			increasePowerAtRequiredFrequency();
		}else{
			decreasePowerAtRequiredFrequency();
		}
	}
	
	private void increasePowerAtRequiredFrequency(){
		if(powerAtRequiredFrequency < generator.getNominalPowerInMW()){
			powerAtRequiredFrequency++;
		}
	}
	
	private void decreasePowerAtRequiredFrequency(){
		if(powerAtRequiredFrequency > generator.getMinimalTechnologyPower()){
			powerAtRequiredFrequency--;
		}
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

	public void setElectricPowerSystemSimulation(ElectricPowerSystemSimulationImpl powerSystemSimulation) {
		this.powerSystemSimulation = powerSystemSimulation;
	}
}
