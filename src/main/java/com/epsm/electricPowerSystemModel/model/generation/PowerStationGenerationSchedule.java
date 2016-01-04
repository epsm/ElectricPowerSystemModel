package com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.MessageInclusionsContainer;
import com.epsm.electricPowerSystemModel.model.dispatch.Command;

public class PowerStationGenerationSchedule extends Command{
	private MessageInclusionsContainer<GeneratorGenerationSchedule> schedules;
	
	public PowerStationGenerationSchedule(long powerObjectId, LocalDateTime realTimeStamp,
			LocalTime simulationTimeStamp, int quanityOfGenerators) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
		schedules = new MessageInclusionsContainer<GeneratorGenerationSchedule>(
				quanityOfGenerators);
	}

	public void addGeneratorSchedule(GeneratorGenerationSchedule schedule){
		schedules.addInclusion(schedule);
	}
	
	public GeneratorGenerationSchedule getGeneratorSchedule(int generatorNumber){
		return schedules.getInclusion(generatorNumber);
	}
	
	public Set<Integer> getGeneratorsNumbers(){
		return schedules.getInclusionsNumbers();
	}
	
	public int getQuantityOfGenerators(){
		return schedules.getQuantityOfInclusions();
	}
	
	@Override
	public String toString(){
		return "PowerStationGenerationSchedule toString() stub.";
	}
}