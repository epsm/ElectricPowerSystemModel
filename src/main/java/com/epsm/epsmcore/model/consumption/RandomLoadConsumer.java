package com.epsm.epsmcore.model.consumption;

import com.epsm.epsmcore.model.common.PowerObjectStateManager;
import com.epsm.epsmcore.model.common.State;
import com.epsm.epsmcore.model.dispatch.Dispatcher;
import com.epsm.epsmcore.model.simulation.Simulation;

import java.time.LocalDateTime;
import java.util.Random;

public class RandomLoadConsumer extends Consumer<RandomLoadConsumerParameters, ConsumerState> {

	private LocalDateTime dateTimeToTurnOn;
	private LocalDateTime dateTimeToTurnOff;
	private boolean isTurnedOn;
	private LocalDateTime currentDateTime;
	private float plannedLoad;
	private float currentLoad;
	private float currentFrequency;
	private volatile ConsumerState state;
	private Random random = new Random();

	public RandomLoadConsumer(
			Simulation simulation,
	        Dispatcher dispatcher,
			RandomLoadConsumerParameters parameters,
			PowerObjectStateManager stateManager) {
		
		super(simulation, dispatcher, parameters, stateManager);
	}
	
	@Override
	protected float calculateConsumerPowerBalance() {

		calculateCurrentLoadInMW();
		prepareState();

		if(isItExactlyMinute(currentDateTime)){
			logger.debug("State: con.#{}, sim.time: {}, freq.: {}, cur.load:{} MW, timeToTurnOn: {},"
					+ "timeToTurnOff: {}, turnedOn: {}.", id, currentDateTime, currentFrequency,
					currentLoad, dateTimeToTurnOn, dateTimeToTurnOff, isTurnedOn);
		}
		
		return -currentLoad;
	}
	
	private void  calculateCurrentLoadInMW(){
		getNecessaryParametersFromPowerSystem();

		if(isTurnedOn){
			if(IsItTimeToTurnOff()){
				turnOffAndSetDateTimeToTurnOn();
			}
		}else{
			if(IsItTimeToTurnOn()){
				turnOnAndSetDateTimeToTurnOff();
			}
		}
		
		calculateLoad();
	}

	private void prepareState(){
		state = prepareState(currentDateTime, currentLoad);
	}
	
	private void getNecessaryParametersFromPowerSystem(){
		currentDateTime = simulation.getDateTimeInSimulation();
		currentFrequency = simulation.getFrequencyInPowerSystem();
	}
	
	private boolean IsItTimeToTurnOn(){
		if(isItFirstTurnOn()){
			setDateTimeToTurnOn();
			return false;
		}else{
			return dateTimeToTurnOn.isBefore(currentDateTime);
		}
	}
	
	private boolean isItFirstTurnOn(){
		return dateTimeToTurnOn == null;
	}
	
	private void turnOnAndSetDateTimeToTurnOff(){
		turnOnWithRandomLoadValue();
		setDateTimeToTurnOff();
	}
	
	private void turnOnWithRandomLoadValue(){
		float halfOfMaxLoad = parameters.getMaxLoad() / 2;
		plannedLoad = halfOfMaxLoad + halfOfMaxLoad * random.nextFloat();
		isTurnedOn = true;
	}
	
	private void setDateTimeToTurnOff(){
		float halfOfTurnedOnDuration = parameters.getMaxWorkDurationInSeconds() / 2;
		dateTimeToTurnOff = currentDateTime.plusSeconds(
				(long)(halfOfTurnedOnDuration + halfOfTurnedOnDuration * random.nextFloat()));
	}
	
	private boolean IsItTimeToTurnOff(){
		return dateTimeToTurnOff.isBefore(currentDateTime);
	}
	
	private void turnOffAndSetDateTimeToTurnOn(){
		turnOff();
		setDateTimeToTurnOn();
	}
	
	private void calculateLoad(){
		if(plannedLoad != 0){
			currentLoad = calculateLoadCountingFrequency(plannedLoad, currentFrequency);
		}else{
			currentLoad = 0;
		}
	}
	
	private void turnOff(){
		plannedLoad = 0;
		isTurnedOn = false;
	}
	
	private void setDateTimeToTurnOn(){
		float halfOfTurnedOffDuration = parameters.getMaxWorkDurationInSeconds() / 2;
		dateTimeToTurnOn = currentDateTime.plusSeconds(
				(long)(halfOfTurnedOffDuration + halfOfTurnedOffDuration * random.nextFloat()));
	}

	@Override
	public ConsumerState getState() {
		return state;
	}
}
