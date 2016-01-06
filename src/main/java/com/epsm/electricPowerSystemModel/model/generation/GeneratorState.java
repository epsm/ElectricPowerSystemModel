package com.epsm.electricPowerSystemModel.model.generation;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.MessageInclusion;

public class GeneratorState extends MessageInclusion{
	protected float generationInWM;
	
	public GeneratorState(int generatorNumber, float generationInWM) {
		super(generatorNumber);
		this.generationInWM = generationInWM;
	}

	public int getGeneratorNumber(){
		return getInclusionNumber();
	}
	
	public float getGenerationInWM() {
		return generationInWM;
	}

	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("#");
		stringBuilder.append(getInclusionNumber());
		stringBuilder.append(" ");
		stringBuilder.append(numberFormatter.format(generationInWM));
		stringBuilder.append("MW ");
		return stringBuilder.toString();
	}
}
