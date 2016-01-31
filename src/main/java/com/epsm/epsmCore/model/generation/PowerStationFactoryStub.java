package com.epsm.epsmCore.model.generation;

import java.time.LocalDateTime;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.AbstractPowerObjectFactory;
import com.epsm.epsmCore.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.TimeService;

public class PowerStationFactoryStub extends AbstractPowerObjectFactory{
	private PowerStation powerStation;
	private long powerObjectId;
	private PowerStationParameters parameters;
	private PowerStationGenerationSchedule stationSchedule;
	private LoadCurve generationCurveGen1;
	private LoadCurve generationCurveGen2;
	private LoadCurve generationCurveGen3;
	private LoadCurve generationCurveGen4;
	private LoadCurve generationCurveGen5;
	private GeneratorGenerationSchedule genrationSchedule_1;
	private GeneratorGenerationSchedule genrationSchedule_2;
	private GeneratorGenerationSchedule genrationSchedule_3;
	private GeneratorGenerationSchedule genrationSchedule_4;
	private GeneratorGenerationSchedule genrationSchedule_5;
	private GeneratorGenerationSchedule genrationSchedule_6;
	private final float[] GENERATION_BY_HOURS_GEN_1_2 = new float[]{
			145f,  145f,  145f,  145f, 	145f,  145f,
			145f,  145f,  145f,  145f,  145f,  145f,
			145f,  145f,  145f,  145f,  145f,  145f,
			145f,  145f,  145f,  145f,  145f,  145f 
	};
	private final float[] GENERATION_BY_HOURS_GEN_3 = new float[]{
			104f,  130f,  145f,  145f, 	145f,  145f,
			145f,  145f,  145f,  145f,  145f,  145f,
			145f,  145f,  145f,  133f,  120f,  145f,
			145f,  145f,  145f,  145f,  133f,  104 
	};
	private final float[] GENERATION_BY_HOURS_GEN_4 = new float[]{
			  0f,    0f,    0f,    0f, 	  0f,  91f,
			117f,  145f,  145f,  145f,  145f,  145f,
			145f,  145f,  145f,   90f,   90f,  145f,
			145f,  145f,  145f,    0f,  90f,    0f 
	};
	private final float[] GENERATION_BY_HOURS_GEN_5 = new float[]{
			0f,  0f,  0f,  0f, 	  0f,    0f,
			0f,  0f,  0f,  0f,    0f,    0f,
			0f,  0f,  0f,  0f,  145f,  145f,
			0f,  0f,  0f,  0f,    0f,    0f 
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
		LocalDateTime realTimeStamp = timeService.getCurrentDateTime();
		LocalDateTime simulationTimeStamp = simulation.getDateTimeInSimulation();
		parameters = new PowerStationParameters(powerObjectId, realTimeStamp,
				simulationTimeStamp, 6);
		GeneratorParameters parameters_1 = new GeneratorParameters(1, 150, 90);
		GeneratorParameters parameters_2 = new GeneratorParameters(2, 150, 90);
		GeneratorParameters parameters_3 = new GeneratorParameters(3, 150, 90);
		GeneratorParameters parameters_4 = new GeneratorParameters(4, 150, 90);
		GeneratorParameters parameters_5 = new GeneratorParameters(5, 150, 90);
		GeneratorParameters parameters_6 = new GeneratorParameters(6, 150, 90);
		
		parameters.addGeneratorParameters(parameters_1);
		parameters.addGeneratorParameters(parameters_2);
		parameters.addGeneratorParameters(parameters_3);
		parameters.addGeneratorParameters(parameters_4);
		parameters.addGeneratorParameters(parameters_5);
		parameters.addGeneratorParameters(parameters_6);
	}
	
	private void createDefaultGenerationSchedule(){
		stationSchedule = new PowerStationGenerationSchedule(powerObjectId,
				LocalDateTime.MIN, LocalDateTime.MIN, 6);
		generationCurveGen1 = new LoadCurve(GENERATION_BY_HOURS_GEN_1_2);
		generationCurveGen2 = new LoadCurve(GENERATION_BY_HOURS_GEN_1_2);
		generationCurveGen3 = new LoadCurve(GENERATION_BY_HOURS_GEN_3);
		generationCurveGen4 = new LoadCurve(GENERATION_BY_HOURS_GEN_4);
		generationCurveGen5 = new LoadCurve(GENERATION_BY_HOURS_GEN_5);
		genrationSchedule_1 = new GeneratorGenerationSchedule(1, true, false, generationCurveGen1);
		genrationSchedule_2 = new GeneratorGenerationSchedule(2, true, false, generationCurveGen2);
		genrationSchedule_3 = new GeneratorGenerationSchedule(3, true, false, generationCurveGen3);
		genrationSchedule_4 = new GeneratorGenerationSchedule(4, true, false, generationCurveGen4);
		genrationSchedule_5 = new GeneratorGenerationSchedule(5, true, false, generationCurveGen5);
		genrationSchedule_6 = new GeneratorGenerationSchedule(6, true, true, null);
		stationSchedule.addGeneratorSchedule(genrationSchedule_1);
		stationSchedule.addGeneratorSchedule(genrationSchedule_2);
		stationSchedule.addGeneratorSchedule(genrationSchedule_3);
		stationSchedule.addGeneratorSchedule(genrationSchedule_4);
		stationSchedule.addGeneratorSchedule(genrationSchedule_5);
		stationSchedule.addGeneratorSchedule(genrationSchedule_6);
	}
	
	private void createPowerStation(){
		powerStation = new PowerStation(simulation, timeService, dispatcher, parameters);
		Generator generator_1 = new Generator(simulation, 1);
		Generator generator_2 = new Generator(simulation, 2);
		Generator generator_3 = new Generator(simulation, 3);
		Generator generator_4 = new Generator(simulation, 4);
		Generator generator_5 = new Generator(simulation, 5);
		Generator generator_6 = new Generator(simulation, 6);
		
		generator_1.setMinimalPowerInMW(90);
		generator_2.setMinimalPowerInMW(90);
		generator_3.setMinimalPowerInMW(90);
		generator_4.setMinimalPowerInMW(90);
		generator_5.setMinimalPowerInMW(90);
		generator_6.setMinimalPowerInMW(90);
		generator_1.setNominalPowerInMW(150);
		generator_2.setNominalPowerInMW(150);
		generator_3.setNominalPowerInMW(150);
		generator_4.setNominalPowerInMW(150);
		generator_5.setNominalPowerInMW(150);
		generator_6.setNominalPowerInMW(150);
		generator_1.setReugulationSpeedInMWPerMinute(3);
		generator_2.setReugulationSpeedInMWPerMinute(3);
		generator_3.setReugulationSpeedInMWPerMinute(3);
		generator_4.setReugulationSpeedInMWPerMinute(3);
		generator_5.setReugulationSpeedInMWPerMinute(3);
		generator_6.setReugulationSpeedInMWPerMinute(3);
		
		powerStation.addGenerator(generator_1);
		powerStation.addGenerator(generator_2);
		powerStation.addGenerator(generator_3);
		powerStation.addGenerator(generator_4);
		powerStation.addGenerator(generator_5);
		powerStation.addGenerator(generator_6);
		
		powerStation.executeCommand(stationSchedule);
	}
}
