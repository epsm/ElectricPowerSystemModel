package com.epsm.electricPowerSystemModel.model.generalModel;

import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingObject;

public interface SimulationObject extends DispatchingObject{
	void doRealTimeDependingOperations();
	float calculatePowerBalance();
}
