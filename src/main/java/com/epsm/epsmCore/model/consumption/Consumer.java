package com.epsm.epsmCore.model.consumption;

import java.time.LocalTime;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.PowerObject;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.Parameters;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.Constants;
import com.epsm.epsmCore.model.generalModel.TimeService;

public abstract class Consumer extends PowerObject{
	public Consumer(ElectricPowerSystemSimulation simulation, TimeService timeService, Dispatcher dispatcher,
			Parameters parameters) {
		super(simulation, timeService, dispatcher, parameters);
	}

	protected float degreeOnDependingOfFrequency;

	protected float calculateLoadCountingFrequency(float load, float frequency){
		return (float)Math.pow((frequency / Constants.STANDART_FREQUENCY),
				degreeOnDependingOfFrequency) * load;
	}

	public void setDegreeOfDependingOnFrequency(float degreeOnDependingOfFrequency){
		this.degreeOnDependingOfFrequency = degreeOnDependingOfFrequency;
	}

	public float getDegreeOnDependingOfFrequency() {
		return degreeOnDependingOfFrequency;
	}
	
	protected ConsumerState prepareState(LocalTime simulationTimeStamp, float load){
		return new ConsumerState(id, timeService.getCurrentTime(), simulationTimeStamp, load);
	}
}
