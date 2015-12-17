package main.java.com.yvhobby.epsm.model.dispatch;

import main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration.LoadCurve;

public class GeneratorGenerationSchedule {
	private boolean GeneratorTurnedOn;
	private boolean AstaticRegulatorTurnedOn;
	private LoadCurve curve;
	
	public GeneratorGenerationSchedule(boolean generatorTurnedOn, boolean astaticRegulatorTurnedOn, LoadCurve curve) {
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
}
