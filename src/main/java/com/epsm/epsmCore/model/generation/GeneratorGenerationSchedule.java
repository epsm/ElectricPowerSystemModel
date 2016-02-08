package com.epsm.epsmCore.model.generation;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.epsmCore.model.bothConsumptionAndGeneration.MessageInclusion;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GeneratorGenerationSchedule extends MessageInclusion{
	
	@JsonProperty("generatorTurnedOn")
	private boolean generatorTurnedOn;
	
	@JsonProperty("astaticRegulatorTurnedOn")
	private boolean astaticRegulatorTurnedOn;
	
	@JsonProperty("generationCurve")
	private LoadCurve generationCurve;
	
	@JsonCreator
	public GeneratorGenerationSchedule(
			@JsonProperty("generatorNumber") int generatorNumber, 
			@JsonProperty("generatorTurnedOn") boolean isGeneratorTurnedOn,
			@JsonProperty("astaticRegulatorTurnedOn") boolean isAstaticRegulatorTurnedOn, 
			@JsonProperty("generationCurve") LoadCurve generationCurve) {
		
		super(generatorNumber);
		generatorTurnedOn = isGeneratorTurnedOn;
		astaticRegulatorTurnedOn = isAstaticRegulatorTurnedOn;
		this.generationCurve = generationCurve;
	}
	
	public int getGeneratorNumber(){
		return getInclusionNumber();
	}
	
	public boolean isGeneratorTurnedOn() {
		return generatorTurnedOn;
	}

	public boolean isAstaticRegulatorTurnedOn() {
		return astaticRegulatorTurnedOn;
	}

	public LoadCurve getGenerationCurve() {
		return generationCurve;
	}

	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("<Generator#");
		stringBuilder.append(getInclusionNumber());
		stringBuilder.append(", turnedOn: ");
		stringBuilder.append(isGeneratorTurnedOn());
		stringBuilder.append(", astatic regulation: ");
		stringBuilder.append(astaticRegulatorTurnedOn);
		stringBuilder.append(", gen.curve: ");
		stringBuilder.append(generationCurve);
		stringBuilder.append(">");
		
		return stringBuilder.toString();
	}
}
