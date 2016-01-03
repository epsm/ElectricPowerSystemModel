package com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalTime;
import java.util.Random;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.Message;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class ShockLoadConsumer extends Consumer{
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
	
	public ShockLoadConsumer(ElectricPowerSystemSimulation simulation, TimeService timeService, Dispatcher dispatcher) {
		super(simulation, timeService, dispatcher);
	}
	
	@Override
	public float calculateCurrentLoadInMW(){
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
		
		state = prepareState(currentTime, currentLoad);
		
		return currentLoad;
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
	public Message getState() {
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
	public void executeCommand(Message message) {
		//TODO turn off/on user by dispatcher command. 
	}

	@Override
	public Message getParameters() {
		return new ConsumerParameters(id, timeService.getCurrentTime(), currentTime);
	}
}
