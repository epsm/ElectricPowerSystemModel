package com.epsm.electricPowerSystemModel.model.dispatch;

import java.util.Set;

public interface CommandsContainer extends Command{
	Set<Long> getCommandsIds();
}
