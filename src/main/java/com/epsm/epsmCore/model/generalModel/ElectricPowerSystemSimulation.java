package com.epsm.epsmCore.model.generalModel;

import java.time.LocalTime;
import java.util.Map;

import com.epsm.epsmCore.model.dispatch.CreationParameters;

public interface ElectricPowerSystemSimulation extends DispatchingObjectsSource{
	void calculateNextStep();
	float getFrequencyInPowerSystem();
	LocalTime getTimeInSimulation();
	void createPowerObject(CreationParameters parameters);
	Map<Long, RealTimeOperations> getRealTimeDependingObjects();
}
