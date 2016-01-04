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
		
		this.powerSystemObjects = powerSystemObjects;
		this.powerStationFactory = new PowerStationFactoryStub(
				simulation, timeService, dispatcher);
		this.shockConsumerFactory = new ShockLoadConsumerFactoryStub(
				simulation, timeService, dispatcher);
		this.scheduledConsumerFactory = new ScheduledLoadConsumerFactoryStub(
				simulation, timeService, dispatcher);
		idSource = new AtomicLong();
	}

	public void build(CreationParameters parameters){
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
			String message = String.format("PowerObjectFactory: {} is unsupported.", 
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
