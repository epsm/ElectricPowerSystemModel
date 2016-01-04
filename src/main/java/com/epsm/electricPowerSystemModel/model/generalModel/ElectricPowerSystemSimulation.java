package com.epsm.electricPowerSystemModel.model.generalModel;

import java.time.LocalTime;
import java.util.Map;

import com.epsm.electricPowerSystemModel.model.dispatch.CreationParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingObject;

public interface ElectricPowerSystemSimulation{
	public void calculateNextStep();
	public float getFrequencyInPowerSystem();
	public LocalTime getTimeInSimulation();
	public void createPowerObject(CreationParameters parameters);
	public Map<Long, DispatchingObject> getDispatchingObjects();
	public Map<Long, RealTimeOperations> getRealTimeDependingObjects();
}
