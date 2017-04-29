package com.epsm.epsmCore.model.generation;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PowerStationParameters extends Message {
	
	@JsonProperty("generators")
	private MessageInclusionsContainer<GeneratorParameters> generatorParameters;
	
	@JsonCreator
	public PowerStationParameters(
			@JsonProperty("powerObjectId") long powerObjectId,
			@JsonProperty("realTimeStamp") LocalDateTime realTimeStamp,
			@JsonProperty("simulationTimeStamp") LocalDateTime simulationTimeStamp,
			@JsonProperty("quantityOfGenerators") int quantityOfGenerators) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
		generatorParameters = new MessageInclusionsContainer<GeneratorParameters>(
				quantityOfGenerators);
	}

	public void addGeneratorParameters(GeneratorParameters parameters){
		generatorParameters.addInclusion(parameters);
	}
	
	public GeneratorParameters getGeneratorParameters(int number){
		return generatorParameters.getInclusion(number);
	}
	
	@JsonIgnore
	public Set<Integer> getGeneratorParametersNumbers(){
		return generatorParameters.getInclusionsNumbers();
	}
	
	public int getQuantityOfGenerators(){
		return generatorParameters.getQuantityOfInclusions();
	}
	
	@Override
	public String toString() {
		return String.format("PowerStationParameters#%d toString() stub", powerObjectId);
	}
}
