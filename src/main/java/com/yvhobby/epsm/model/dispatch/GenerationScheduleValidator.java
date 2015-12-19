package main.java.com.yvhobby.epsm.model.dispatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import main.java.com.yvhobby.epsm.model.generation.PowerStationException;

public class GenerationScheduleValidator {
	private PowerStationGenerationSchedule stationSchedule;
	private PowerStationParameters stationParameters;
	private Collection<Integer> stationGeneratorsId;
	private Collection<Integer> scheduleGeneratorsId;
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
		
		numbersOfGeneratorsOnStationConformsToTheirNubersInSchedule();
		scheduleContainsTheSameGeneratorsIdAsPowerStation();
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
		getStationAndScheduledGeneraorIdNumbers();
		getTurnedOnGeneratorsSchedules();
	}
	
	private void getStationAndScheduledGeneraorIdNumbers(){
		stationGeneratorsId = stationParameters.getGeneratorsId();
		scheduleGeneratorsId = stationSchedule.getGeneratorsId();
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
	
	private void numbersOfGeneratorsOnStationConformsToTheirNubersInSchedule(){
		int generatorsInStation =  stationParameters.getNumbersOfGenerators();
		int generatorsInSchedule = stationSchedule.getNumbersOfGenerators();
		
		if(generatorsInSchedule != generatorsInStation){
			String message = HEADER + "station has + " + generatorsInStation + 
					" generators but schedule has " + generatorsInSchedule + "generators.";
			throw new PowerStationException(message);
		}
	}
	
	private void scheduleContainsTheSameGeneratorsIdAsPowerStation(){
		//collections with the same elements added in different order can be not equal
		HashSet<Integer> stationGeneratorsIdSet = new HashSet<Integer>(stationGeneratorsId);
		HashSet<Integer> scheduleGeneratorsIdSet = new HashSet<Integer>(scheduleGeneratorsId); 
		
		if(!stationGeneratorsIdSet.equals(scheduleGeneratorsIdSet)){
			String message = HEADER + "station and schedule has different generator id numbers.";
			throw new PowerStationException(message);
		}
	}
	
	public void ifAstaticRegulationTurnedOffThereIsGenerationCurve(){
		for(GeneratorGenerationSchedule schedule: turnedOnGeneratorsSchedules){
			if(!schedule.isAstaticRegulatorTurnedOn() && schedule.getCurve() == null){
				String message = HEADER + "there is no generation curve for generator " + 
						schedule.getGeneratorId() + ".";
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
