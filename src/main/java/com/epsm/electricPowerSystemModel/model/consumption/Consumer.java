package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.dispatch.ConsumerState;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatcherMessage;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerSystemObject;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public abstract class Consumer extends PowerSystemObject{
	protected int number;
	protected ElectricPowerSystemSimulation simulation;
	protected float degreeOnDependingOfFrequency;
	
	public Consumer(TimeService timeService, Dispatcher dispatcher, 
			Class<? extends DispatcherMessage>  expectedMessageType,
			String childNameForLogging, int consumerNumber,
			ElectricPowerSystemSimulation simulation){
		
		super(timeService, dispatcher, expectedMessageType, childNameForLogging);
		this.number = consumerNumber;
		this.simulation = simulation;
		
		if(simulation == null){
			throw new ConsumptionException("Consumer: simulation must not be null.");
		}
	}

	protected float calculateLoadCountingFrequency(float load, float frequency){
		return (float)Math.pow((frequency / GlobalConstants.STANDART_FREQUENCY),
				degreeOnDependingOfFrequency) * load;
	}
	
	protected ConsumerState prepareState(LocalTime timeStamp, float load){
		return new ConsumerState(number, load, timeStamp);
	}
	
	public int getConsumerNumber() {
		return number;
	}

	public void setDegreeOfDependingOnFrequency(float degreeOnDependingOfFrequency){
		this.degreeOnDependingOfFrequency = degreeOnDependingOfFrequency;
	}

	public float getDegreeOnDependingOfFrequency() {
		return degreeOnDependingOfFrequency;
	}
	
	public abstract float calculateCurrentLoadInMW();
}
