package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.simulation.Simulation;

public class Generator {

	private int generatorNumber;
	private StaticRegulator staticRegulator;
	private AstaticRegulator astaticRegulator;
	private float nominalPowerInMW;
	private float minimalPowerInMW;
	private float reugulationSpeedInMWPerMinute;
	private float currentGeneration;
	private boolean turnedOn;
	private boolean astaticRegulationTurnedOn;
	private GeneratorState state;

	public Generator(Simulation simulation, int generatorNumber){
		if(simulation == null){
			throw new IllegalArgumentException("Generator constructor: simulation must not be"
					+ " null.");
		}
		
		this.generatorNumber = generatorNumber;
		staticRegulator = new StaticRegulator(simulation, this);
		astaticRegulator = new AstaticRegulator(simulation, this);
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
	
	public GeneratorState getState(){
		return state;
	}
	
	private void prepareState(){
		state = new GeneratorState(generatorNumber, currentGeneration);
	}
	
	public int getGeneratorNumber(){
		return generatorNumber;
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
	
	public float getReugulationSpeedInMWPerMinute() {
		return reugulationSpeedInMWPerMinute;
	}

	public void setReugulationSpeedInMWPerMinute(float reugulationSpeedInMWPerMinute) {
		this.reugulationSpeedInMWPerMinute = reugulationSpeedInMWPerMinute;
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
