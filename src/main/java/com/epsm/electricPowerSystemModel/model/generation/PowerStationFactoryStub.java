package com.epsm.electricPowerSystemModel.model.generation;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.AbstractPowerObjectFactory;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class PowerStationFactoryStub extends AbstractPowerObjectFactory{
	private PowerStation powerStation;
	private long powerObjectId;
	private PowerStationParameters parameters;
	
	public PowerStationFactoryStub(ElectricPowerSystemSimulation simulation,
			TimeService timeService, Dispatcher dispatcher){
		
		super(simulation, timeService, dispatcher);
	}

	public synchronized PowerStation createPowerStation(long powerObjectId,
			PowerStationCreationParametersStub parameters){
		
		saveValues(powerObjectId, parameters);
		createPowerStationParameters();
		createPowerStation();
		
		return powerStation;
	}
	
	private void saveValues(long powerObjectId, PowerStationCreationParametersStub parameters){
		this.powerObjectId = powerObjectId;
	}
	
	private void createPowerStation(){
		powerStation = new PowerStation(simulation, timeService, dispatcher, parameters);
		new MainControlPanel(simulation, powerStation);
		Generator generator_1 = new Generator(simulation, 1);
		Generator generator_2 = new Generator(simulation, 2);
		
		generator_1.setMinimalPowerInMW(5);
		generator_1.setNominalPowerInMW(40);
		generator_2.setMinimalPowerInMW(25);
		generator_2.setNominalPowerInMW(100);
		powerStation.addGenerator(generator_1);
		powerStation.addGenerator(generator_2);
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
}
