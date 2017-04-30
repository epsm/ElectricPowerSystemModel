package com.epsm.epsmcore.model.generation;

import com.epsm.epsmcore.model.common.AbstractPowerObjectFactory;
import com.epsm.epsmcore.model.common.PowerCurve;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;
import com.epsm.epsmcore.model.simulation.TimeService;

import java.util.List;

import static java.util.Arrays.asList;

public class PowerStationFactoryStub extends AbstractPowerObjectFactory {

	private final TimeService timeService;
	private PowerStation powerStation;
	private long powerObjectId;
	private PowerStationParameters parameters;
	private PowerStationGenerationSchedule stationSchedule;
	private PowerCurve generationCurveGen1;
	private PowerCurve generationCurveGen2;
	private PowerCurve generationCurveGen3;
	private PowerCurve generationCurveGen4;
	private PowerCurve generationCurveGen5;
	private GeneratorGenerationSchedule genrationSchedule_1;
	private GeneratorGenerationSchedule genrationSchedule_2;
	private GeneratorGenerationSchedule genrationSchedule_3;
	private GeneratorGenerationSchedule genrationSchedule_4;
	private GeneratorGenerationSchedule genrationSchedule_5;
	private GeneratorGenerationSchedule genrationSchedule_6;

	//as factory is not complete, this are previously calculated values for creation
	private final List<Float> GENERATION_BY_HOURS_GEN_1_2 = asList(
			143f,  143f,  143f,  143f, 	143f,  143f,
			143f,  143f,  143f,  143f,  143f,  143f,
			143f,  143f,  143f,  143f,  143f,  143f,
			143f,  143f,  143f,  143f,  143f,  143f 
	);

	private final List<Float> GENERATION_BY_HOURS_GEN_3 = asList(
			143f,  143f,  143f,  120f, 	120f,  143f,
			125f,  143f,  143f,  143f,  143f,  143f,
			143f,  143f,  143f,  143f,  143f,  143f,
			143f,  143f,  143f,  143f,  143f,  135f 
	);

	private final List<Float> GENERATION_BY_HOURS_GEN_4 = asList(
			  0f,    0f,    0f,    0f, 	  0f,    0f,
			 90f,   90f,  130f,  100f,  143f,   90f,
			 90f,  120f,   90f,   90f,  140f,  143f,
			143f,  143f,  143f,  135f,  135f,   90f 
	);

	private final List<Float> GENERATION_BY_HOURS_GEN_5 = asList(
			  0f,    0f,    0f,    0f,    0f,    0f,
			  0f,    0f,   90f,   90f,  100f,   90f,
			 90f,   90f,   90f,   90f,   90f,  110f,
			120f,  100f,   90f,   90f,    0f,    0f 
	);

	private final int NOMINAL_POWER_IN_MW = 150;
	private final int MINIMAL_POWER_IN_MW_1 = 90;
	private final int MINIMAL_POWER_IN_MW_2 = 60;
	private final boolean ASTATIC_REGULATION_ON = true;
	private final boolean ASTATIC_REGULATION_OFF = false;
	private final boolean GENERATOR_ON = true;
	private final int REGULATION_SPEED_IN_MW_1 = 3;
	private final int REGULATION_SPEED_IN_MW_2 = 20;
	private final PowerCurve NULL_CURVE = null;
	
	public PowerStationFactoryStub(Simulation simulation, Dispatcher dispatcher, TimeService timeService){
		super(simulation, dispatcher);
		this.timeService = timeService;
	}

	public synchronized PowerStation createPowerStation(long powerObjectId,
			PowerStationCreationParametersStub parameters){
		
		savePowerObjectId(powerObjectId);
		createPowerStationParameters();
		createDefaultGenerationSchedule();
		createPowerStation();
		
		return powerStation;
	}
	
	private void savePowerObjectId(long powerObjectId){
		this.powerObjectId = powerObjectId;
	}
	
	private void createPowerStationParameters(){
		int generatorNumber = 0;
		parameters = new PowerStationParameters(powerObjectId);
		GeneratorParameters parameters_1 = new GeneratorParameters(generatorNumber++, NOMINAL_POWER_IN_MW, MINIMAL_POWER_IN_MW_1);
		GeneratorParameters parameters_2 = new GeneratorParameters(generatorNumber++, NOMINAL_POWER_IN_MW, MINIMAL_POWER_IN_MW_1);
		GeneratorParameters parameters_3 = new GeneratorParameters(generatorNumber++, NOMINAL_POWER_IN_MW, MINIMAL_POWER_IN_MW_1);
		GeneratorParameters parameters_4 = new GeneratorParameters(generatorNumber++, NOMINAL_POWER_IN_MW, MINIMAL_POWER_IN_MW_1);
		GeneratorParameters parameters_5 = new GeneratorParameters(generatorNumber++, NOMINAL_POWER_IN_MW, MINIMAL_POWER_IN_MW_1);
		GeneratorParameters parameters_6 = new GeneratorParameters(generatorNumber++, NOMINAL_POWER_IN_MW, MINIMAL_POWER_IN_MW_2);
		
		parameters.getGeneratorParameters().add(parameters_1);
		parameters.getGeneratorParameters().add(parameters_2);
		parameters.getGeneratorParameters().add(parameters_3);
		parameters.getGeneratorParameters().add(parameters_4);
		parameters.getGeneratorParameters().add(parameters_5);
		parameters.getGeneratorParameters().add(parameters_6);
	}
	
	private void createDefaultGenerationSchedule(){
		int generatorNumber = 1;
		stationSchedule = new PowerStationGenerationSchedule(powerObjectId);
		generationCurveGen1 = new PowerCurve(GENERATION_BY_HOURS_GEN_1_2);
		generationCurveGen2 = new PowerCurve(GENERATION_BY_HOURS_GEN_1_2);
		generationCurveGen3 = new PowerCurve(GENERATION_BY_HOURS_GEN_3);
		generationCurveGen4 = new PowerCurve(GENERATION_BY_HOURS_GEN_4);
		generationCurveGen5 = new PowerCurve(GENERATION_BY_HOURS_GEN_5);
		genrationSchedule_1 = new GeneratorGenerationSchedule(generatorNumber++, GENERATOR_ON, 
				ASTATIC_REGULATION_OFF, generationCurveGen1);
		genrationSchedule_2 = new GeneratorGenerationSchedule(generatorNumber++, GENERATOR_ON, 
				ASTATIC_REGULATION_OFF, generationCurveGen2);
		genrationSchedule_3 = new GeneratorGenerationSchedule(generatorNumber++, GENERATOR_ON, 
				ASTATIC_REGULATION_OFF, generationCurveGen3);
		genrationSchedule_4 = new GeneratorGenerationSchedule(generatorNumber++, GENERATOR_ON, 
				ASTATIC_REGULATION_OFF, generationCurveGen4);
		genrationSchedule_5 = new GeneratorGenerationSchedule(generatorNumber++, GENERATOR_ON, 
				ASTATIC_REGULATION_OFF, generationCurveGen5);
		genrationSchedule_6 = new GeneratorGenerationSchedule(generatorNumber++, GENERATOR_ON, 
				ASTATIC_REGULATION_ON, NULL_CURVE);
		stationSchedule.getGeneratorSchedules().add(genrationSchedule_1);
		stationSchedule.getGeneratorSchedules().add(genrationSchedule_2);
		stationSchedule.getGeneratorSchedules().add(genrationSchedule_3);
		stationSchedule.getGeneratorSchedules().add(genrationSchedule_4);
		stationSchedule.getGeneratorSchedules().add(genrationSchedule_5);
		stationSchedule.getGeneratorSchedules().add(genrationSchedule_6);
	}
	
	private void createPowerStation(){
		int generatorNuber = 1;
		powerStation = new PowerStation(simulation, dispatcher, parameters, createStateManager());
		Generator generator_1 = new Generator(simulation, generatorNuber++);
		Generator generator_2 = new Generator(simulation, generatorNuber++);
		Generator generator_3 = new Generator(simulation, generatorNuber++);
		Generator generator_4 = new Generator(simulation, generatorNuber++);
		Generator generator_5 = new Generator(simulation, generatorNuber++);
		Generator generator_6 = new Generator(simulation, generatorNuber++);
		
		generator_1.setMinimalPowerInMW(MINIMAL_POWER_IN_MW_1);
		generator_2.setMinimalPowerInMW(MINIMAL_POWER_IN_MW_1);
		generator_3.setMinimalPowerInMW(MINIMAL_POWER_IN_MW_1);
		generator_4.setMinimalPowerInMW(MINIMAL_POWER_IN_MW_1);
		generator_5.setMinimalPowerInMW(MINIMAL_POWER_IN_MW_1);
		generator_6.setMinimalPowerInMW(MINIMAL_POWER_IN_MW_2);
		generator_1.setNominalPowerInMW(NOMINAL_POWER_IN_MW);
		generator_2.setNominalPowerInMW(NOMINAL_POWER_IN_MW);
		generator_3.setNominalPowerInMW(NOMINAL_POWER_IN_MW);
		generator_4.setNominalPowerInMW(NOMINAL_POWER_IN_MW);
		generator_5.setNominalPowerInMW(NOMINAL_POWER_IN_MW);
		generator_6.setNominalPowerInMW(NOMINAL_POWER_IN_MW);
		generator_1.setReugulationSpeedInMWPerMinute(REGULATION_SPEED_IN_MW_1);
		generator_2.setReugulationSpeedInMWPerMinute(REGULATION_SPEED_IN_MW_1);
		generator_3.setReugulationSpeedInMWPerMinute(REGULATION_SPEED_IN_MW_1);
		generator_4.setReugulationSpeedInMWPerMinute(REGULATION_SPEED_IN_MW_1);
		generator_5.setReugulationSpeedInMWPerMinute(REGULATION_SPEED_IN_MW_1);
		generator_6.setReugulationSpeedInMWPerMinute(REGULATION_SPEED_IN_MW_2);
		
		powerStation.addGenerator(generator_1);
		powerStation.addGenerator(generator_2);
		powerStation.addGenerator(generator_3);
		powerStation.addGenerator(generator_4);
		powerStation.addGenerator(generator_5);
		powerStation.addGenerator(generator_6);

		powerStation.executeSchedule(stationSchedule);
	}

	private PowerStationStateManager createStateManager() {
		return new PowerStationStateManager(timeService, dispatcher);
	}
}