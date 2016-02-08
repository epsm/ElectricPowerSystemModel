package com.epsm.epsmCore.model.generation;

import java.time.LocalDateTime;
import java.util.Set;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.MessageInclusionsContainer;
import com.epsm.epsmCore.model.dispatch.State;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PowerStationState extends State{
	
	@JsonProperty("generators")
	private MessageInclusionsContainer<GeneratorState> states;
	
	@JsonProperty("frequency")
	private float frequency;
	
	@JsonCreator
	public PowerStationState(
			@JsonProperty("powerObjectId") long powerObjectId,
			@JsonProperty("realTimeStamp") LocalDateTime realTimeStamp,
			@JsonProperty("simulationTimeStamp") LocalDateTime simulationTimeStamp,
			@JsonProperty("quantityOfGenerators") int quantityOfGenerators,
			@JsonProperty("frequency") float frequency) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
		this.frequency = frequency;
		states = new MessageInclusionsContainer<GeneratorState>(quantityOfGenerators);
	}

	public float getFrequency() {
		return frequency;
	}
	
	public void addGeneratorState(GeneratorState state){
		states.addInclusion(state);
	}
	
	public GeneratorState getGeneratorState(int generatorNumber){
		return states.getInclusion(generatorNumber);
	}
	
	@JsonIgnore
	public Set<Integer> getGeneratorsNumbers(){
		return states.getInclusionsNumbers();
	}
	
	public int getQuantityOfGenerators(){
		return states.getQuantityOfInclusions();
	}

	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("PowerSt.#");
		stringBuilder.append(powerObjectId);
		stringBuilder.append(" [sim.time: ");
		stringBuilder.append(simulationTimeStamp.toString());
		stringBuilder.append(" freq.: ");
		stringBuilder.append(numberFormatter.format(frequency));
		stringBuilder.append("Hz");
		stringBuilder.append(" gener.: ");
		stringBuilder.append(states.toString());
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
