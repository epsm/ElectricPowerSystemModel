package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.dispatch.Command;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.Parameters;
import com.epsm.epsmCore.model.dispatch.State;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.RealTimeOperations;
import com.epsm.epsmCore.model.generalModel.SimulationObject;
import com.epsm.epsmCore.model.generalModel.TimeService;

public abstract class PowerObject implements SimulationObject, RealTimeOperations{
	protected final long id;
	protected Parameters parameters;
	protected ElectricPowerSystemSimulation simulation;
	protected TimeService timeService;
	private PowerObjectMessageManager manager;
	protected Logger logger;

	public PowerObject(ElectricPowerSystemSimulation simulation, TimeService timeService,
			Dispatcher dispatcher, Parameters parameters) {
		
		logger = LoggerFactory.getLogger(PowerObject.class);
		
		if(simulation == null){
			String message = "PowerObject constructor: simulation can't be null.";
			logger.error(message);
			throw new IllegalArgumentException(message);
		}else if(timeService == null){
			String message = "PowerObject constructor: timeService can't be null.";
			logger.error(message);
			throw new IllegalArgumentException(message);
		}else if(dispatcher == null){
			String message = "PowerObject constructor: dispatcher can't be null.";
			logger.error(message);
			throw new IllegalArgumentException(message);
		}else if(parameters == null){
			String message = "PowerObject constructor: parameters can't be null.";
			logger.error(message);
			throw new IllegalArgumentException(message);
		}
		
		this.simulation = simulation;
		this.timeService = timeService;
		this.parameters = parameters;
		id = parameters.getPowerObjectId();
		manager = new PowerObjectMessageManager(timeService, dispatcher, this);
		
		logger.info("{} was created.", this);
	}

	public long getId(){
		return id;
	}
	
	public final Parameters getParameters(){
		return parameters;
	}
	
	@Override
	public final void doRealTimeDependingOperations(){
		manager.manageMessages();
	}
	
	@Override
	public final void executeCommand(Command command){
		manager.verifyCommand(command);
	}
	
	protected abstract void performDispatcherCommand(Command command);
	
	public abstract State getState();
	
	@Override
	public String toString() {
		return String.format("%s#%d", getClass().getSimpleName(), id);
	}
	
	protected boolean isItExactlyMinute(LocalDateTime currentDateTime){
		return currentDateTime.getSecond() == 0 && currentDateTime.getNano() == 0;
	}
	
	@Override
	public final float calculatePowerBalance(){
		float powerBalance = calculateCurrentPowerBalance();
		
		if(ifItExactlyTenMinute()){
			passStateOnCurrentTimeToMessageManager();
		}
		
		return powerBalance;
	}
	
	protected abstract float calculateCurrentPowerBalance();
	
	private boolean ifItExactlyTenMinute(){
		LocalDateTime simulationDateTime = simulation.getDateTimeInSimulation();
		boolean nanosIsZero = simulationDateTime.getNano() == 0;
		boolean secondsIsZero = simulationDateTime.getSecond() == 0;
		boolean minutesIsZero = simulationDateTime.getMinute() % 10 == 0;
		return nanosIsZero && secondsIsZero && minutesIsZero;
	}
	
	private void passStateOnCurrentTimeToMessageManager(){
		manager.acceptState(getState());
	}
}
