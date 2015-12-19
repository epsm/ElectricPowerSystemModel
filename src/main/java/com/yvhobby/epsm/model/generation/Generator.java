package main.java.com.yvhobby.epsm.model.generation;

public class Generator{
	private ControlUnit controlUnit;
	private AstaticRegulationUnit regulationUnit;
	private float nominalPowerInMW;
	private float minimalTechnologyPower;
	private float currentGeneration;
	private boolean generatorTurnedOn;
	private boolean astaticRegulationTurnedOn;
	private int number;//must not be changed after construction
	
	public Generator(int number){
		this.number = number;
	}
	
	public float getGenerationInMW(){
		if(generatorTurnedOn){
			calculateCurrentGeneration();
			return currentGeneration;
		}else{
			return 0;
		}
	}
	
	private void calculateCurrentGeneration(){
		if(astaticRegulationTurnedOn){
			regulationUnit.verifyAndAdjustPowerAtRequiredFrequency();
		}
		currentGeneration = controlUnit.getGeneratorPowerInMW();
	}
	
	public void setControlUnit(ControlUnit controlUnit) {
		this.controlUnit = controlUnit;
	}
	
	public void setAstaticRegulationUnit(AstaticRegulationUnit regulationUnit) {
		this.regulationUnit = regulationUnit;
	}

	public float getNominalPowerInMW() {
		return nominalPowerInMW;
	}

	public void setNominalPowerInMW(float nominalPowerInMW) {
		this.nominalPowerInMW = nominalPowerInMW;
	}

	public float getMinimalTechnologyPower() {
		return minimalTechnologyPower;
	}

	public void setMinimalTechnologyPower(float minimalTechnologyPower) {
		this.minimalTechnologyPower = minimalTechnologyPower;
	}

	public boolean isTurnedOn(){
		return generatorTurnedOn;
	}

	public void turnOnGenerator(){
		generatorTurnedOn = true;
	}
	
	public void turnOffGenerator(){
		generatorTurnedOn = false;
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
	
	public int getNumber(){
		return number;
	}

	public float getPowerAtRequiredFrequency() {
		return controlUnit.getPowerAtRequiredFrequency();
	}

	public void setPowerAtRequiredFrequency(float powerAtRequiredFrequency) {
		controlUnit.setPowerAtRequiredFrequency(powerAtRequiredFrequency);
	}

	public void setCoefficientOfStatism(float coefficientOfStatism) {
		controlUnit.setCoefficientOfStatism(coefficientOfStatism);
	}
}
