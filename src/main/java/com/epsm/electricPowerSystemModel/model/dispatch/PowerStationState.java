package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class PowerStationState extends MessageInclusionsContainer{
	public PowerStationState(long powerObjectId, LocalDateTime realTimeStamp, LocalTime simulationTimeStamp,
			int quantityOfInclusions, float frequency) {
		
		super(powerObjectId, realTimeStamp, simulationTimeStamp, quantityOfInclusions);
		this.frequency = frequency;
	}

	private float frequency;

	public float getFrequency() {
		return frequency;
	}
	
	public void addGeneratorState(GeneratorState state){
		addInclusion(state);
	}
	
	public GeneratorState getGeneratorState(int generatorNumber){
		return (GeneratorState)getInclusion(generatorNumber);
	}

	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("PowerSt. with id ");
		stringBuilder.append(powerObjectId);
		stringBuilder.append(" [time: ");
		stringBuilder.append(simulationTimeStamp.format(timeFormatter));
		stringBuilder.append(" freq.: ");
		stringBuilder.append(numberFormatter.format(frequency));
		stringBuilder.append("Hz");
		stringBuilder.append(" gener.: ");
		stringBuilder.append(super.toString());
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
