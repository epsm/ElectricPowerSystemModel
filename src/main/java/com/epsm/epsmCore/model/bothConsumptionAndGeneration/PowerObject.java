package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.RealTimeOperations;
import com.epsm.epsmCore.model.generalModel.SimulationObject;
import com.epsm.epsmCore.model.generalModel.TimeService;

@AllArgsConstructor
public abstract class PowerObject implements SimulationObject, RealTimeOperations{
	protected final long id;
	protected ElectricPowerSystemSimulation simulation;
	protected TimeService timeService;
	private PowerObjectMessageManager manager;
	protected Logger logger;

	public long getId(){
		return id;
	}
	

	@Override
	public final void doRealTimeDependingOperations(){
		manager.manageMessages();
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
