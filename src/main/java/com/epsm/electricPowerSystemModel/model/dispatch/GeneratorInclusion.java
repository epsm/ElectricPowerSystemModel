package com.epsm.electricPowerSystemModel.model.dispatch;

public class GeneratorInclusion extends MessageInclusion{
	public GeneratorInclusion(int inclusionNumber) {
		super(inclusionNumber);
	}

	public int getGeneratorNumber() {
		return getInclusionNumber();
	}
}
