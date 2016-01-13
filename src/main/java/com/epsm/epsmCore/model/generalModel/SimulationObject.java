package com.epsm.epsmCore.model.generalModel;

import com.epsm.epsmCore.model.dispatch.DispatchingObject;

public interface SimulationObject extends DispatchingObject{
	float calculatePowerBalance();
}
