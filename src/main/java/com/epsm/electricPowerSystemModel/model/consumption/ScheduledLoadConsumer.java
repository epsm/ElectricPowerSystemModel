package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.State;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public final class ScheduledLoadConsumer extends Consumer{
	private LoadCurveFactory factory = new LoadCurveFactory();
	private float[] approximateLoadByHoursOnDayInPercent;
	private float maxLoadWithoutFluctuationsInMW;
	private float randomFluctuationsInPercent;
	private LoadCurve loadCurveOnDay;
	private LocalTime previousLoadRequestTime;
	private ConsumerState state;
	private LocalTime currentTime;
	private float currentLoad;
	private float currentFrequency;
	private Logger logger;
	
	public ScheduledLoadConsumer(ElectricPowerSystemSimulation simulation, TimeService timeService,
			Dispatcher dispatcher, ConsumerParametersStub parameters) {
		
		super(simulation, timeService, dispatcher, parameters);
		logger = LoggerFactory.getLogger(ScheduledLoadConsumer.class);
	}
	
	@Override
	public float calculatePowerBalance() {
		calculateCurrentLoadInMW();
		prepareState();
		
		logger.debug("sim.time: {}, freq.: {}, load:{} MW.", currentTime,
				currentFrequency,currentLoad);
		
		return -currentLoad;
	}
	
	private void calculateCurrentLoadInMW(){
		getNecessaryParametersFromPowerSystem();
		
		if(isItANewDay()){
			calculateLoadCurveOnThisDay();
		}
		
		saveRequestTime();
		getLoadFromCurve();
		countFrequency();	
	}
	
	private void getNecessaryParametersFromPowerSystem(){
		currentTime = simulation.getTimeInSimulation();
		currentFrequency = simulation.getFrequencyInPowerSystem();
	}
	
	private boolean isItANewDay(){
		return previousLoadRequestTime == null || previousLoadRequestTime.isAfter(currentTime);
	}
	
	private void calculateLoadCurveOnThisDay(){
		loadCurveOnDay = factory.getRandomCurve(approximateLoadByHoursOnDayInPercent,
				maxLoadWithoutFluctuationsInMW, randomFluctuationsInPercent);
	}

	private void saveRequestTime(){
		previousLoadRequestTime = currentTime;
	}
	
	private void getLoadFromCurve(){
		currentLoad = loadCurveOnDay.getPowerOnTimeInMW(currentTime);
	}
	
	private void countFrequency(){
		currentLoad = calculateLoadCountingFrequency(currentLoad, currentFrequency);
	}
	
	private void prepareState(){
		state = prepareState(currentTime, -currentLoad);
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
	protected void performDispatcheCommand(Command command) {
		//TODO turn off/on user by dispatcher command. 
	}
}
