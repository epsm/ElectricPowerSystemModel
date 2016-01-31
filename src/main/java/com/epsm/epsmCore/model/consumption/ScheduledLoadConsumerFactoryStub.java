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
	public final float[] LOAD_BY_HOURS = new float[]{
			50f,  53f,  56f,  59f, 	61f,  65f,
			68f,  71f,  74f,  76f,  75f,  74f,
			74f,  75f,  74f,  76f,  80f,  79f,
			77f,  74f,  73f,  59f,  64f,  50f 
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
		consumer.setApproximateLoadByHoursOnDayInPercent(LOAD_BY_HOURS);
		consumer.setMaxLoadWithoutRandomInMW(80);
		consumer.setRandomFluctuationsInPercent(9);
	}
}
