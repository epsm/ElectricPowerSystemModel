package main.java.com.yvhobby.epsm.model.dispatch;

public class GeneratorStateReport implements Comparable<GeneratorStateReport>{
	private  int generatorId;
	boolean isTurnedOn;
	private float generationInWM;
	
	public GeneratorStateReport(int generatorId, boolean isTurnedOn, float generationInWM) {
		super();
		this.generatorId = generatorId;
		this.isTurnedOn = isTurnedOn;
		this.generationInWM = generationInWM;
	}

	public int getGeneratorId() {
		return generatorId;
	}

	public boolean isTurnedOn() {
		return isTurnedOn;
	}

	public float getGenerationInWM() {
		return generationInWM;
	}

	@Override
	public int compareTo(GeneratorStateReport o) {
		if(generationInWM - o.generatorId < 0){
			return -1;
		}else if(generationInWM - o.generatorId > 0){
			return + 1;
		}
		
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + generatorId;
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
		
		if(generatorId != other.generatorId){
			return false;
		}
		return true;
	}
	
	
}
