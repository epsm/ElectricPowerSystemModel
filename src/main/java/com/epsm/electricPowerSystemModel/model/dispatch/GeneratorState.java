package main.java.com.epsm.electricPowerSystemModel.model.dispatch;

import java.text.DecimalFormat;

import main.java.com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;

public class GeneratorState extends PowerObjectState implements Comparable<GeneratorState>{
	private  int generatorNumber;
	private float generationInWM;
	private StringBuilder stringBuilder;
	private DecimalFormat formatter;
	
	public GeneratorState(int generatorNumber, float generationInWM) {
		this.generatorNumber = generatorNumber;
		this.generationInWM = generationInWM;

		stringBuilder = new StringBuilder();
		formatter = new DecimalFormat("0000.000", GlobalConstatnts.SYMBOLS);
	}

	public int getGeneratorNumber() {
		return generatorNumber;
	}

	public float getGenerationInWM() {
		return generationInWM;
	}

	@Override
	public int compareTo(GeneratorState o) {
		if(generationInWM - o.generatorNumber < 0){
			return +1;
		}else if(generationInWM - o.generatorNumber > 0){
			return -1;
		}
		
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + generatorNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		
		GeneratorState other = (GeneratorState) obj;
		
		if(generatorNumber != other.generatorNumber){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("¹");
		stringBuilder.append(generatorNumber);
		stringBuilder.append(" MW: ");
		stringBuilder.append(formatter.format(generationInWM));

		return stringBuilder.toString();
	}
}
