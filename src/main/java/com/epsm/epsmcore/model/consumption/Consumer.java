package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.PowerObject;
import com.epsm.epsmcore.model.common.PowerObjectStateManager;
import com.epsm.epsmcore.model.dispatch.DispatchedConsumer;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Constants;
import com.epsm.epsmcore.model.simulation.Simulation;

import java.time.LocalDateTime;

public abstract class Consumer <T extends ConsumerParameters, E extends ConsumerState> extends PowerObject<T, E> implements DispatchedConsumer {

	protected boolean active = true;

	public Consumer(
			Simulation simulation,
			Dispatcher dispatcher,
			T parameters,
			PowerObjectStateManager stateManager) {

		super(simulation, dispatcher, parameters, stateManager);
	}

	protected float calculateLoadCountingFrequency(float load, float frequency){
		return (float)Math.pow((frequency / Constants.STANDART_FREQUENCY),
				parameters.getDegreeOnDependingOfFrequency()) * load;
	}

	protected ConsumerState prepareState(LocalDateTime simulationTimeStamp, float load, ConsumerType consumerType){
		return new ConsumerState(id, simulationTimeStamp, load, consumerType);
	}

	@Override
	protected final float calculatPowerBalance() {
		return active ? calculateConsumerPowerBalance() : 0;
	}

	@Override
	public void processPermissions(ConsumerPermission permission) {
		this.active = permission.isActive();
	}

	protected abstract float calculateConsumerPowerBalance();
}
