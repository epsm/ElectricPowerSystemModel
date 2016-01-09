package com.epsm.electricPowerSystemModel.model.generation;

import java.time.Duration;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.Constants;

public class AstaticRegulator {
	private ElectricPowerSystemSimulation simulation;
	private Generator generator;
	private float currentFrequency;
	private LocalTime currentTime;
	private LocalTime previousTime;
	private final float ASTATIC_REGULATION_SENSIVITY = 0.03f;
	

	public AstaticRegulator(ElectricPowerSystemSimulation simulation, Generator generator) {
		this.simulation = simulation;
		this.generator = generator;
		previousTime = simulation.getTimeInSimulation();//for fist time, otherwise NPE
		
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
		currentTime = simulation.getTimeInSimulation();
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
		Duration duration = Duration.between(previousTime, currentTime);
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
		previousTime = currentTime;
	}
}