package com.epsm.epsmCore.model.generation;

import java.time.Duration;
import java.time.LocalDateTime;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.epsmCore.model.generalModel.Constants;

public class GeneratorController{
	private SpeedController speedController;
	private GeneratorGenerationSchedule newSchedule;
	private GeneratorGenerationSchedule currentSchedule;
	private LocalDateTime currentDateTime;
	private LocalDateTime previousDateTime;
	private Generator generator;
	private LoadCurve generationCurve;
	private float currentFrequency;
	private float generationAtGivenFrequency;
	private final float DEAD_ZONE = 0.005f;
	
	public GeneratorController(Generator generator, SpeedController speedController) {
		if(generator == null){
			String message = "GeneratorController: generator must not be null.";
			throw new IllegalArgumentException(message);
		}else if(speedController == null){
			String message = "GeneratorController: speedController must not be null.";
			throw new IllegalArgumentException(message);
		}
		
		this.generator = generator;
		this.speedController = speedController;
		previousDateTime = LocalDateTime.MIN;
	}
	
	public void adjustGenerator(LocalDateTime currentDateTime, float frequency) {
		saveParameters(currentDateTime, frequency);
		setCurrentSchedule();
		
		if(isSecondaryFrequencyRegulationOn()){
			adjustPowerAccordingToFrequency();
		}else{
			
			adjustGenerationAccordingToSchedule();
		}
		
		setPreviousTime();
	}
	
	private void saveParameters(LocalDateTime currentDateTime, float frequency){
		this.currentDateTime = currentDateTime;
		currentFrequency = frequency;
	}
	
	private boolean isSecondaryFrequencyRegulationOn(){
		return currentSchedule.isSecondaryFrequencyRegulationOn();
	}
	
	private void adjustPowerAccordingToFrequency(){
		if(! isFrequencyInNDeadZone()){
			adjustGenerationAtGivenFrequency();
		}
	}
	
	private boolean isFrequencyInNDeadZone(){
		float deviation = Math.abs(currentFrequency - Constants.STANDART_FREQUENCY);
		
		return deviation <= DEAD_ZONE;
	}
	
	private void adjustGenerationAtGivenFrequency(){
		getGenerationAtGivenFrequency();
		
		if(currentFrequency < Constants.STANDART_FREQUENCY){
			increasePowerAtRequiredFrequency();
		}else{
			decreasePowerAtRequiredFrequency();
		}
	}
	
	private void getGenerationAtGivenFrequency(){
		generationAtGivenFrequency = speedController.getGenerationAtGivenFrequency();
	}
	
	private void increasePowerAtRequiredFrequency(){
		if(generationAtGivenFrequency < generator.getNominalPowerInMW()){
			speedController.setGenerationAtGivenFrequency(generationAtGivenFrequency
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
		if(generationAtGivenFrequency > generator.getMinimalPowerInMW()){
			speedController.setGenerationAtGivenFrequency(generationAtGivenFrequency 
					- calculateRegulationStep());
		}
	}
	
	private void setPreviousTime(){
		previousDateTime = currentDateTime;
	}
	
	private void setCurrentSchedule(){//thread safe
		currentSchedule = newSchedule;
	}
	
	private void adjustGenerationAccordingToSchedule(){
		getNewGenerationParameters();
		adjustGenerationPower();
	}

	private void getNewGenerationParameters(){
		generationCurve = currentSchedule.getGenerationCurve();
	}
	
	private void adjustGenerationPower(){
		float newGenerationPower = generationCurve.getPowerOnTimeInMW(currentDateTime.toLocalTime());
		speedController.setGenerationAtGivenFrequency(newGenerationPower);
	}
	
	public void setSchedule(GeneratorGenerationSchedule schedule){
		newSchedule = schedule;
	}
}