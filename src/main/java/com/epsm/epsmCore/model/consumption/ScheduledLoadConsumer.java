package com.epsm.epsmCore.model.consumption;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.epsmCore.model.dispatch.Command;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.State;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.TimeService;

public final class ScheduledLoadConsumer extends Consumer{
	private LoadCurveFactory factory = new LoadCurveFactory();
	private float[] approximateLoadByHoursOnDayInPercent;
	private float maxLoadWithoutFluctuationsInMW;
	private float randomFluctuationsInPercent;
	private LoadCurve loadCurveOnDay;//There must be LoadCurves at least for every day of week to emulate real behavior.
	private LocalDateTime previousLoadRequestDateTime;
	private ConsumerState state;
	private LocalDateTime currentDateTime;
	private float currentLoad;
	private float currentFrequency;
	private Logger logger;
	
	public ScheduledLoadConsumer(ElectricPowerSystemSimulation simulation, TimeService timeService,
			Dispatcher dispatcher, ConsumerParametersStub parameters) {
		
		super(simulation, timeService, dispatcher, parameters);
		logger = LoggerFactory.getLogger(ScheduledLoadConsumer.class);
	}
	
	@Override
	protected float calculateCurrentPowerBalance() {
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
		loadCurveOnDay = factory.getRandomCurve(approximateLoadByHoursOnDayInPercent,
				maxLoadWithoutFluctuationsInMW, randomFluctuationsInPercent);
	}

	private void saveRequestDateTime(){
		previousLoadRequestDateTime = currentDateTime;
	}
	
	private void getLoadFromCurve(){
		currentLoad = loadCurveOnDay.getPowerOnTimeInMW(currentDateTime.toLocalTime());
	}
	
	private void countFrequency(){
		currentLoad = calculateLoadCountingFrequency(currentLoad, currentFrequency);
	}
	
	private void prepareState(){
		state = prepareState(currentDateTime, currentLoad);
	}
	
	@Override
	public State getState() {
		return state;
	}
	
	public void setRandomFluctuationsInPercent(float randomFluctuationsInPercent) {
		this.randomFluctuationsInPercent = randomFluctuationsInPercent;
	}

	public float getRandomFluctuationsInPercent() {
		return randomFluctuationsInPercent;
	}

	public void setApproximateLoadByHoursOnDayInPercent(float[] approximateLoadByHoursOnDayInPercent) {
		this.approximateLoadByHoursOnDayInPercent = approximateLoadByHoursOnDayInPercent;
	}

	public void setMaxLoadWithoutRandomInMW(float maxLoadWithoutRandomInMW) {
		this.maxLoadWithoutFluctuationsInMW = maxLoadWithoutRandomInMW;
	}

	public float getMaxLoadWithoutRandomInMW() {
		return maxLoadWithoutFluctuationsInMW;
	}

	@Override
	protected void performDispatcherCommand(Command command) {
		//TODO turn off/on user by dispatcher command. 
	}
}
