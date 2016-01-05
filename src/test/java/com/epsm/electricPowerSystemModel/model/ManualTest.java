package com.epsm.electricPowerSystemModel.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import com.epsm.electricPowerSystemModel.model.consumption.ConsumerParametersStub;
import com.epsm.electricPowerSystemModel.model.consumption.ConsumptionPermissionStub;
import com.epsm.electricPowerSystemModel.model.consumption.ScheduledLoadConsumerCreationParametersStub;
import com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumerCreationParametersStub;
import com.epsm.electricPowerSystemModel.model.control.SimulationRunner;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingObject;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;
import com.epsm.electricPowerSystemModel.model.dispatch.State;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;
import com.epsm.electricPowerSystemModel.model.generation.GeneratorGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationCreationParametersStub;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationParameters;

public class ManualTest {
	private ElectricPowerSystemSimulation simulation;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private SimulationRunner runner;
	
	private void setUp(){
		timeService = new TimeService();
		dispatcher = new DispatherImpl();
		simulation = new ElectricPowerSystemSimulationImpl(timeService, dispatcher);
		runner = new SimulationRunner();
	}
	
	private void go(){
		simulation.createPowerObject(new PowerStationCreationParametersStub());
		simulation.createPowerObject(new ScheduledLoadConsumerCreationParametersStub());
		simulation.createPowerObject(new ShockLoadConsumerCreationParametersStub());
		
		runner.runSimulation(simulation);
	}
	
	private class DispatherImpl implements Dispatcher{
		private boolean firstTime = true;
		private Map<Long, DispatchingObject> objects;
		private PowerStationGenerationSchedule stationSchedule;
		private GeneratorGenerationSchedule generatorSchedule;
		private ConsumptionPermissionStub permision;
		
		@Override
		public void establishConnection(Parameters parameters) {
			System.out.println(parameters);
			
			if(firstTime){
				gerObjectsMap();
			}
			
			DispatchingObject object = objects.get(parameters.getPowerObjectId());
			
			if(parameters instanceof PowerStationParameters){
				prepareWrongSchedule(parameters);
				object.executeCommand(stationSchedule);
			}else if(parameters instanceof ConsumerParametersStub){
				preparePermission(parameters);
				object.executeCommand(permision);
			}
		}
		
		private void prepareWrongSchedule(Parameters parameters){
			stationSchedule = new PowerStationGenerationSchedule(parameters.getPowerObjectId(),
					LocalDateTime.MIN, LocalTime.MIN, 1);
			generatorSchedule = new GeneratorGenerationSchedule(8, false, false, null); 
			stationSchedule.addGeneratorSchedule(generatorSchedule);
		}
		
		private void preparePermission(Parameters parameters){
			long objectId = parameters.getPowerObjectId();
			permision = new ConsumptionPermissionStub(objectId, LocalDateTime.now(), LocalTime.MIN);
		}
		
		private void gerObjectsMap(){
			objects = simulation.getDispatchingObjects();
		}

		@Override
		public void acceptState(State state) {
			System.out.println(state);
		}
	}
	
	public static void main(String[] args) {
		ManualTest test = new ManualTest();
		test.setUp();
		test.go();
	}
}
