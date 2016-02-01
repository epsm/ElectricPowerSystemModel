package com.epsm.epsmCore.model.consumption;

import java.time.LocalDateTime;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.AbstractPowerObjectFactory;
import com.epsm.epsmCore.model.bothConsumptionAndGeneration.PowerObject;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.TimeService;

public class ScheduledLoadConsumerFactoryStub extends AbstractPowerObjectFactory{
	private ScheduledLoadConsumer consumer;
	private long powerObjectId;
	private ConsumerParametersStub parameters;
	public final float[] LOAD_BY_HOURS_IN_PERCENT = new float[]{
			 69f,  68f,  66f,  65f,	 62f,  65f,
			 72f,  83f,  91f,  96f,  95f,  93f,
			 91f,  92f,  92f,  93f,  94f,  98f,
			100f,  98f,  95f,  93f,  88f,  75f 
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
		LocalDateTime realTimeStamp = timeService.getCurrentDateTime();
		LocalDateTime simulationTimeStamp = simulation.getDateTimeInSimulation();
		
		parameters = new ConsumerParametersStub(powerObjectId, realTimeStamp, simulationTimeStamp);
	}
	
	private void createScheduledLoadConsumer(){
		consumer = new ScheduledLoadConsumer(simulation, timeService, dispatcher, parameters);
		consumer.setDegreeOfDependingOnFrequency(2);
		consumer.setApproximateLoadByHoursOnDayInPercent(LOAD_BY_HOURS_IN_PERCENT);
		consumer.setMaxLoadWithoutRandomInMW(95);
		consumer.setRandomFluctuationsInPercent(5);
	}
}