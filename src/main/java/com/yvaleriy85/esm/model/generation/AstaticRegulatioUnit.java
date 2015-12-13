package main.java.com.yvaleriy85.esm.model.generation;

import main.java.com.yvaleriy85.esm.model.generalModel.GlobalConstatnts;

public class AstaticRegulatioUnit {
	private ControlUnit controlUnit;
	private Generator generator;
	private float frequencyInPowerSystem;
	private final float ASTATIC_REGULATION_SENSIVITY = 0.03f;

	public void verifyAndAdjustPowerAtRequiredFrequency(float frequencyInPowerSystem){
		this.frequencyInPowerSystem = frequencyInPowerSystem;
		
		if(! isFrequencyInNonSensivityLimit()){
			adjustPowerAtRequiredFrequency();
		}
	}

	private boolean isFrequencyInNonSensivityLimit(){
		float deviation = Math.abs(frequencyInPowerSystem - GlobalConstatnts.STANDART_FREQUENCY);
		
		return deviation <= ASTATIC_REGULATION_SENSIVITY;
	}

	private void adjustPowerAtRequiredFrequency(){
		if(frequencyInPowerSystem < GlobalConstatnts.STANDART_FREQUENCY){
			increasePowerAtRequiredFrequency();
		}else{
			decreasePowerAtRequiredFrequency();
		}
	}
	
	private void increasePowerAtRequiredFrequency(){
		float powerAtRequiredFrequency = controlUnit.getPowerAtRequiredFrequency();
		
		if(powerAtRequiredFrequency < generator.getNominalPowerInMW()){
			controlUnit.setPowerAtRequiredFrequency(++powerAtRequiredFrequency);
		}
	}
	
	private void decreasePowerAtRequiredFrequency(){
		float powerAtRequiredFrequency = controlUnit.getPowerAtRequiredFrequency();
		
		if(powerAtRequiredFrequency > generator.getMinimalTechnologyPower()){
			controlUnit.setPowerAtRequiredFrequency(--powerAtRequiredFrequency);
		}
	}
	
	public void setGenerator(Generator generator) {
		this.generator = generator;
	}
	
	public void setControlUnit(ControlUnit controlUnit) {
		this.controlUnit = controlUnit;
	}
}

