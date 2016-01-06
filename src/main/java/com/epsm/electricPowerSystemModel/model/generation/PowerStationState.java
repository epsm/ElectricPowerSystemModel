package com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.MessageInclusionsContainer;
import com.epsm.electricPowerSystemModel.model.dispatch.State;
import com.epsm.electricPowerSystemModel.util.PowerStationStateJsonDeserializer;
import com.epsm.electricPowerSystemModel.util.PowerStationStateJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = PowerStationStateJsonSerializer.class)
@JsonDeserialize(using = PowerStationStateJsonDeserializer.class)
public class PowerStationState extends State{
	private MessageInclusionsContainer<GeneratorState> states;
	
	public PowerStationState(long powerObjectId, LocalDateTime realTimeStamp, LocalTime simulationTimeStamp,
			int quantityOfInclusions, float frequency) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp);
		this.frequency = frequency;
		states = new MessageInclusionsContainer<GeneratorState>(quantityOfInclusions);
	}

	private float frequency;

	public float getFrequency() {
		return frequency;
	}
	
	public void addGeneratorState(GeneratorState state){
		states.addInclusion(state);
	}
	
	public GeneratorState getGeneratorState(int generatorNumber){
		return states.getInclusion(generatorNumber);
	}
	
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
		stringBuilder.append(simulationTimeStamp.format(timeFormatter));
		stringBuilder.append(" freq.: ");
		stringBuilder.append(numberFormatter.format(frequency));
		stringBuilder.append("Hz");
		stringBuilder.append(" gener.: ");
		stringBuilder.append(states.toString());
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
