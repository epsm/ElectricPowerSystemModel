package com.epsm.electricPowerSystemModel.model.generalModel;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.PowerObject;
import com.epsm.electricPowerSystemModel.model.consumption.ScheduledLoadConsumerCreationParametersStub;
import com.epsm.electricPowerSystemModel.model.consumption.ScheduledLoadConsumerFactoryStub;
import com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumerCreationParametersStub;
import com.epsm.electricPowerSystemModel.model.consumption.ShockLoadConsumerFactoryStub;
import com.epsm.electricPowerSystemModel.model.dispatch.CreationParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationCreationParametersStub;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationFactoryStub;

public class PowerObjectFactory {
	private Map<Long, PowerObject> powerSystemObjects;
	private PowerStationFactoryStub powerStationFactory;
	private ShockLoadConsumerFactoryStub shockConsumerFactory;
	private ScheduledLoadConsumerFactoryStub scheduledConsumerFactory;
	private AtomicLong idSource;
	
	public PowerObjectFactory(Map<Long, PowerObject> powerSystemObjects,
			ElectricPowerSystemSimulation simulation, TimeService timeService,
			Dispatcher dispatcher) {
		
		if(powerSystemObjects == null){
			String message = "PowerObjectFactory constructor: powerSystemObjects can't be null.";
			throw new IllegalArgumentException(message);
		}else if(simulation == null){
			String message = "PowerObjectFactory constructor: simulation can't be null.";
			throw new IllegalArgumentException(message);
		}else if(timeService == null){
			String message = "PowerObjectFactory constructor: timeService can't be null.";
			throw new IllegalArgumentException(message);
		}else if(dispatcher == null){
			String message = "PowerObjectFactory constructor: dispatcher can't be null.";
			throw new IllegalArgumentException(message);
		}
		
		this.powerSystemObjects = powerSystemObjects;
		powerStationFactory = new PowerStationFactoryStub(
				simulation, timeService, dispatcher);
		shockConsumerFactory = new ShockLoadConsumerFactoryStub(
				simulation, timeService, dispatcher);
		scheduledConsumerFactory = new ScheduledLoadConsumerFactoryStub(
				simulation, timeService, dispatcher);
		idSource = new AtomicLong();
	}

	public void create(CreationParameters parameters){
		if(parameters instanceof PowerStationCreationParametersStub){
			PowerObject object = powerStationFactory.createPowerStation(getId(),
					(PowerStationCreationParametersStub) parameters);
			addPowerObjectToSimulation(object);
		}else if (parameters instanceof ShockLoadConsumerCreationParametersStub){
			PowerObject object = shockConsumerFactory.createConsumer(getId(),
					(ShockLoadConsumerCreationParametersStub) parameters);
			addPowerObjectToSimulation(object);
		}else if (parameters instanceof ScheduledLoadConsumerCreationParametersStub){
			PowerObject object = scheduledConsumerFactory.createConsumer(getId(),
					(ScheduledLoadConsumerCreationParametersStub) parameters);
			addPowerObjectToSimulation(object);
		}else{
			String message = String.format("PowerObjectFactory: %s is unsupported.", 
					parameters.getClass().getSimpleName());
			throw new IllegalArgumentException(message);
		}
	}
	
	private long getId(){
		return idSource.getAndIncrement();
	}
	
	private void addPowerObjectToSimulation(PowerObject object){
		long objectId = object.getId();
		powerSystemObjects.put(objectId, object);
	}
}
