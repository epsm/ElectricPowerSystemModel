package com.epsm.epsmcore.model.dispatch;

import com.epsm.epsmcore.model.consumption.ConsumerPermission;

public interface DispatchedConsumer {
	void processPermissions(ConsumerPermission permission);
}
