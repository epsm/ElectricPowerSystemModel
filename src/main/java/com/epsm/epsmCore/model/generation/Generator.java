package com.epsm.epsmCore.model.generation;

import java.time.LocalDateTime;

import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;

public class Generator{
	private ElectricPowerSystemSimulation simulation;
	private final int number;
	private SpeedController speedController;
	private GeneratorController generatorController;
	private final float nominalPowerInMW;
	private final float minimalPowerInMW;
	private final float reugulationSpeedInMWPerMinute;
	private LocalDateTime currentDateTime;
	private float currentFrequency;
	private float currentGeneration;
	private GeneratorState state;

	public Generator(ElectricPowerSystemSimulation simulation, int number, float nominalPowerInMW,
			float minimalPowerInMW, float reugulationSpeedInMWPerMinute){
		
		if(simulation == null){
			String message = "Generator constructor: simulation must not be null.";
			throw new IllegalArgumentException(message);
		}else if(nominalPowerInMW <= 0){
			String message = "Generator constructor: nominalPowerInMW must be positive.";
			throw new IllegalArgumentException(message);
		}else if(minimalPowerInMW <= 0){
			String message = "Generator constructor: minimalPowerInMW must be positive.";
			throw new IllegalArgumentException(message);
		}else if(reugulationSpeedInMWPerMinute <= 0){
			String message = "Generator constructor: reugulationSpeedInMWPerMinute must be positive.";
			throw new IllegalArgumentException(message);
		}else if(minimalPowerInMW > nominalPowerInMW ){
			String message = "Generator constructor: minimalPowerInMW can't be higher nominalPowerInMW.";
			throw new IllegalArgumentException(message);
		}
		
		this.simulation = simulation;
		this.number = number;
		this.nominalPowerInMW = nominalPowerInMW;
		this.minimalPowerInMW = minimalPowerInMW;
		this.reugulationSpeedInMWPerMinute = reugulationSpeedInMWPerMinute;
		speedController = new SpeedController(this);
	}
	
	public float calculateGeneration(){
		getParametersFromSimulation();
		calculateCurrentGeneration();
		prepareState();
		
		return currentGeneration;
	}
	
	private void getParametersFromSimulation(){
		currentDateTime = simulation.getDateTimeInSimulation();
		currentFrequency = simulation.getFrequencyInPowerSystem();
	}
	
	private void calculateCurrentGeneration(){
		generatorController.adjustGenerator(currentDateTime, currentFrequency);
		currentGeneration = speedController.getGenerationInMW(currentFrequency);
	}
	
	private void prepareState(){
		state = new GeneratorState(number, currentGeneration);
	}
	
	public GeneratorState getState(){
		return state;
	}
	
	public int getNumber(){
		return number;
	}
	
	public float getNominalPowerInMW() {
		return nominalPowerInMW;
	}

	public float getMinimalPowerInMW() {
		return minimalPowerInMW;
	}
	
	public float getReugulationSpeedInMWPerMinute() {
		return reugulationSpeedInMWPerMinute;
	}
}
