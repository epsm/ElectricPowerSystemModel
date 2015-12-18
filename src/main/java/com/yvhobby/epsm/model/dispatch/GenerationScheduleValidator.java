package main.java.com.yvhobby.epsm.model.dispatch;

import java.util.Collection;
import java.util.HashSet;

import main.java.com.yvhobby.epsm.model.generation.PowerStationException;

public class GenerationScheduleValidator {
	private PowerStationGenerationSchedule stationSchedule;
	private PowerStationParameters stationParameters;
	private Collection<Integer> stationGeneratorsId;
	private Collection<Integer> scheduleGeneratorsId;
	private final String HEADER = "Wrong schedule: ";
	
	public void validate(PowerStationGenerationSchedule schedule,
			PowerStationParameters stationParameters) throws PowerStationException{
		this.stationSchedule = schedule;
		this.stationParameters = stationParameters;
		
		getStationAndScheduledGeneraorIdNumbers();
		numbersOfGeneratorsOnStationConformsToTheirNubersInSchedule();
		scheduleContainsTheSameGeneratorsIdAsPowerStation();
	}
	
	private void getStationAndScheduledGeneraorIdNumbers(){
		stationGeneratorsId = stationParameters.getGeneratorsId();
		scheduleGeneratorsId = stationSchedule.getGeneratorsId();
	}
	
	private void numbersOfGeneratorsOnStationConformsToTheirNubersInSchedule(){
		int generatorsInStation =  stationParameters.getNumbersOfGenerators();
		int generatorsInSchedule = stationSchedule.getNumbersOfGenerators();
		
		if(generatorsInSchedule != generatorsInStation){
			String message = HEADER + "station has + " + generatorsInStation + 
					" generators but schedule has " + generatorsInSchedule + "generators";
			throw new PowerStationException(message);
		}
	}
	
	private void scheduleContainsTheSameGeneratorsIdAsPowerStation(){
		//collections with the same elements added in different order can be not equal
		HashSet<Integer> stationGeneratorsIdSet = new HashSet<Integer>(stationGeneratorsId);
		HashSet<Integer> scheduleGeneratorsIdSet = new HashSet<Integer>(scheduleGeneratorsId); 
		
		if(!stationGeneratorsIdSet.equals(scheduleGeneratorsIdSet)){
			String message = HEADER + "station and schedule has different generator id numbers";
			throw new PowerStationException(message);
		}
	}
	
	public void astaticRegulationTurnedOffAndThereIsNoGenerationCurve(){
		
	}
	
	private void verifyEverGenerator(){
		//for(GeneratorGenerationSchedule schedule: stationSchedule.)
	}
}
