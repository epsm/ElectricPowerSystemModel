package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.PowerCurve;
import com.epsm.epsmcore.model.common.PowerCurveProcessor;
import com.epsm.epsmcore.model.common.PowerObjectStateManager;
import com.epsm.epsmcore.model.common.State;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;

import java.time.LocalDateTime;

public final class ScheduledLoadConsumer extends Consumer<ScheduledLoadConsumerParameters, ConsumerState> {

	private ScheduledLoadConsumerParameters parameters;
	private LoadCurveFactory loadCurveFactory = new LoadCurveFactory();
	private PowerCurveProcessor powerCurveProcessor = new PowerCurveProcessor();
	private PowerCurve powerCurveOnDay;//There must be PoverCurves at least for every day of week to emulate real behavior.
	private LocalDateTime previousLoadRequestDateTime;
	private ConsumerState state;
	private LocalDateTime currentDateTime;
	private float currentLoad;
	private float currentFrequency;

	public ScheduledLoadConsumer(
			Simulation simulation,
	        Dispatcher dispatcher,
			ScheduledLoadConsumerParameters parameters,
			PowerObjectStateManager stateManager) {
		
		super(simulation, dispatcher, parameters, stateManager);

		this.parameters = parameters;
	}
	
	@Override
	protected float calculateConsumerPowerBalance() {
		calculateCurrentLoadInMW();
		prepareState();
		
		if(isItExactlyMinute(currentDateTime)){
			logger.debug("State: con.#{}, sim.time: {}, freq.: {}, load:{} MW.", id, currentDateTime,
					currentFrequency,currentLoad);
		}
		
		return -currentLoad;
	}
	
	private void calculateCurrentLoadInMW(){
		getNecessaryParametersFromPowerSystem();
		
		if(isItANewDay()){
			calculateLoadCurveOnThisDay();
		}
		
		saveRequestDateTime();
		getLoadFromCurve();
		countFrequency();	
	}
	
	private void getNecessaryParametersFromPowerSystem(){
		currentDateTime = simulation.getDateTimeInSimulation();
		currentFrequency = simulation.getFrequencyInPowerSystem();
	}
	
	private boolean isItANewDay(){
		return previousLoadRequestDateTime == null 
				|| previousLoadRequestDateTime.getDayOfMonth() != currentDateTime.getDayOfMonth();
	}
	
	private void calculateLoadCurveOnThisDay(){
		powerCurveOnDay = loadCurveFactory.getRandomCurve(parameters.getApproximateLoadByHoursOnDayInPercent(),
				parameters.getMaxLoadWithoutFluctuationsInMW(), parameters.getRandomFluctuationsInPercent());
	}

	private void saveRequestDateTime(){
		previousLoadRequestDateTime = currentDateTime;
	}
	
	private void getLoadFromCurve(){
		currentLoad = powerCurveProcessor.getPowerOnTimeInMW(powerCurveOnDay, currentDateTime.toLocalTime());
	}
	
	private void countFrequency(){
		currentLoad = calculateLoadCountingFrequency(currentLoad, currentFrequency);
	}
	
	private void prepareState(){
		state = prepareState(currentDateTime, currentLoad);
	}

	@Override
	public ConsumerState getState() {
		return state;
	}
}
