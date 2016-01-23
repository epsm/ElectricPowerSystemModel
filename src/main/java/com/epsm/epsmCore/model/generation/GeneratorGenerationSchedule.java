package com.epsm.epsmCore.model.generation;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.epsmCore.model.bothConsumptionAndGeneration.MessageInclusion;

public class GeneratorGenerationSchedule extends MessageInclusion{
	private boolean generatorTurnedOn;
	private boolean astaticRegulatorTurnedOn;
	private LoadCurve generationCurve;
	
	public GeneratorGenerationSchedule(int generatorNumber, boolean isGeneratorTurnedOn,
			boolean isAstaticRegulatorTurnedOn, LoadCurve generationCurve) {
		
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