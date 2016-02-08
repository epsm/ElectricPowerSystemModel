package com.epsm.epsmCore.model.generation;

import java.time.Duration;
import java.time.LocalDateTime;

import com.epsm.epsmCore.model.generalModel.Constants;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;

public class AstaticRegulator {
	private ElectricPowerSystemSimulation simulation;
	private Generator generator;
	private float currentFrequency;
	private float powerAtRequiredFrequency;
	private float regulationStep;
	private LocalDateTime currentDateTime;
	private LocalDateTime previousDateTime;

	public AstaticRegulator(ElectricPowerSystemSimulation simulation, Generator generator) {
		this.simulation = simulation;
		this.generator = generator;
		previousDateTime = simulation.getDateTimeInSimulation();
		
		generator.setAstaticRegulator(this);
	}
	
	public void verifyAndAdjustPowerAtRequiredFrequency(){
		getNecessaryParametersFromPowerSystem();
		
		if(! isFrequencyInNonSensivityLimit()){
			adjustPowerAtRequiredFrequency();
		}
		
		setPreviousTime();
	}
	
	private void getNecessaryParametersFromPowerSystem(){
		currentDateTime = simulation.getDateTimeInSimulation();
		currentFrequency = simulation.getFrequencyInPowerSystem();
	}

	private boolean isFrequencyInNonSensivityLimit(){
		float deviation = Math.abs(currentFrequency - Constants.STANDART_FREQUENCY);
		
		return deviation <= Constants.ASTATIC_REGULATION_DEAD_ZONE;
	}

	private void adjustPowerAtRequiredFrequency(){
		getPowerAtRequiredFrequency();
		calculateRegulationStep();
		
		if(currentFrequency < Constants.STANDART_FREQUENCY){
			increasePowerAtRequiredFrequency();
		}else{
			decreasePowerAtRequiredFrequency();
		}
	}
	
	private void getPowerAtRequiredFrequency(){
		powerAtRequiredFrequency = generator.getPowerAtRequiredFrequency();
	}
	
	private void calculateRegulationStep(){
		float regulationSpeedInMWPerMillis = generator.getReugulationSpeedInMWPerMinute() / 60 / 1000;
		Duration duration = Duration.between(previousDateTime, currentDateTime);
		long durationInMillis = Math.abs(duration.toMillis());
		
		regulationStep =  durationInMillis * regulationSpeedInMWPerMillis;
	}
	
	private void increasePowerAtRequiredFrequency(){
		if(powerAfterIncreasionWillBePermissible()){
			generator.setPowerAtRequiredFrequency(powerAtRequiredFrequency + regulationStep);
		}
	}
	
	private boolean powerAfterIncreasionWillBePermissible(){
		return powerAtRequiredFrequency + regulationStep < generator.getNominalPowerInMW();
	}
	
	private void decreasePowerAtRequiredFrequency(){
		if(powerAfterDecreasionWillBePermissible()){
			generator.setPowerAtRequiredFrequency(powerAtRequiredFrequency - regulationStep);
		}
	}
	
	private boolean powerAfterDecreasionWillBePermissible(){
		return powerAtRequiredFrequency - regulationStep > generator.getMinimalPowerInMW();
	}

	private void setPreviousTime(){
		previousDateTime = currentDateTime;
	}
}