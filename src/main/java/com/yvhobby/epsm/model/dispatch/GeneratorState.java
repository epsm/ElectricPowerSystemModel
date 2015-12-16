package main.java.com.yvhobby.epsm.model.dispatch;

public class GeneratorState {
	private  int generatorId;
	boolean isTurnedOn;
	private float generationInWM;
	
	public GeneratorState(int generatorId, boolean isTurnedOn, float generationInWM) {
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
