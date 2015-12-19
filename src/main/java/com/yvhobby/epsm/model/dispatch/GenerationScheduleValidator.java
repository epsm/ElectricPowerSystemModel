package main.java.com.yvhobby.epsm.model.dispatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration.LoadCurve;
import main.java.com.yvhobby.epsm.model.generation.PowerStationException;

public class GenerationScheduleValidator {
	private PowerStationGenerationSchedule stationSchedule;
	private PowerStationParameters stationParameters;
	private Collection<Integer> stationGeneratorsNumbers;
	private Collection<Integer> scheduleGeneratorsNumbers;
	private GeneratorGenerationSchedule currentGeneratorSchedule;
	private ArrayList<Integer> turnedOnGeneratorsNumbers;
	private final String HEADER = "Wrong schedule: ";
	
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
		anyScheduleIsNotNull();
	}
	
	public void stationParametersIsNotNull(){
		if(stationParameters == null){
			String message = "Station parameters is null.";
			throw new PowerStationException(message);
		}
	}
	
	public void generationScheduleIsNotNull(){
		if(stationSchedule == null){
			String message = "Station schedule is null.";
			throw new PowerStationException(message);
		}
	}
	
	private void anyScheduleIsNotNull(){
		Collection<Integer> generatorNumbers = stationSchedule.getGeneratorsNumbers();
		for(Integer generatorNumber: generatorNumbers){
			currentGeneratorSchedule = stationSchedule.getGeneratorGenerationSchedule(generatorNumber);
			verifyScheduleIsNotNull();
		}
	}
	
	private void verifyScheduleIsNotNull(){
		if(currentGeneratorSchedule == null){
			String message = HEADER + "one of schedules for generator is null.";
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
		scheduleReturnsGeneratorsWithNumberAsRequested();
		quantityOfGeneratorsOnStationConformsToTheirQuantityInSchedule();
		scheduleContainsTheSameGeneratorsNumbersAsPowerStation();
	}
	
	private void scheduleReturnsGeneratorsWithNumberAsRequested(){
		for(Integer generatorNumber: scheduleGeneratorsNumbers){
			verifyIsReturndGeneratorNumberConformsRequested(generatorNumber);
		}
	}
	
	private void verifyIsReturndGeneratorNumberConformsRequested(int requestedGeneratorNumber){
		currentGeneratorSchedule = stationSchedule.getGeneratorGenerationSchedule(requestedGeneratorNumber);
		int generatorNumberInSchedule = currentGeneratorSchedule.getGeneratorNumber();
		
		if(generatorNumberInSchedule != requestedGeneratorNumber){
			String message = HEADER + "numbers of requested and received generator doesn't match.";
			throw new PowerStationException(message);
		}
	}
	
	private void quantityOfGeneratorsOnStationConformsToTheirQuantityInSchedule(){
		int generatorsInStation =  stationParameters.getQuantityOfGenerators();
		int generatorsInSchedule = stationSchedule.getQuantityOfGenerators();
		
		if(generatorsInSchedule != generatorsInStation){
			String message = HEADER + "station has " + generatorsInStation + 
					" generator(s) but schedule has " + generatorsInSchedule + " generator(s).";
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
	
	private ArrayList<Integer> suggestAllGeneratorsTurnedOn(){
		return new ArrayList<Integer>(stationGeneratorsNumbers);
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
	}
	
	private void thereIsGenerationCurvesIfAstaticRegulationTurnedOff(){
		for(Integer generatorNumber: scheduleGeneratorsNumbers){
			verifyEveryGeneratorIsCurvePresentIfAstaticRegulationTurnedOff(generatorNumber);
		}
	}
	
	private void verifyEveryGeneratorIsCurvePresentIfAstaticRegulationTurnedOff(int generatorNumber){
		currentGeneratorSchedule = stationSchedule.getGeneratorGenerationSchedule(generatorNumber);
		
		if(isAstaticRegulationTurnedOffAndThereIsGenerationCurve(currentGeneratorSchedule)){
			String message = HEADER + "there is no necessary generation curve for generator " + 
					currentGeneratorSchedule.getGeneratorNumber() + ".";
			throw new PowerStationException(message);
		}
	}
	
	private boolean isAstaticRegulationTurnedOffAndThereIsGenerationCurve(GeneratorGenerationSchedule schedule){
		boolean astaticRegulationTurnedOff = !currentGeneratorSchedule.isAstaticRegulatorTurnedOn();
		LoadCurve curve = currentGeneratorSchedule.getCurve();
		
		return astaticRegulationTurnedOff && curve == null;
	}
}
