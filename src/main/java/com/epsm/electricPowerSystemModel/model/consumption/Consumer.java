package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.dispatch.ConsumerState;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatcherMessage;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;
import com.epsm.electricPowerSystemModel.model.generalModel.PowerObject;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public abstract class Consumer extends PowerObject{
	protected float degreeOnDependingOfFrequency;
	
	/*public Consumer(ElectricPowerSystemSimulation simulation, TimeService timeService,
			Dispatcher dispatcher, Class<? extends DispatcherMessage>  expectedMessageType){
		
		super(simulation, timeService, dispatcher, expectedMessageType);
		this.simulation = simulation;
	}*/

	protected float calculateLoadCountingFrequency(float load, float frequency){
		return (float)Math.pow((frequency / GlobalConstants.STANDART_FREQUENCY),
				degreeOnDependingOfFrequency) * load;
	}
	
	protected ConsumerState prepareState(LocalTime timeStamp, float load){
		return new ConsumerState(id, load, timeStamp);
	}

	public void setDegreeOfDependingOnFrequency(float degreeOnDependingOfFrequency){
		this.degreeOnDependingOfFrequency = degreeOnDependingOfFrequency;
	}

	public float getDegreeOnDependingOfFrequency() {
		return degreeOnDependingOfFrequency;
	}
	
	public abstract float calculateCurrentLoadInMW();
}
