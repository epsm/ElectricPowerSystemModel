package com.epsm.electricPowerSystemModel.model.generalModel;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.PowerObject;
import com.epsm.electricPowerSystemModel.model.consumption.Consumer;
import com.epsm.electricPowerSystemModel.model.dispatch.CreationParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingObject;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;

public class ElectricPowerSystemSimulationImpl implements ElectricPowerSystemSimulation{
	
	private Map<Long, PowerObject> objects;
	private float frequencyInPowerSystem;
	private LocalTime currentTimeInSimulation;
	private PowerObjectFactory powerObjectFactory;
	private final float TIME_CONASTNT = 5_000;
	private final int SIMULATION_STEP_IN_NANOS = 100_000_000;
	private final float ACCEPTABLE_FREQUENCY_DELTA = 0.03f;
	private Logger logger;

	public ElectricPowerSystemSimulationImpl(TimeService timeService, Dispatcher dispatcher) {
		objects = new ConcurrentHashMap<Long, PowerObject>();
		frequencyInPowerSystem = Constants.STANDART_FREQUENCY;
		currentTimeInSimulation = LocalTime.NOON;
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
		
		logger.debug("sim.time: {}, power balance: {} MW.", currentTimeInSimulation, balance);
		
		return balance;
	}
	
	/*
	 * Just a stub. Power must be calculated for every generator independent, taking into account
	 * bandwich of power lines, transformers and many other electrical parameters. And only if power
	 * system will be sustainable, it will be possible to get system frequency.
	 */
	private void calculateFrequencyInPowerSystem(float powerBalance){
		frequencyInPowerSystem = frequencyInPowerSystem + (powerBalance / TIME_CONASTNT)
				* ((float)SIMULATION_STEP_IN_NANOS / Constants.NANOS_IN_SECOND);
	}
	
	private void changeTimeForStep(){
		currentTimeInSimulation = currentTimeInSimulation.plusNanos(SIMULATION_STEP_IN_NANOS);
	}

	private boolean isFrequencyLowerThanNormal(){
		float delta = Math.abs(Constants.STANDART_FREQUENCY - frequencyInPowerSystem);
		
		if(delta > ACCEPTABLE_FREQUENCY_DELTA){
			return true;
		}else{
			return false;
		}
	}
	
	private void logFrequency(){
		if(isItExactlySecond()){
			logger.warn("sim.time: {}, unnacept. frequency: {} Hz.",
					getTimeInSimulation(), frequencyInPowerSystem);
		}
	}
	
	private boolean isItExactlySecond(){
		return currentTimeInSimulation.getNano() == 0;
	}
	
	@Override
	public float getFrequencyInPowerSystem(){
		return frequencyInPowerSystem;
	}
	
	@Override
	public LocalTime getTimeInSimulation(){
		return currentTimeInSimulation;
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
