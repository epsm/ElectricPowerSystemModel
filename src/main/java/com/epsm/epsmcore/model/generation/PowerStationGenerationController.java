package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.PowerCurve;
import com.epsm.epsmcore.model.common.PowerCurveProcessor;
import com.epsm.epsmcore.model.simulation.Simulation;

public class PowerStationGenerationController {

	private final Simulation simulation;
	private final PowerStation station;
	private final PowerCurveProcessor powerCurveProcessor = new PowerCurveProcessor();

	public PowerStationGenerationController(Simulation simulation, PowerStation station) {
		this.simulation = simulation;
		this.station = station;
	}
	
	public void adjustGenerators(PowerStationGenerationSchedule powerStationGenerationSchedule) {
		for(Integer generatorNumber: station.getGeneratorsNumbers()){
			adjustGenerator(
					station.getGenerator(generatorNumber),
					powerStationGenerationSchedule.getGeneratorSchedules().get(generatorNumber));
		}
	}
	
	private void adjustGenerator(Generator generator, GeneratorGenerationSchedule generatorGenerationSchedule) {
		if(generatorGenerationSchedule.isGeneratorTurnedOn()){
			vefifyAndTurnOnGenerator(generator);
			adjustGeneration(generator, generatorGenerationSchedule);
		}else{
			vefifyAndTurnOffGenerator(generator);
		}
	}
	
	private void vefifyAndTurnOnGenerator(Generator generator){
		if(!generator.isTurnedOn()){
			generator.turnOnGenerator();
		}
	}
	
	private void adjustGeneration(Generator generator, GeneratorGenerationSchedule generatorGenerationSchedule){
		if(generatorGenerationSchedule.isAstaticRegulatorTurnedOn()){
			vefifyAndTurnOnAstaticRegulation(generator);
		}else{
			vefifyAndTurnOffAstaticRegulation(generator);
			adjustGenerationPower(generator, generatorGenerationSchedule.getGenerationCurve());
		}
	}
	
	private void vefifyAndTurnOffGenerator(Generator generator) {
		if(generator.isTurnedOn()){
			generator.turnOffGenerator();
		}
	}
	
	private void vefifyAndTurnOnAstaticRegulation(Generator generator){
		if(! generator.isAstaticRegulationTurnedOn()){
			generator.turnOnAstaticRegulation();
		}
	}
	
	private void vefifyAndTurnOffAstaticRegulation(Generator generator){
		if(generator.isAstaticRegulationTurnedOn()){
			generator.turnOffAstaticRegulation();
		}
	}
	
	private void adjustGenerationPower(Generator generator, PowerCurve generationCurve){
		float newGenerationPower = powerCurveProcessor.getPowerOnTimeInMW(generationCurve, simulation.getDateTimeInSimulation().toLocalTime());
		generator.setPowerAtRequiredFrequency(newGenerationPower);
	}
}