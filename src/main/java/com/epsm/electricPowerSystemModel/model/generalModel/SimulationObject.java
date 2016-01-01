package com.epsm.electricPowerSystemModel.model.generalModel;

public interface SimulationObject {
	void doRealTimeDependingOperation();
	float calculatePowerBalance();
}
