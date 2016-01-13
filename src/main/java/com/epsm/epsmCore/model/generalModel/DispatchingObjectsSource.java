package com.epsm.epsmCore.model.generalModel;

import java.util.Map;

import com.epsm.epsmCore.model.dispatch.DispatchingObject;

public interface DispatchingObjectsSource {
	Map<Long, DispatchingObject> getDispatchingObjects();
}
