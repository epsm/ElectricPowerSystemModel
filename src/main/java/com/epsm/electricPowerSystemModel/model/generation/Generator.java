package main.java.com.epsm.electricPowerSystemModel.model.generation;

import main.java.com.epsm.electricPowerSystemModel.model.dispatch.GeneratorState;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.PowerObjectState;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.StateSource;

public class Generator implements StateSource{
	private int number;
	private StaticRegulator staticRegulator;
	private AstaticRegulator astaticRegulator;
	private float nominalPowerInMW;
	private float minimalPowerInMW;
	private float currentGeneration;
	private boolean turnedOn;
	private boolean astaticRegulationTurnedOn;
	private GeneratorState state;

	public Generator(int number){
		this.number = number;
		prepareState();
	}
	
	public float calculateGeneration(){
		if(turnedOn){
			calculateCurrentGeneration();
			prepareState();
			return currentGeneration;
		}else{
			prepareState();
			return 0;
		}
	}
	
	private void calculateCurrentGeneration(){
		if(astaticRegulationTurnedOn){
			astaticRegulator.verifyAndAdjustPowerAtRequiredFrequency();
		}
		currentGeneration = staticRegulator.getGeneratorPowerInMW();
	}
	
	@Override
	public PowerObjectState getState(){
		return state;
	}
	
	private void prepareState(){
		state = new GeneratorState(number, currentGeneration);
	}
	
	public int getNumber(){
		return number;
	}
	
	public void setStaticRegulator(StaticRegulator staticRegulator) {
		this.staticRegulator = staticRegulator;
	}
	
	public void setAstaticRegulator(AstaticRegulator astaticRegulator) {
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
