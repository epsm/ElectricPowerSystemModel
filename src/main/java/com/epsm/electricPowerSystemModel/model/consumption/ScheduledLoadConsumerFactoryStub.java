package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.AbstractPowerObjectFactory;
import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.PowerObject;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class ScheduledLoadConsumerFactoryStub extends AbstractPowerObjectFactory{
	private ScheduledLoadConsumer consumer;
	private long powerObjectId;
	private ConsumerParametersStub parameters;
	public final float[] LOAD_BY_HOURS = new float[]{
			64.88f,  59.54f,  55.72f,  51.90f, 	48.47f,  48.85f,
			48.09f,  57.25f,  76.35f,  91.60f,  100.0f,  99.23f,
			91.60f,  91.60f,  91.22f,  90.83f,  90.83f,  90.83f,
			90.83f,  90.83f,  90.83f,  90.83f,  90.83f,  83.96f 
	};
	
	public ScheduledLoadConsumerFactoryStub(ElectricPowerSystemSimulation simulation,
			TimeService timeService, Dispatcher dispatcher) {
		
		super(simulation, timeService, dispatcher);
	}
	
	public synchronized PowerObject createConsumer(long id,
			ScheduledLoadConsumerCreationParametersStub parameters) {
		
		saveValues(id);
		createScheduledLadConsumerParameters();
		createScheduledLoadConsumer();
	
		return consumer;
	}
	
	private void saveValues(long powerObjectId){
		this.powerObjectId = powerObjectId;
	}
	
	private void createScheduledLadConsumerParameters(){
		LocalDateTime realTimeStamp = timeService.getCurrentTime();
		LocalTime simulationTimeStamp = simulation.getTimeInSimulation();
		
		parameters = new ConsumerParametersStub(powerObjectId, realTimeStamp, simulationTimeStamp);
	}
	
	private void createScheduledLoadConsumer(){
		consumer = new ScheduledLoadConsumer(simulation, timeService, dispatcher, parameters);
		consumer.setDegreeOfDependingOnFrequency(2);
		consumer.setApproximateLoadByHoursOnDayInPercent(LOAD_BY_HOURS);
		consumer.setMaxLoadWithoutRandomInMW(100);
		consumer.setRandomFluctuationsInPercent(10);
	}
}
