package com.epsm.electricPowerSystemModel.model.generalModel;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.PowerObject;
import com.epsm.electricPowerSystemModel.model.consumption.ConsumerParametersStub;
import com.epsm.electricPowerSystemModel.model.dispatch.CreationParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingObject;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationParameters;

public class ElectricPowerSystemSimulationImpl implements ElectricPowerSystemSimulation{
	private Map<Long, PowerObject> objects;
	private float frequencyInPowerSystem;
	private LocalTime currentTimeInSimulation;
	private AtomicLong idSource;
	private final float TIME_CONASTNT = 2_000;
	private final int SIMULATION_STEP_IN_NANOS = 100_000_000;
	private final float ACCEPTABLE_FREQUENCY_DELTA = 0.03f;
	private Logger logger;

	public ElectricPowerSystemSimulationImpl() {
		objects = new HashMap<Long, PowerObject>();
		frequencyInPowerSystem = GlobalConstants.STANDART_FREQUENCY;
		currentTimeInSimulation = LocalTime.NOON;
		idSource = new AtomicLong();
		logger = LoggerFactory.getLogger(ElectricPowerSystemSimulationImpl.class);
	}
	
	@Override
	public long generateId() {
		return idSource.getAndIncrement();
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
		
		return balance;
	}
	
	/*
	 * Just a stub. Power must be calculated for every generator independent, taking into account
	 * bandwich of power lines, transformers and many other electrical parameters. And only if power
	 * system will be sustainable, it will be possible to get system frequency.
	 */
	private void calculateFrequencyInPowerSystem(float powerBalance){
		frequencyInPowerSystem = frequencyInPowerSystem + ((powerBalance) / TIME_CONASTNT)
				* ((float)SIMULATION_STEP_IN_NANOS / GlobalConstants.NANOS_IN_SECOND);
	}
	
	private void changeTimeForStep(){
		currentTimeInSimulation = currentTimeInSimulation.plusNanos(SIMULATION_STEP_IN_NANOS);
	}

	private boolean isFrequencyLowerThanNormal(){
		float delta = Math.abs(GlobalConstants.STANDART_FREQUENCY - frequencyInPowerSystem);
		
		if(delta > ACCEPTABLE_FREQUENCY_DELTA){
			return true;
		}else{
			return false;
		}
	}
	
	private void logFrequency(){
		logger.warn("Frqueency is unacceptable: {} Hz.", frequencyInPowerSystem);
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
	public void createPowerObject(CreationParameters parameters) {
		// TODO Auto-generated method stub
		
	}
}
