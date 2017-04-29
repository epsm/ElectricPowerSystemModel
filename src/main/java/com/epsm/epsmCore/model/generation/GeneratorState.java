package com.epsm.epsmCore.model.generation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GeneratorState extends MessageInclusion{
	
	@JsonProperty("generationInWM")
	protected float generationInWM;
	
	@JsonCreator
	public GeneratorState(
			@JsonProperty("generatorNumber")int generatorNumber,
			@JsonProperty("generationInWM") float generationInWM) {
		
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
