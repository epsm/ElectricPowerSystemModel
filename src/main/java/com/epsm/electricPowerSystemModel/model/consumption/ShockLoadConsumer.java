package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.State;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public final class ShockLoadConsumer extends Consumer{
	private int maxWorkDurationInSeconds;
	private int maxPauseBetweenWorkInSeconds;
	private float maxLoad;
	private LocalTime timeToTurnOn;
	private LocalTime timeToTurnOff;
	private boolean isTurnedOn;
	private LocalTime currentTime;
	private float currentLoad;
	private float currentFrequency;
	private volatile ConsumerState state;
	private Random random = new Random();
	private Logger logger;
	
	public ShockLoadConsumer(ElectricPowerSystemSimulation simulation, TimeService timeService,
			Dispatcher dispatcher,	ConsumerParametersStub parameters) {
		
		super(simulation, timeService, dispatcher, parameters);
		logger = LoggerFactory.getLogger(ShockLoadConsumer.class);
	}
	
	@Override
	public float calculatePowerBalance() {
		calculateCurrentLoadInMW();
		prepareState();
		
		logger.debug("sim.time: {}, freq.: {}, cur.load:{} MW, timeToTurnOn: {},"
				+ "timeToTurnOff: {}, turnedOn: {}.", currentTime, currentFrequency,
				currentLoad, timeToTurnOn, timeToTurnOff, isTurnedOn);
		
		return -currentLoad;
	}
	
	private void  calculateCurrentLoadInMW(){
		getNecessaryParametersFromPowerSystem();

		if(isTurnedOn){
			if(IsItTimeToTurnOff()){
				turnOffAndSetTimeToTurnOn();
			}
		}else{
			if(IsItTimeToTurnOn()){
				turnOnAndSetTimeToTurnOff();
			}
		}
		
		if(currentLoad != 0){
			currentLoad = calculateLoadCountingFrequency(currentLoad, currentFrequency);
		}
	}

	private void prepareState(){
		state = prepareState(currentTime, -currentLoad);
	}
	
	private void getNecessaryParametersFromPowerSystem(){
		currentTime = simulation.getTimeInSimulation();
		currentFrequency = simulation.getFrequencyInPowerSystem();
	}
	
	private boolean IsItTimeToTurnOn(){
		if(isItFirstTurnOn()){
			setTimeToTurnOn();
			return false;
		}else{
			return timeToTurnOn.isBefore(currentTime);
		}
	}
	
	private boolean isItFirstTurnOn(){
		return timeToTurnOn == null;
	}
	
	private void turnOnAndSetTimeToTurnOff(){
		turnOnWithRandomLoadValue();
		setTimeToTurnOff();
	}
	
	private void turnOnWithRandomLoadValue(){
		float halfOfMaxLoad = maxLoad / 2;
		currentLoad = halfOfMaxLoad + halfOfMaxLoad * random.nextFloat();
		isTurnedOn = true;
	}
	
	private void setTimeToTurnOff(){
		float halfOfTurnedOnDuration = maxWorkDurationInSeconds / 2; 
		timeToTurnOff = currentTime.plusSeconds(
				(long)(halfOfTurnedOnDuration + halfOfTurnedOnDuration * random.nextFloat()));
	}
	
	private boolean IsItTimeToTurnOff(){
		return timeToTurnOff.isBefore(currentTime);
	}
	
	private void turnOffAndSetTimeToTurnOn(){
		turnOff();
		setTimeToTurnOn();
	}
	
	private void turnOff(){
		currentLoad = 0;
		isTurnedOn = false;
	}
	
	private void setTimeToTurnOn(){
		float halfOfTurnedOffDuration = maxPauseBetweenWorkInSeconds / 2; 
		timeToTurnOn = currentTime.plusSeconds(
				(long)(halfOfTurnedOffDuration + halfOfTurnedOffDuration * random.nextFloat()));
	}
	
	@Override
	public State getState() {
		return state;
	}
	
	public void setMaxWorkDurationInSeconds(int WorkDurationInSeconds) {
		this.maxWorkDurationInSeconds = WorkDurationInSeconds;
	}

	public void setMaxPauseBetweenWorkInSeconds(int durationBetweenWorkInSeconds) {
		this.maxPauseBetweenWorkInSeconds = durationBetweenWorkInSeconds;
	}
		
	public void setMaxLoad(float maxLoad) {
		this.maxLoad = maxLoad;
	}

	@Override
	protected void performDispatcheCommand(Command command) {
		//TODO turn off/on user by dispatcher command. 
	}
}
