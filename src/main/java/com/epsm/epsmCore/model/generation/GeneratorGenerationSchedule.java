package com.epsm.epsmCore.model.generation;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.epsmCore.model.bothConsumptionAndGeneration.MessageInclusion;

public class GeneratorGenerationSchedule extends MessageInclusion{
	private boolean secondaryFrequencyRegulationOn;
	private LoadCurve generationCurve;
	
	public GeneratorGenerationSchedule(int generatorNumber,
			boolean secondaryFrequencyRegulationOn, LoadCurve generationCurve) {
		
		super(generatorNumber);
		this.secondaryFrequencyRegulationOn = secondaryFrequencyRegulationOn;
		this.generationCurve = generationCurve;
	}
	
	public int getGeneratorNumber(){
		return getInclusionNumber();
	}

	public boolean isSecondaryFrequencyRegulationOn() {
		return secondaryFrequencyRegulationOn;
	}

	public LoadCurve getGenerationCurve() {
		return generationCurve;
	}

	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("<Generator#");
		stringBuilder.append(getInclusionNumber());
		stringBuilder.append(", secondary frequency regulationOn: ");
		stringBuilder.append(secondaryFrequencyRegulationOn);
		stringBuilder.append(", gen.curve: ");
		stringBuilder.append(generationCurve);
		stringBuilder.append(">");
		
		return stringBuilder.toString();
	}
}
