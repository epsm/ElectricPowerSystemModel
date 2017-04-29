package com.epsm.epsmCore.model.generalModel;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.PowerObject;
import com.epsm.epsmCore.model.consumption.Consumer;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.generation.PowerStation;

public class ElectricPowerSystemSimulationImpl implements ElectricPowerSystemSimulation{
	
	private Map<Long, PowerObject> objects;
	private float frequencyInPowerSystem;
	private LocalDateTime currentDateTimeInSimulation;
	private PowerObjectFactory powerObjectFactory;
	private Logger logger;

	public ElectricPowerSystemSimulationImpl(TimeService timeService, Dispatcher dispatcher,
			LocalDateTime startDateTime) {
		
		currentDateTimeInSimulation = startDateTime;

		objects = new ConcurrentHashMap<Long, PowerObject>();
		frequencyInPowerSystem = Constants.STANDART_FREQUENCY;
		powerObjectFactory = new PowerObjectFactory(objects, this, timeService, dispatcher);
		logger = LoggerFactory.getLogger(ElectricPowerSystemSimulationImpl.class);
	}
	
	@Override
	public void calculateNextStep() {
		float powerBalance = calculatePowerBalanceInMW();
		calculateFrequencyInPowerSystem(powerBalance);
		changeTimeForStep();
		
		if(isFrequencyLowerThanNormal()){
			logFrequency();
		}
	}
	
	private float calculatePowerBalanceInMW(){
		float balance = 0;

		for(PowerObject object: objects.values()){
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
		currentDateTimeInSimulation = currentDateTimeInSimulation.plusNanos(
				Constants.SIMULATION_STEP_IN_NANOS);
	}

	private boolean isFrequencyLowerThanNormal(){
		float delta = Math.abs(Constants.STANDART_FREQUENCY - frequencyInPowerSystem);
		
		if(delta > Constants.ACCEPTABLE_FREQUENCY_DELTA){
			return true;
		}else{
			return false;
		}
	}
	
	private void logFrequency(){
		if(isItExactlySecond()){
			logger.warn("sim.time: {}, unnacept. frequency: {} Hz.",
					getDateTimeInSimulation(), frequencyInPowerSystem);
		}
	}
	
	private boolean isItExactlySecond(){
		return currentDateTimeInSimulation.getNano() == 0;
	}
	
	@Override
	public float getFrequencyInPowerSystem(){
		return frequencyInPowerSystem;
	}
	
	@Override
	public LocalDateTime getDateTimeInSimulation(){
		return currentDateTimeInSimulation;
	}

	@Override
	public Map<Long, DispatchingObject> getDispatchingObjects() {
		return Collections.unmodifiableMap(new HashMap<Long, DispatchingObject>(objects));
	}

	@Override
	public Map<Long, RealTimeOperations> getRealTimeDependingObjects() {
		return Collections.unmodifiableMap(new HashMap<Long, RealTimeOperations>(objects));
	}
	
	@Override
	public void createPowerObject(CreationParameters parameters) {
		powerObjectFactory.create(parameters);
	}
	
	/*
	 * Non public for unit testing. When PowerObjects factories will be implemented it will
	 * be possible to remove this two methods from here and create appropriate objects
	 * for testing.
	 */
	void addPowerStation(PowerStation station) {
		long powerObjectId = station.getId();
		objects.put(powerObjectId, station);
	}

	void addPowerConsumer(Consumer consumer) {
		long powerObjectId = consumer.getId();
		objects.put(powerObjectId, consumer);
	}
}
