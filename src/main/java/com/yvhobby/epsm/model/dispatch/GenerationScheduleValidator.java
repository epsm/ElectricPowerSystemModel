package main.java.com.yvhobby.epsm.model.dispatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import main.java.com.yvhobby.epsm.model.generation.PowerStationException;

public class GenerationScheduleValidator {
	private PowerStationGenerationSchedule stationSchedule;
	private PowerStationParameters stationParameters;
	private Collection<Integer> stationGeneratorsNumbers;
	private Collection<Integer> scheduleGeneratorsNumbers;
	private Collection<GeneratorGenerationSchedule> turnedOnGeneratorsSchedules;
	private final String HEADER = "Wrong schedule: ";
	
	public void validate(PowerStationGenerationSchedule schedule,
			PowerStationParameters stationParameters) throws PowerStationException{
		this.stationSchedule = schedule;
		this.stationParameters = stationParameters;
		
		stationParametersIsNotNull();
		generationScheduleIsNotNull();
		generatorSchedulesContainerDoesNotNull();
		anyScheduleIsNotNull();
		
		getNecessaryDataFromStationScheduleAndStationParameters();
		
		quantityOfGeneratorsOnStationConformsToTheirQuantityInSchedule();
		scheduleContainsTheSameGeneratorsNumbersAsPowerStation();
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
	
	public void generatorSchedulesContainerDoesNotNull(){
		if(stationSchedule.getGeneratorGenerationSchedules() == null){
			String message = HEADER + "generator schedules container is null.";
			throw new PowerStationException(message);
		}
	}
	
	private void anyScheduleIsNotNull(){
		for(GeneratorGenerationSchedule schedule: stationSchedule.getGeneratorGenerationSchedules()){
			if(schedule == null){
				String message = HEADER + "one of schedule for generator is null.";
				throw new PowerStationException(message);
			}
		}
	}
	
	private void getNecessaryDataFromStationScheduleAndStationParameters(){
		getStationAndScheduledGeneraorNumbers();
		getTurnedOnGeneratorsSchedules();
	}
	
	private void getStationAndScheduledGeneraorNumbers(){
		stationGeneratorsNumbers = stationParameters.getGeneratorsNumbers();
		scheduleGeneratorsNumbers = stationSchedule.getGeneratorsNumbers();
	}
	
	private void getTurnedOnGeneratorsSchedules(){
		turnedOnGeneratorsSchedules = new ArrayList<GeneratorGenerationSchedule>();
		
		Collection<GeneratorGenerationSchedule> schedules = 
				stationSchedule.getGeneratorGenerationSchedules();
		
		for(GeneratorGenerationSchedule schedule: schedules){
			if(schedule.isGeneratorTurnedOn()){
				turnedOnGeneratorsSchedules.add(schedule);
			}
		}
	}
	
	private void quantityOfGeneratorsOnStationConformsToTheirQuantityInSchedule(){
		int generatorsInStation =  stationParameters.getQuantityOfGenerators();
		int generatorsInSchedule = stationSchedule.getQuantityOfGener();
		
		if(generatorsInSchedule != generatorsInStation){
			String message = HEADER + "station has + " + generatorsInStation + 
					" generators but schedule has " + generatorsInSchedule + "generators.";
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
	
	public void ifAstaticRegulationTurnedOffThereIsGenerationCurve(){
		for(GeneratorGenerationSchedule schedule: turnedOnGeneratorsSchedules){
			if(!schedule.isAstaticRegulatorTurnedOn() && schedule.getCurve() == null){
				String message = HEADER + "there is no generation curve for generator " + 
						schedule.getGeneratorNumbers() + ".";
				throw new PowerStationException(message);
			}
		}
	}
	
	private void verifyEverGenerator(){
		
	}
	
	private boolean isGeneratorTurnedOn(GeneratorGenerationSchedule schedule){
		return schedule.isGeneratorTurnedOn();
	}
}
