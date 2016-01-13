package com.epsm.epsmCore.model.generation;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.MessageInclusion;

public abstract class GeneratorInclusion extends MessageInclusion{
	public GeneratorInclusion(int inclusionNumber) {
		super(inclusionNumber);
	}

	public final int getGeneratorNumber(){
		return getInclusionNumber();
	}
}
