package com.epsm.epsmCore.model.consumption;

import java.time.LocalDateTime;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.AbstractPowerObjectFactory;
import com.epsm.epsmCore.model.bothConsumptionAndGeneration.PowerObject;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.TimeService;

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
		LocalDateTime realTimeStamp = timeService.getCurrentDateTime();
		LocalDateTime simulationTimeStamp = simulation.getDateTimeInSimulation();
		
		parameters = new ConsumerParametersStub(powerObjectId, realTimeStamp, simulationTimeStamp);
	}
	
	private void createShockLoadConsumer(){
		consumer = new ShockLoadConsumer(simulation, timeService, dispatcher, parameters);
		consumer.setDegreeOfDependingOnFrequency(2);
		consumer.setMaxLoad(5);
		consumer.setMaxWorkDurationInSeconds(60 * 60 * 2);
		consumer.setMaxPauseBetweenWorkInSeconds(60 * 60 * 2);
	}
}