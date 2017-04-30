package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.PowerCurve;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class GenerationScheduleValidator {
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
		
		saveMessagesToValidate(schedule, parameters);
		getStationAndScheduledGeneratorNumbers();
		validateOnEqualsGeneratorsNumbers();
		
		removeTurnedOffGeneratorsFromValidating();
		validateGenerationScheduleOnCorectness();
	}
	
	private void saveMessagesToValidate(PowerStationGenerationSchedule schedule, PowerStationParameters parameters){
		this.schedule = schedule;
		this.parameters = parameters;
	}
	
	private void getStationAndScheduledGeneratorNumbers(){
		stationGeneratorsNumbers = parameters.getGeneratorParameters().keySet();
		scheduleGeneratorsNumbers = schedule.getGeneratorSchedules().keySet();
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
		return new HashSet<>(stationGeneratorsNumbers);
	}
	
	private void verifyEveryGeneratorIfItScheduledBeTurnedOff(){
		for(Integer generatorNumber: stationGeneratorsNumbers){
			if(isGeneratorScheduledBeTurnedOff(generatorNumber)){
				removeTurnedOnGeneratorFromTurnedOnGeneratorList(generatorNumber);
			}
		}
	}
	
	private boolean isGeneratorScheduledBeTurnedOff(int generatorNumber){
		currentGeneratorSchedule = schedule.getGeneratorSchedules().get(generatorNumber);
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
		generationInGenerationCurveWithinGeneratorCapabilities();
	}
	
	private void thereIsGenerationCurvesIfAstaticRegulationTurnedOff(){
		for(Integer generatorNumber: scheduleGeneratorsNumbers){
			IsCurvePresentIfAstaticRegulationTurnedOff(generatorNumber);
		}
	}
	
	private void IsCurvePresentIfAstaticRegulationTurnedOff(int generatorNumber){
		currentGeneratorSchedule = schedule.getGeneratorSchedules().get(generatorNumber);
		if(isAstaticRegulationTurnedOffAndThereIsNotGenerationCurve(currentGeneratorSchedule)){
			String message = String.format("GenerationScheduleValidator: there is no necessary"
					+ " generation curve for generator#%d.", generatorNumber);
			throw new GenerationException(message);
		}
	}
	
	private boolean isAstaticRegulationTurnedOffAndThereIsNotGenerationCurve(
			GeneratorGenerationSchedule schedule){
		
		boolean astaticRegulationTurnedOff = !currentGeneratorSchedule.isAstaticRegulatorTurnedOn();
		PowerCurve curve = currentGeneratorSchedule.getGenerationCurve();
		
		return astaticRegulationTurnedOff && curve == null;
	}
	
	private void generationInGenerationCurveWithinGeneratorCapabilities(){
		for(Integer generatorNumber: scheduleGeneratorsNumbers){
			verifyEveryGeneratorIsPowerInGenerationCurveWithinGeneratorCapabilities(generatorNumber);
		}
	}
	
	private void verifyEveryGeneratorIsPowerInGenerationCurveWithinGeneratorCapabilities(
			int generatorNumber){
		currentGeneratorSchedule = schedule.getGeneratorSchedules().get(generatorNumber);
		generatorParameters = parameters.getGeneratorParameters().get(generatorNumber);
		
		if(isAstaticRegulationTurnedOffAndThereIsGenerationCurve(currentGeneratorSchedule)){
			findMinAndMaxPowerInGenerationCurve();
			isPowerInGenerationCurveNotHigherThanGeneratorNominalPower(generatorNumber);
			isPowerInGenerationCurveNotLowerThanGeneratorNominalPower(generatorNumber);
		}
	}
	
	private boolean isAstaticRegulationTurnedOffAndThereIsGenerationCurve(
			GeneratorGenerationSchedule schedule){
		
		boolean astaticRegulationTurnedOff = !currentGeneratorSchedule.isAstaticRegulatorTurnedOn();
		PowerCurve curve = currentGeneratorSchedule.getGenerationCurve();
		
		return astaticRegulationTurnedOff && curve != null;
	}
	
	private void findMinAndMaxPowerInGenerationCurve(){
		PowerCurve generationCurve = currentGeneratorSchedule.getGenerationCurve();
		LocalTime pointer = LocalTime.MIDNIGHT;
		maxGenerationPower = Float.MIN_VALUE;
		minGenerationPower = Float.MAX_VALUE;

		for(int i = 0; i < generationCurve.getPowerByHoursInMW().size(); i++) {
			float currentPower = generationCurve.getPowerByHoursInMW().get(1);
			
			if(currentPower > maxGenerationPower){
				maxGenerationPower = currentPower;
			}
			if(currentPower != 0 && currentPower < minGenerationPower){
				minGenerationPower = currentPower;
			}
			
			pointer = pointer.plusHours(1);
		}
	}
	
	private void isPowerInGenerationCurveNotHigherThanGeneratorNominalPower(int generatorNumber){
		float generatorNominalPower = generatorParameters.getNominalPowerInMW();

		if(maxGenerationPower > generatorNominalPower){
			String message = String.format("GenerationScheduleValidator: scheduled generation"
					+ " power for generator#%d is more than nominal.", generatorNumber);
			throw new GenerationException(message);
		}
	}

	private void isPowerInGenerationCurveNotLowerThanGeneratorNominalPower(int generatorNumber){
		float minimalGeneratorTechnologyPower = generatorParameters.getMinimalTechnologyPower();

		if(minGenerationPower < minimalGeneratorTechnologyPower){
			String message = String.format("GenerationScheduleValidator: scheduled generation"
					+ " power for generator#%d is less than minimal technology.",
					generatorNumber);

			throw new GenerationException(message);
		}
	}
}