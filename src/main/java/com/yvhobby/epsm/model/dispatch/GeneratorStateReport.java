package main.java.com.yvhobby.epsm.model.dispatch;

import java.text.DecimalFormat;

public class GeneratorStateReport implements Comparable<GeneratorStateReport>{
	private  int generatorNumber;
	boolean isTurnedOn;
	private float generationInWM;
	private StringBuilder stringBuilder;
	
	public GeneratorStateReport(int generatorNumber, boolean isTurnedOn, float generationInWM) {
		super();
		this.generatorNumber = generatorNumber;
		this.isTurnedOn = isTurnedOn;
		this.generationInWM = generationInWM;
		
		stringBuilder = new StringBuilder();
	}

	public int getGeneratorNumber() {
		return generatorNumber;
	}

	public boolean isTurnedOn() {
		return isTurnedOn;
	}

	public float getGenerationInWM() {
		return generationInWM;
	}

	@Override
	public int compareTo(GeneratorStateReport o) {
		if(generationInWM - o.generatorNumber < 0){
			return -1;
		}else if(generationInWM - o.generatorNumber > 0){
			return + 1;
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
		
		GeneratorStateReport other = (GeneratorStateReport) obj;
		
		if(generatorNumber != other.generatorNumber){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		DecimalFormat formatter = new DecimalFormat("0000.000");
		
		stringBuilder.setLength(0);
		stringBuilder.append("GeneratorStateReport ");
		stringBuilder.append("[generatorNumber=");
		stringBuilder.append(generatorNumber);
		stringBuilder.append(", generationInWM=");
		stringBuilder.append(formatter.format(generationInWM));
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
