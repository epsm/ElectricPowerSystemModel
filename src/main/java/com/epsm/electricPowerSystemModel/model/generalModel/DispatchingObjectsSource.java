package com.epsm.electricPowerSystemModel.model.generalModel;

import java.util.Map;

import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingObject;

public interface DispatchingObjectsSource {
	Map<Long, DispatchingObject> getDispatchingObjects();
}
