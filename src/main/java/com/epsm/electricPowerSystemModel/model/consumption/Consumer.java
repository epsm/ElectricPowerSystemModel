package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.dispatch.ConsumerState;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.ObjectToBeDispatching;
import com.epsm.electricPowerSystemModel.model.dispatch.StateSender;
import com.epsm.electricPowerSystemModel.model.dispatch.StateSenderSource;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;

public abstract class Consumer implements ObjectToBeDispatching, StateSenderSource{
	protected int number;
	protected ElectricPowerSystemSimulation simulation;
	protected float degreeOnDependingOfFrequency;
	protected StateSender sender;
	
	public Consumer(int consumerNumber, ElectricPowerSystemSimulation simulation){
		this.number = consumerNumber;
		this.simulation = simulation;
		
		if(simulation == null){
			throw new ConsumptionException("Consumer: simulation must not be null.");
		}
	}

	protected float calculateLoadCountingFrequency(float load, float frequency){
		return (float)Math.pow((frequency / GlobalConstatnts.STANDART_FREQUENCY),
				degreeOnDependingOfFrequency) * load;
	}
	
	protected ConsumerState prepareState(LocalTime timeStamp, float load){
		return new ConsumerState(number, load, timeStamp);
	}
	
	@Override
	public void sendReports(){
		sender.sendReports();
	}
	
	@Override
	public void setStateSender(StateSender sender) {
		this.sender = sender;
	}

	@Override
	public final void registerWithDispatcher(Dispatcher dispatcher){
		dispatcher.connectToPowerObject(this);
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
