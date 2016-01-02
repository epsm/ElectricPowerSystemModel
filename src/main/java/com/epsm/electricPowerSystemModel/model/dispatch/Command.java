package com.epsm.electricPowerSystemModel.model.dispatch;

import java.util.Set;

public interface Command {
	long getPowerObjectId();
	Set<Long> getCommandsIds();
}
