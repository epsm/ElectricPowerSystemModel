package com.epsm.electricPowerSystemModel.model.generalModel;

import java.time.LocalTime;
import java.util.Map;

import com.epsm.electricPowerSystemModel.model.dispatch.ConsumerParametersStub;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingObject;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerStationParameters;

public interface ElectricPowerSystemSimulation{
	public long generateId();
	public void calculateNextStep();
	public float getFrequencyInPowerSystem();
	public LocalTime getTimeInSimulation();
	public void createConsumer(ConsumerParametersStub parameters);
	public void createPowerStation(PowerStationParameters parameters);
	public Map<Long, DispatchingObject> getDispatchingObjects();
}
