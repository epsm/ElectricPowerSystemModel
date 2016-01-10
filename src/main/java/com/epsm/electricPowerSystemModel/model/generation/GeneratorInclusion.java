package com.epsm.electricPowerSystemModel.model.generation;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.MessageInclusion;

public abstract class GeneratorInclusion extends MessageInclusion{
	public GeneratorInclusion(int inclusionNumber) {
		super(inclusionNumber);
	}

	public final int getGeneratorNumber(){
		return getInclusionNumber();
	}
}
