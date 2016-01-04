package com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalTime;
import java.util.Collection;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.electricPowerSystemModel.model.generation.Generator;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;

public class GeneratorsController{
	private PowerStationGenerationSchedule powerstationGenerationSchedule;
	private PowerStation station;
	private LocalTime currentTime;
	private Generator generator;
	private LoadCurve generationCurve;
	private boolean shouldGeneratorBeTurnedOn;
	private boolean shouldAstaticRegulationBeTurnedOn;
	
	public GeneratorsController(PowerStation station) {
		this.station = station;
	}
	
	public void adjustGenerators(PowerStationGenerationSchedule schedule, LocalTime currentTime) {
		powerstationGenerationSchedule = schedule;
		this.currentTime = currentTime;
		processEveryGenerationSchedule();
	}
	
	private void processEveryGenerationSchedule(){
		Collection<Integer> generatorNumbers = station.getGeneratorsNumbers();
		for(Integer generatorNumber: generatorNumbers){
			Generator generator = station.getGenerator(generatorNumber);
			rememberCurrentGenerator(generator);
			GeneratorGenerationSchedule generatorSchedule 
					= getGeneratorGenerationSchedule(generatorNumber);
			adjustGenerationAccordingToSchedule(generatorSchedule);
		}
	}

	private void rememberCurrentGenerator(Generator generator){
		this.generator = generator;
	}
	
	private GeneratorGenerationSchedule getGeneratorGenerationSchedule(int generatorNumber){
		return powerstationGenerationSchedule.getGeneratorSchedule(generatorNumber);
	}
	
	private void adjustGenerationAccordingToSchedule(GeneratorGenerationSchedule generatorSchedule){
		getNewGenerationParameters(generatorSchedule);
		adjustNecessaryParameters();
	}

	private void getNewGenerationParameters(GeneratorGenerationSchedule generatorSchedule){
		shouldGeneratorBeTurnedOn = generatorSchedule.isGeneratorTurnedOn();
		shouldAstaticRegulationBeTurnedOn = generatorSchedule.isAstaticRegulatorTurnedOn();
		generationCurve = generatorSchedule.getCurve();
	}
	
	private void adjustNecessaryParameters() {
		if(shouldGeneratorBeTurnedOn){
			vefifyAndTurnOnGenerator();
			adjustGeneration();
		}else{
			vefifyAndTurnOffGenerator();
		}
	}
	
	private void vefifyAndTurnOnGenerator(){
		if(! generator.isTurnedOn()){
			generator.turnOnGenerator();
		}
	}
	
	private void adjustGeneration(){
		if(shouldAstaticRegulationBeTurnedOn){
			vefifyAndTurnOnAstaticRegulation();
		}else{
			vefifyAndTurnOffAstaticRegulation();
			adjustGenerationPower();
		}
	}
	
	private void vefifyAndTurnOffGenerator() {
		if(generator.isTurnedOn()){
			generator.turnOffGenerator();
		}
	}
	
	private void vefifyAndTurnOnAstaticRegulation(){
		if(! generator.isAstaticRegulationTurnedOn()){
			generator.turnOnAstaticRegulation();
		}
	}
	
	private void vefifyAndTurnOffAstaticRegulation(){
		if(generator.isAstaticRegulationTurnedOn()){
			generator.turnOffAstaticRegulation();
		}
	}
	
	private void adjustGenerationPower(){
		float newGenerationPower = generationCurve.getPowerOnTimeInMW(currentTime);
		generator.setPowerAtRequiredFrequency(newGenerationPower);
	}
}