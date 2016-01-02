package com.epsm.electricPowerSystemModel.model.dispatch;

import java.util.Set;

public interface Parameters {
	long getPowerObjectId();
	Set<Long> getCommandsIds();
}
