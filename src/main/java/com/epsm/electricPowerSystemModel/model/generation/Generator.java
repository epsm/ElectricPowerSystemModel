package main.java.com.epsm.electricPowerSystemModel.model.generation;

public class Generator {
	private int number;
	private StaticRegulator staticRegulator;
	private AstaticRegulatort astaticRegulator;
	private float nominalPowerInMW;
	private float minimalPowerInMW;
	private float currentGeneration;
	private boolean turnedOn;
	private boolean astaticRegulationTurnedOn;
	
	public Generator(int number){
		this.number = number;
	}
	
	public float calculateGeneration(){
		if(turnedOn){
			calculateCurrentGeneration();
			return currentGeneration;
		}else{
			return 0;
		}
	}
	
	private void calculateCurrentGeneration(){
		if(astaticRegulationTurnedOn){
			astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
		}
		currentGeneration = staticRegulator.getGeneratorPowerInMW();
	}
	
	public int getNumber(){
		return number;
	}
	
	public void setStaticRegulator(StaticRegulator staticRegulator) {
		this.staticRegulator = staticRegulator;
	}
	
	public void setAstaticRegulator(AstaticRegulatort astaticRegulator) {
		this.astaticRegulator = astaticRegulator;
	}
	
	public float getNominalPowerInMW() {
		return nominalPowerInMW;
	}

	public void setNominalPowerInMW(float nominalPowerInMW) {
		this.nominalPowerInMW = nominalPowerInMW;
	}

	public float getMinimalPowerInMW() {
		return minimalPowerInMW;
	}

	public void setMinimalPowerInMW(float minimalPowerInMW) {
		this.minimalPowerInMW = minimalPowerInMW;
	}
	
	public float getGenerationInMW(){
		return currentGeneration;
	}
	
	public boolean isTurnedOn(){
		return turnedOn;
	}

	public void turnOnGenerator(){
		turnedOn = true;
	}
	
	public void turnOffGenerator(){
		turnedOn = false;
	}

	public boolean isAstaticRegulationTurnedOn() {
		return astaticRegulationTurnedOn;
	}

	public void turnOnAstaticRegulation(){
		astaticRegulationTurnedOn = true;
	}
	
	public void turnOffAstaticRegulation(){
		astaticRegulationTurnedOn = false;
	}
	
	public float getPowerAtRequiredFrequency() {
		return staticRegulator.getPowerAtRequiredFrequency();
	}

	public void setPowerAtRequiredFrequency(float powerAtRequiredFrequency) {
		staticRegulator.setPowerAtRequiredFrequency(powerAtRequiredFrequency);
	}
}
