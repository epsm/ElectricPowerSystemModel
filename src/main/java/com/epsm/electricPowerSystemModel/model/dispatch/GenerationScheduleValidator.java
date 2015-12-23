package main.java.com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;

import main.java.com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import main.java.com.epsm.electricPowerSystemModel.model.generation.PowerStationException;

public class GenerationScheduleValidator {
	private PowerStationGenerationSchedule stationSchedule;
	private PowerStationParameters stationParameters;
	private Collection<Integer> stationGeneratorsNumbers;
	private Collection<Integer> scheduleGeneratorsNumbers;
	private GeneratorGenerationSchedule currentGeneratorSchedule;
	private GeneratorParameters generatorParameters;
	private HashSet<Integer> turnedOnGeneratorsNumbers;
	private float maxGenerationPower;
	private float minGenerationPower;
	private final String HEADER = "wrong schedule: ";
	
	public void validate(PowerStationGenerationSchedule schedule,
			PowerStationParameters stationParameters) throws PowerStationException{
		this.stationSchedule = schedule;
		this.stationParameters = stationParameters;
		
		validateOnNullValues();
		getNecessaryDataFromStationScheduleAndStationParameters();
		validateOnConformityGeneratorsNumberInStationAndInSchedule();
		removeTurnedOffGeneratorsFromValidating();
		validateGenerationScheduleOnCorectness();
	}
	
	private void validateOnNullValues(){
		stationParametersIsNotNull();
		generationScheduleIsNotNull();
	}
	
	public void stationParametersIsNotNull(){
		if(stationParameters == null){
			String message = "station parameters is null.";
			throw new PowerStationException(message);
		}
	}
	
	public void generationScheduleIsNotNull(){
		if(stationSchedule == null){
			String message = "station schedule is null.";
			throw new PowerStationException(message);
		}
	}
	
	private void getNecessaryDataFromStationScheduleAndStationParameters(){
		getStationAndScheduledGeneraorNumbers();
	}
	
	private void getStationAndScheduledGeneraorNumbers(){
		stationGeneratorsNumbers = stationParameters.getGeneratorsNumbers();
		scheduleGeneratorsNumbers = stationSchedule.getGeneratorsNumbers();
	}
	
	private void validateOnConformityGeneratorsNumberInStationAndInSchedule(){
		quantityOfGeneratorsOnStationConformsToTheirQuantityInSchedule();
		scheduleContainsTheSameGeneratorsNumbersAsPowerStation();
	}
	
	private void quantityOfGeneratorsOnStationConformsToTheirQuantityInSchedule(){
		int generatorsInStation =  stationParameters.getQuantityOfGenerators();
		int generatorsInSchedule = stationSchedule.getQuantityOfGenerators();
		
		if(generatorsInSchedule != generatorsInStation){
			String message = HEADER + "station has " + generatorsInStation 
					+ " generator(s) but schedule has " + generatorsInSchedule + " generator(s).";
			throw new PowerStationException(message);
		}
	}
	
	private void scheduleContainsTheSameGeneratorsNumbersAsPowerStation(){
		//collections with the same elements but added in different order can be not equal
		HashSet<Integer> stationGeneratorsNumbersSet = new HashSet<Integer>(stationGeneratorsNumbers);
		HashSet<Integer> scheduleGeneratorsNumbersSet = new HashSet<Integer>(scheduleGeneratorsNumbers); 
		
		if(!stationGeneratorsNumbersSet.equals(scheduleGeneratorsNumbersSet)){
			String message = HEADER + "station and schedule has different generator numbers.";
			throw new PowerStationException(message);
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
		currentGeneratorSchedule = stationSchedule.getGeneratorGenerationSchedule(generatorNumber);
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
			verifyGeneratorIsCurvePresentIfAstaticRegulationTurnedOff(generatorNumber);
		}
	}
	
	private void verifyGeneratorIsCurvePresentIfAstaticRegulationTurnedOff(int generatorNumber){
		currentGeneratorSchedule = stationSchedule.getGeneratorGenerationSchedule(generatorNumber);
		if(isAstaticRegulationTurnedOffAndThereIsNotGenerationCurve(currentGeneratorSchedule)){
			String message = HEADER + "there is no necessary generation curve for generator "
					+ generatorNumber + ".";
			throw new PowerStationException(message);
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
		currentGeneratorSchedule = stationSchedule.getGeneratorGenerationSchedule(generatorNumber);
		generatorParameters = stationParameters.getGeneratorParameters(generatorNumber);
		
		if(isAstaticRegulationTurnedOffAndThereIsGenerationCurve(currentGeneratorSchedule)){
			findMinAndMaxPowerInGenerationCurve();
			verifyIsPowerInGenerationCurveNotHigherThanGeneratorNominalPower(generatorNumber);
			verifyIsPowerInGenerationCurveNotLowerThanGeneratorNominalPower(generatorNumber);
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
	
	private void verifyIsPowerInGenerationCurveNotHigherThanGeneratorNominalPower(int generatorNumber){
		float generatorNominalPower = generatorParameters.getNominalPowerInMW();

		if(maxGenerationPower > generatorNominalPower){
			String message = HEADER + "scheduled generation power for generator " + generatorNumber
					+ " is more than nominal.";
			throw new PowerStationException(message);
		}
	}

	private void verifyIsPowerInGenerationCurveNotLowerThanGeneratorNominalPower(int generatorNumber){
		float minimalGeneratorTechnologyPower = generatorParameters.getMinimalTechnologyPower();

		if(minGenerationPower < minimalGeneratorTechnologyPower){
			String message = HEADER + "scheduled generation power for generator " + generatorNumber
					+ " is less than minimal technology.";
			throw new PowerStationException(message);
		}
	}
}
