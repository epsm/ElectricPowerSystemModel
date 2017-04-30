package com.epsm.epsmcore.model.simulation;

import com.epsm.epsmcore.model.common.PowerObject;
import com.epsm.epsmcore.model.consumption.Consumer;
import com.epsm.epsmcore.model.generation.PowerStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Simulation {

	private float frequencyInPowerSystem = Constants.STANDART_FREQUENCY;
	private LocalDateTime currentDateTimeInSimulation;
	private final Map<Long, PowerStation> powerStations = new ConcurrentHashMap<>();
	private final Map<Long, Consumer> consumers = new ConcurrentHashMap<>();
	private static final Logger logger = LoggerFactory.getLogger(Simulation.class);

	public Simulation(LocalDateTime startDateTime) {
		currentDateTimeInSimulation = startDateTime;
	}
	
	public void doNextStep() {
		float powerBalance = calculatePowerBalanceInMW();
		calculateFrequencyInPowerSystem(powerBalance);
		changeTimeForStep();
		
		if(isFrequencyLowerThanNormal()){
			logFrequency();
		}
	}
	
	private float calculatePowerBalanceInMW(){
		float balance = 0;

		for(PowerObject object: powerStations.values()){
			balance += object.calculatePowerBalance();
		}

		for(PowerObject object: consumers.values()){
			balance += object.calculatePowerBalance();
		}
		
		if(isItExactlyMinute()){
			logger.debug("State: simul., sim.time: {}, power balance: {} MW.", currentDateTimeInSimulation, balance);
		}
		
		return balance;
	}

	private boolean isItExactlyMinute(){
		return currentDateTimeInSimulation.getSecond() == 0 && currentDateTimeInSimulation.getNano() == 0;
	}
	
	/*
	 * Just a stub. Power must be calculated for every generator independent, taking into account
	 * bandwich of power lines, transformers and many other electrical parameters. And only if power
	 * system will be sustainable, it will be possible to get system frequency.
	 */
	private void calculateFrequencyInPowerSystem(float powerBalance){
		frequencyInPowerSystem = frequencyInPowerSystem + (powerBalance / Constants.TIME_CONASTNT)
				* ((float)Constants.SIMULATION_STEP_IN_NANOS / Constants.NANOS_IN_SECOND);
	}
	
	private void changeTimeForStep(){
		currentDateTimeInSimulation = currentDateTimeInSimulation.plusNanos(Constants.SIMULATION_STEP_IN_NANOS);
	}

	private boolean isFrequencyLowerThanNormal(){
		float delta = Math.abs(Constants.STANDART_FREQUENCY - frequencyInPowerSystem);
		
		return delta > Constants.ACCEPTABLE_FREQUENCY_DELTA;
	}
	
	private void logFrequency(){
		if(isItExactlyMinute()){
			logger.warn("sim.time: {}, unnacept. frequency: {} Hz. {}", getDateTimeInSimulation(), frequencyInPowerSystem);
			logger.warn("consumers: {}", consumers.values().stream().map(PowerObject::getState).collect(Collectors.toList()));
			logger.warn("stations: {}", powerStations.values().stream().map(PowerObject::getState).collect(Collectors.toList()));
		}
	}
	
	public void sendStatesToDispatcher() {
		for(PowerObject object: powerStations.values()){
			object.sendStatesToDispatcher();
		}

		for(PowerObject object: consumers.values()){
			object.sendStatesToDispatcher();
		}
	}
	
	public float getFrequencyInPowerSystem(){
		return frequencyInPowerSystem;
	}
	
	public LocalDateTime getDateTimeInSimulation(){
		return currentDateTimeInSimulation;
	}

	public void addPowerStation(PowerStation powerStation) {
		powerStations.put(powerStation.getId(), powerStation);
	}

	public void addConsumer(Consumer consumer) {
		consumers.put(consumer.getId(), consumer);
	}

	public Map<Long, PowerStation> getPowerStations() {
		return powerStations;
	}

	public Map<Long, Consumer> getConsumers() {
		return consumers;
	}
}
