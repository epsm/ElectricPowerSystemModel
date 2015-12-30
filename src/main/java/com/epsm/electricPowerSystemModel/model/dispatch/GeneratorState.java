package com.epsm.electricPowerSystemModel.model.dispatch;

public class GeneratorState extends PowerObjectState implements Comparable<GeneratorState>{
	
	private float generationInWM;
	
	public GeneratorState(long powerObjectId, int generatorNumber, float generationInWM) {
		super(powerObjectId);
		this.generatorNumber = generatorNumber;
		this.generationInWM = generationInWM;

	}

	

	public float getGenerationInWM() {
		return generationInWM;
	}

	@Override
	public int compareTo(GeneratorState o) {
		if(generationInWM - o.generatorNumber < 0){
			return -1;
		}else if(generationInWM - o.generatorNumber > 0){
			return +1;
		}
		
		return 0;
	}

	

	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("№");
		stringBuilder.append(generatorNumber);
		stringBuilder.append(" ");
		stringBuilder.append(numberFormatter.format(generationInWM));
		stringBuilder.append("MW");
		return stringBuilder.toString();
	}
}
