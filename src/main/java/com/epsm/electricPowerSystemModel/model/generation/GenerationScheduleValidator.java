package com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.CommandValidator;
import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;

public class GenerationScheduleValidator extends CommandValidator{
	private PowerStationGenerationSchedule schedule;
	private PowerStationParameters parameters;
	private Collection<Integer> stationGeneratorsNumbers;
	private Collection<Integer> scheduleGeneratorsNumbers;
	private GeneratorGenerationSchedule currentGeneratorSchedule;
	private GeneratorParameters generatorParameters;
	private HashSet<Integer> turnedOnGeneratorsNumbers;
	private float maxGenerationPower;
	private float minGenerationPower;
	
	public void validate(PowerStationGenerationSchedule schedule,
			PowerStationParameters parameters) throws GenerationException{
		
		super.validate(schedule, parameters);
		saveMessagesToValidate(schedule, parameters);
		getStationAndScheduledGeneratorNumbers();
		validateOnEqualsGeneratorsNumbers();
		
		removeTurnedOffGeneratorsFromValidating();
		validateGenerationScheduleOnCorectness();
	}
	
	private void saveMessagesToValidate(PowerStationGenerationSchedule schedule,
			PowerStationParameters parameters){
		this.schedule = schedule;
		this.parameters = parameters;
	}
	
	private void getStationAndScheduledGeneratorNumbers(){
		stationGeneratorsNumbers = parameters.getGeneratorParametersNumbers();
		scheduleGeneratorsNumbers = schedule.getGeneratorsNumbers();
	}
	
	private void validateOnEqualsGeneratorsNumbers(){
		if(!(stationGeneratorsNumbers.equals(scheduleGeneratorsNumbers))){
			String message = String.format("GenerationScheduleValidator: station has"
					+ " generator(s) with number(s) %s, but schedule has generator(s)"
					+ " with number(s) %s.", stationGeneratorsNumbers, scheduleGeneratorsNumbers);
			throw new GenerationException(message);
		}
	}

	private void removeTurnedOffGeneratorsFromValidating(){
		turnedOnGeneratorsNumbers = suggestAllGeneratorsTurnedOn();
		verifyEveryGeneratorIfItScheduledBeTurnedOff();
		leaveInScheduleGeneratorsNumbersOnlyGeneratorsScheduledBeTurnedOn();
	}
	
	private HashSet<Integer> suggestAllGeneratorsTurnedOn(){
		return new HashSet<Integer>(stationGeneratorsNumbers);
	}
	
	private void verifyEveryGeneratorIfItScheduledBeTurnedOff(){
		for(Integer generatorNumber: stationGeneratorsNumbers){
			if(isGeneratorScheduledBeTurnedOff(generatorNumber)){
				removeTurnedOnGeneratorFromTurnedOnGeneratorList(generatorNumber);
			}
		}
	}
	
	private boolean isGeneratorScheduledBeTurnedOff(int generatorNumber){
		currentGeneratorSchedule = schedule.getGeneratorSchedule(generatorNumber);
		return !currentGeneratorSchedule.isGeneratorTurnedOn();
	}
	
	private void removeTurnedOnGeneratorFromTurnedOnGeneratorList(int generatorNumber){
		turnedOnGeneratorsNumbers.remove(generatorNumber);
	}
	
	private void leaveInScheduleGeneratorsNumbersOnlyGeneratorsScheduledBeTurnedOn(){
		scheduleGeneratorsNumbers = turnedOnGeneratorsNumbers;
	}
	
	private void validateGenerationScheduleOnCorectness(){
		thereIsGenerationCurvesIfAstaticRegulationTurnedOff();
		powerInGenerationCurveWithinGeneratorCapabilities();
	}
	
	private void thereIsGenerationCurvesIfAstaticRegulationTurnedOff(){
		for(Integer generatorNumber: scheduleGeneratorsNumbers){
			IsCurvePresentIfAstaticRegulationTurnedOff(generatorNumber);
		}
	}
	
	private void IsCurvePresentIfAstaticRegulationTurnedOff(int generatorNumber){
		currentGeneratorSchedule = schedule.getGeneratorSchedule(generatorNumber);
		if(isAstaticRegulationTurnedOffAndThereIsNotGenerationCurve(currentGeneratorSchedule)){
			String message = String.format("GenerationScheduleValidator: there is no necessary"
					+ " generation curve for generator#%d.", generatorNumber);
			throw new GenerationException(message);
		}
	}
	
	private boolean isAstaticRegulationTurnedOffAndThereIsNotGenerationCurve(GeneratorGenerationSchedule schedule){
		boolean astaticRegulationTurnedOff = !currentGeneratorSchedule.isAstaticRegulatorTurnedOn();
		LoadCurve curve = currentGeneratorSchedule.getCurve();
		
		return astaticRegulationTurnedOff && curve == null;
	}
	
	private void powerInGenerationCurveWithinGeneratorCapabilities(){
		for(Integer generatorNumber: scheduleGeneratorsNumbers){
			verifyEveryGeneratorIsPowerInGenerationCurveWithinGeneratorCapabilities(generatorNumber);
		}
	}
	
	private void verifyEveryGeneratorIsPowerInGenerationCurveWithinGeneratorCapabilities(
			int generatorNumber){
		currentGeneratorSchedule = schedule.getGeneratorSchedule(generatorNumber);
		generatorParameters = parameters.getGeneratorParameters(generatorNumber);
		
		if(isAstaticRegulationTurnedOffAndThereIsGenerationCurve(currentGeneratorSchedule)){
			findMinAndMaxPowerInGenerationCurve();
			IsPowerInGenerationCurveNotHigherThanGeneratorNominalPower(generatorNumber);
			IsPowerInGenerationCurveNotLowerThanGeneratorNominalPower(generatorNumber);
		}
	}
	
	private boolean isAstaticRegulationTurnedOffAndThereIsGenerationCurve(
			GeneratorGenerationSchedule schedule){
		boolean astaticRegulationTurnedOff = !currentGeneratorSchedule.isAstaticRegulatorTurnedOn();
		LoadCurve curve = currentGeneratorSchedule.getCurve();
		
		return astaticRegulationTurnedOff && curve != null;
	}
	
	private void findMinAndMaxPowerInGenerationCurve(){
		LoadCurve generationCurve = currentGeneratorSchedule.getCurve();
		LocalTime pointer = LocalTime.MIDNIGHT;
		maxGenerationPower = Float.MIN_VALUE;
		minGenerationPower = Float.MAX_VALUE;
		do{
			float currentPower = generationCurve.getPowerOnTimeInMW(pointer);
			
			if(currentPower > maxGenerationPower){
				maxGenerationPower = currentPower;
			}
			if(currentPower < minGenerationPower){
				minGenerationPower = currentPower;
			}
			
			pointer = pointer.plusHours(1);
		}while(pointer.isAfter(LocalTime.MIDNIGHT));
	}
	
	private void IsPowerInGenerationCurveNotHigherThanGeneratorNominalPower(int generatorNumber){
		float generatorNominalPower = generatorParameters.getNominalPowerInMW();

		if(maxGenerationPower > generatorNominalPower){
			String message = String.format("GenerationScheduleValidator: scheduled generation"
					+ " power for generator#%d is more than nominal.", generatorNumber);
			throw new GenerationException(message);
		}
	}

	private void IsPowerInGenerationCurveNotLowerThanGeneratorNominalPower(int generatorNumber){
		float minimalGeneratorTechnologyPower = generatorParameters.getMinimalTechnologyPower();

		if(minGenerationPower < minimalGeneratorTechnologyPower){
			String message = String.format("GenerationScheduleValidator: scheduled generation"
					+ " power for generator#%d is less than minimal technology.",
					generatorNumber);

			throw new GenerationException(message);
		}
	}
}
