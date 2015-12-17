package main.java.com.yvhobby.epsm.model.generation;

import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;

public class AstaticRegulatioUnit {
	private ElectricPowerSystemSimulation simulation;
	private ControlUnit controlUnit;
	private Generator generator;
	private float currentFrequency;
	private final float ASTATIC_REGULATION_SENSIVITY = 0.03f;

	public void verifyAndAdjustPowerAtRequiredFrequency(){
		currentFrequency = simulation.getFrequencyInPowerSystem();
		
		if(! isFrequencyInNonSensivityLimit()){
			adjustPowerAtRequiredFrequency();
		}
	}

	private boolean isFrequencyInNonSensivityLimit(){
		float deviation = Math.abs(currentFrequency - GlobalConstatnts.STANDART_FREQUENCY);
		
		return deviation <= ASTATIC_REGULATION_SENSIVITY;
	}

	private void adjustPowerAtRequiredFrequency(){
		if(currentFrequency < GlobalConstatnts.STANDART_FREQUENCY){
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
	
	public void setElectricPowerSystemSimulation(ElectricPowerSystemSimulation simulation) {
		this.simulation = simulation;
	}
}

