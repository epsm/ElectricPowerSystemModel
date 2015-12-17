package main.java.com.yvhobby.epsm.model.dispatch;

public class GeneratorStateReport {
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
}
