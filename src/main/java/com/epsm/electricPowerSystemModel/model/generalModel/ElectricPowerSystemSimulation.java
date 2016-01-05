package com.epsm.electricPowerSystemModel.model.generalModel;

import java.time.LocalTime;
import java.util.Map;

import com.epsm.electricPowerSystemModel.model.dispatch.CreationParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingObject;

public interface ElectricPowerSystemSimulation{
	void calculateNextStep();
	float getFrequencyInPowerSystem();
	LocalTime getTimeInSimulation();
	void createPowerObject(CreationParameters parameters);
	Map<Long, DispatchingObject> getDispatchingObjects();
	Map<Long, RealTimeOperations> getRealTimeDependingObjects();
}
