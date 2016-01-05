package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.AbstractPowerObjectFactory;
import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.PowerObject;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class ShockLoadConsumerFactoryStub extends AbstractPowerObjectFactory{
	private ShockLoadConsumer consumer;
	private long powerObjectId;
	private ConsumerParametersStub parameters;
	
	public ShockLoadConsumerFactoryStub(ElectricPowerSystemSimulation simulation,
			TimeService timeService, Dispatcher dispatcher) {
		
		super(simulation, timeService, dispatcher);
	}

	public synchronized PowerObject createConsumer(long id,
			ShockLoadConsumerCreationParametersStub parameters) {
		
		saveValues(id);
		createShockLoadConsumerParameters();
		createShockLoadConsumer();
	
		return consumer;
	}
	
	private void saveValues(long powerObjectId){
		this.powerObjectId = powerObjectId;
	}
	
	private void createShockLoadConsumerParameters(){
		LocalDateTime realTimeStamp = timeService.getCurrentTime();
		LocalTime simulationTimeStamp = simulation.getTimeInSimulation();
		
		parameters = new ConsumerParametersStub(powerObjectId, realTimeStamp, simulationTimeStamp);
	}
	
	private void createShockLoadConsumer(){
		consumer = new ShockLoadConsumer(simulation, timeService, dispatcher, parameters);
		consumer.setDegreeOfDependingOnFrequency(2);
		consumer.setMaxLoad(10f);
		consumer.setMaxWorkDurationInSeconds(300);
		consumer.setMaxPauseBetweenWorkInSeconds(200);
	}
}
