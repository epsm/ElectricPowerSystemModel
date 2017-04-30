package com.epsm.epsmcore.model.common;

import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public abstract class PowerObject <T extends Parameters, E extends State>  {

	protected final long id;
	protected final T parameters;
	protected final Simulation simulation;
	protected final Dispatcher dispatcher;
	protected PowerObjectStateManager<T, E> stateManager;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public PowerObject(Simulation simulation, Dispatcher dispatcher, T parameters, PowerObjectStateManager<T, E> stateManager) {
		this.id = parameters.getPowerObjectId();
		this.parameters = parameters;
		this.simulation = simulation;
		this.dispatcher = dispatcher;
		this.stateManager = stateManager;
		stateManager.setPowerObject(this);
	}

	public long getId(){
		return parameters.getPowerObjectId();
	}

	public final void sendStatesToDispatcher(){
		stateManager.manageStates();
	}
	
	protected boolean isItExactlyMinute(LocalDateTime currentDateTime){
		return currentDateTime.getSecond() == 0 && currentDateTime.getNano() == 0;
	}
	
	public final float calculatePowerBalance(){
		float powerBalance = calculatPowerBalance();
		
		if(ifItExactlyTenMinute()){
			passStateOnCurrentTimeToMessageManager();
		}
		
		return powerBalance;
	}
	
	private boolean ifItExactlyTenMinute(){
		LocalDateTime simulationDateTime = simulation.getDateTimeInSimulation();
		boolean nanosIsZero = simulationDateTime.getNano() == 0;
		boolean secondsIsZero = simulationDateTime.getSecond() == 0;
		boolean minutesIsZero = simulationDateTime.getMinute() % 10 == 0;
		return nanosIsZero && secondsIsZero && minutesIsZero;
	}

	private void passStateOnCurrentTimeToMessageManager(){
		stateManager.acceptState(getState());
	}

	public T getParameters() {
		return parameters;
	}

	protected abstract float calculatPowerBalance();

	public abstract E getState();
}
