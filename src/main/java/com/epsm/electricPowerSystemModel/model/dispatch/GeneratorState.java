package com.epsm.electricPowerSystemModel.model.dispatch;

public class GeneratorState extends GeneratorInclusion{
	public GeneratorState(int generatorNumber) {
		super(generatorNumber);
	}

	protected float gnerationInWM;

	public float getGenerationInWM() {
		return gnerationInWM;
	}

	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("№");
		stringBuilder.append(getGeneratorNumber());
		stringBuilder.append(" ");
		stringBuilder.append(numberFormatter.format(gnerationInWM));
		stringBuilder.append("MW");
		return stringBuilder.toString();
	}
}
