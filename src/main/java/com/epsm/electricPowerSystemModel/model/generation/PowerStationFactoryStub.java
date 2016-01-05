package com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.AbstractPowerObjectFactory;
import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class PowerStationFactoryStub extends AbstractPowerObjectFactory{
	private PowerStation powerStation;
	private long powerObjectId;
	private PowerStationParameters parameters;
	private PowerStationGenerationSchedule generationSchedule;
	private final float[] GENERATION_BY_HOURS = new float[]{
			55.15f,  50.61f,  47.36f,  44.11f, 	41.20f,  41.52f,
			40.87f,  48.66f,  64.89f,  77.86f,  85.00f,  84.34f,
			77.86f,  77.86f,  77.53f,  77.20f,  77.20f,  77.20f,
			77.20f,  77.20f,  77.20f,  77.20f,  77.20f,  77.20f 
	};
	
	public PowerStationFactoryStub(ElectricPowerSystemSimulation simulation,
			TimeService timeService, Dispatcher dispatcher){
		
		super(simulation, timeService, dispatcher);
	}

	public synchronized PowerStation createPowerStation(long powerObjectId,
			PowerStationCreationParametersStub parameters){
		
		saveValues(powerObjectId);
		createPowerStationParameters();
		createDefaultGenerationSchedule();
		createPowerStation();
		
		return powerStation;
	}
	
	private void saveValues(long powerObjectId){
		this.powerObjectId = powerObjectId;
	}
	
	private void createPowerStationParameters(){
		LocalDateTime realTimeStamp = timeService.getCurrentTime();
		LocalTime simulationTimeStamp = simulation.getTimeInSimulation();
		parameters = new PowerStationParameters(powerObjectId, realTimeStamp,
				simulationTimeStamp, 2);
		GeneratorParameters parameters_1 = new GeneratorParameters(1, 40, 5);
		GeneratorParameters parameters_2 = new GeneratorParameters(2, 100, 25);
		
		parameters.addGeneratorParameters(parameters_1);
		parameters.addGeneratorParameters(parameters_2);
	}
	
	private void createDefaultGenerationSchedule(){
		LoadCurve generationCurve;
		GeneratorGenerationSchedule genrationSchedule_1;
		GeneratorGenerationSchedule genrationSchedule_2;
		
		generationSchedule = new PowerStationGenerationSchedule(powerObjectId,
				LocalDateTime.MIN, LocalTime.MIN, 2);
		generationCurve = new LoadCurve(GENERATION_BY_HOURS);
		genrationSchedule_1 = new GeneratorGenerationSchedule(1, true, true, null);
		genrationSchedule_2 = new GeneratorGenerationSchedule(2, true, false, generationCurve);
		generationSchedule.addGeneratorSchedule(genrationSchedule_1);
		generationSchedule.addGeneratorSchedule(genrationSchedule_2);
	}
	
	private void createPowerStation(){
		powerStation = new PowerStation(simulation, timeService, dispatcher, parameters);
		Generator generator_1 = new Generator(simulation, 1);
		Generator generator_2 = new Generator(simulation, 2);
		
		generator_1.setMinimalPowerInMW(5);
		generator_1.setNominalPowerInMW(40);
		generator_2.setMinimalPowerInMW(25);
		generator_2.setNominalPowerInMW(100);
		generator_1.setReugulationSpeedInMWPerMinute(20);
		powerStation.addGenerator(generator_1);
		powerStation.addGenerator(generator_2);
		powerStation.executeCommand(generationSchedule);
	}
}
