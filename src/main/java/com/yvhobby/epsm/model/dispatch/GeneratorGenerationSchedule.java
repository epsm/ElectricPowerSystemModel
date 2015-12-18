package main.java.com.yvhobby.epsm.model.dispatch;

import main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration.LoadCurve;

public class GeneratorGenerationSchedule {
	private int generatorId;
	private boolean GeneratorTurnedOn;
	private boolean AstaticRegulatorTurnedOn;
	private LoadCurve curve;

	public GeneratorGenerationSchedule(int generatorId, boolean generatorTurnedOn,
			boolean astaticRegulatorTurnedOn, LoadCurve curve) {
		this.generatorId = generatorId;
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

	public int getGeneratorId() {
		return generatorId;
	}
}
