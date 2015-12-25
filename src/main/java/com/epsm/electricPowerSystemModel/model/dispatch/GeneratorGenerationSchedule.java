package com.epsm.electricPowerSystemModel.model.dispatch;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;

public class GeneratorGenerationSchedule {
	private int generatorNumber;//must not be changed after construction
	private boolean GeneratorTurnedOn;
	private boolean AstaticRegulatorTurnedOn;
	private LoadCurve curve;

	public GeneratorGenerationSchedule(int generatorNumber, boolean generatorTurnedOn,
			boolean astaticRegulatorTurnedOn, LoadCurve curve) {
		this.generatorNumber = generatorNumber;
		GeneratorTurnedOn = generatorTurnedOn;
		AstaticRegulatorTurnedOn = astaticRegulatorTurnedOn;
		this.curve = curve;
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

	public int getGeneratorNumber() {
		return generatorNumber;
	}
}
