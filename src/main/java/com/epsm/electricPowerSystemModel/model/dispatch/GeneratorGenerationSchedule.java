package com.epsm.electricPowerSystemModel.model.dispatch;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;

public class GeneratorGenerationSchedule extends GeneratorInclusion{
	private boolean GeneratorTurnedOn;
	private boolean AstaticRegulatorTurnedOn;
	private LoadCurve curve;
	
	public GeneratorGenerationSchedule(int inclusionNumber) {
		super(inclusionNumber);
	}
	
	public boolean isGeneratorTurnedOn() {
		return GeneratorTurnedOn;
	}

	public boolean isAstaticRegulatorTurnedOn() {
		return AstaticRegulatorTurnedOn;
	}

	public LoadCurve getCurve() {
		return curve;
	}
}
