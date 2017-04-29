package com.epsm.epsmCore.model.generation;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PowerStationGenerationSchedule extends Message {
	
	@JsonProperty("generators")
	private MessageInclusionsContainer<GeneratorGenerationSchedule> schedules;
	
	@JsonCreator
	public PowerStationGenerationSchedule(
			@JsonProperty("powerObjectId") long powerObjectId, 
			@JsonProperty("realTimeStamp") LocalDateTime realTimeStamp,
			@JsonProperty("simulationTimeStamp") LocalDateTime simulationTimeStamp, 
			@JsonProperty("quantityOfGenerators") int quanityOfGenerators) {
		
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
	
	@JsonIgnore
	public Set<Integer> getGeneratorsNumbers(){
		return schedules.getInclusionsNumbers();
	}
	
	public int getQuantityOfGenerators(){
		return schedules.getQuantityOfInclusions();
	}
	
	@Override
	public String toString(){
		stringBuilder.setLength(0);
		stringBuilder.append("<Pow.St.Gen.Sch. for st.#");
		stringBuilder.append(powerObjectId);
		stringBuilder.append(" generators schedules: [");
		stringBuilder.append(schedules.toString());
		stringBuilder.append("]>");
		
		return stringBuilder.toString();
	}
}