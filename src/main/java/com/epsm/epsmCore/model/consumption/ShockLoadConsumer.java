package com.epsm.epsmCore.model.consumption;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.dispatch.Command;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.dispatch.State;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.TimeService;

public final class ShockLoadConsumer extends Consumer{
	private int maxWorkDurationInSeconds;
	private int maxPauseBetweenWorkInSeconds;
	private float maxLoad;
	private LocalDateTime dateTimeToTurnOn;
	private LocalDateTime dateTimeToTurnOff;
	private boolean isTurnedOn;
	private LocalDateTime currentDateTime;
	private float plannedLoad;
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
	protected float calculateCurrentPowerBalance() {
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
		float halfOfMaxLoad = maxLoad / 2;
		plannedLoad = halfOfMaxLoad + halfOfMaxLoad * random.nextFloat();
		isTurnedOn = true;
	}
	
	private void setDateTimeToTurnOff(){
		float halfOfTurnedOnDuration = maxWorkDurationInSeconds / 2; 
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
		float halfOfTurnedOffDuration = maxPauseBetweenWorkInSeconds / 2; 
		dateTimeToTurnOn = currentDateTime.plusSeconds(
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
	protected void performDispatcherCommand(Command command) {
		//TODO turn off/on user by dispatcher command. 
	}
}
