package main.java.com.yvhobby.epsm.model.generation;

import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;

public class AstaticRegulationUnit {
	private ElectricPowerSystemSimulation simulation;
	private Generator generator;
	private float currentFrequency;
	private final float ASTATIC_REGULATION_SENSIVITY = 0.03f;

	public AstaticRegulationUnit(ElectricPowerSystemSimulation simulation, Generator generator) {
		this.simulation = simulation;
		this.generator = generator;
	}
	
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
		float powerAtRequiredFrequency = generator.getPowerAtRequiredFrequency();
		if(powerAtRequiredFrequency < generator.getNominalPowerInMW()){
			generator.setPowerAtRequiredFrequency(++powerAtRequiredFrequency);
		}
	}
	
	private void decreasePowerAtRequiredFrequency(){
		float powerAtRequiredFrequency = generator.getPowerAtRequiredFrequency();
		
		if(powerAtRequiredFrequency > generator.getMinimalTechnologyPower()){
			generator.setPowerAtRequiredFrequency(--powerAtRequiredFrequency);
		}
	}

}