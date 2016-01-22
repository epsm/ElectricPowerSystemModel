package com.epsm.epsmCore.model.generation;

import java.time.Duration;
import java.time.LocalDateTime;

import com.epsm.epsmCore.model.generalModel.Constants;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;

public class AstaticRegulator {
	private ElectricPowerSystemSimulation simulation;
	private Generator generator;
	private float currentFrequency;
	private LocalDateTime currentDateTime;
	private LocalDateTime previousDateTime;
	private final float ASTATIC_REGULATION_SENSIVITY = 0.03f;
	

	public AstaticRegulator(ElectricPowerSystemSimulation simulation, Generator generator) {
		this.simulation = simulation;
		this.generator = generator;
		previousDateTime = simulation.getDateTimeInSimulation();//for fist time, otherwise NPE
		
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
		
		return deviation <= ASTATIC_REGULATION_SENSIVITY;
	}

	private void adjustPowerAtRequiredFrequency(){
		if(currentFrequency < Constants.STANDART_FREQUENCY){
			increasePowerAtRequiredFrequency();
		}else{
			decreasePowerAtRequiredFrequency();
		}
	}
	
	private void increasePowerAtRequiredFrequency(){
		float powerAtRequiredFrequency = generator.getPowerAtRequiredFrequency();
		
		if(powerAtRequiredFrequency < generator.getNominalPowerInMW()){
			generator.setPowerAtRequiredFrequency(powerAtRequiredFrequency
					+ calculateRegulationStep());
		}
	}
	
	private float calculateRegulationStep(){
		float regulationSpeedInMWPerMills = generator.getReugulationSpeedInMWPerMinute() / 60 / 1000;
		Duration duration = Duration.between(previousDateTime, currentDateTime);
		long durationInMillis = Math.abs(duration.toMillis());
		
		return durationInMillis * regulationSpeedInMWPerMills;
	}
	
	private void decreasePowerAtRequiredFrequency(){
		float powerAtRequiredFrequency = generator.getPowerAtRequiredFrequency();
		
		if(powerAtRequiredFrequency > generator.getMinimalPowerInMW()){
			generator.setPowerAtRequiredFrequency(powerAtRequiredFrequency 
					- calculateRegulationStep());
		}
	}

	private void setPreviousTime(){
		previousDateTime = currentDateTime;
	}
}