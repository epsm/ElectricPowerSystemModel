package com.epsm.electricPowerSystemModel.model.dispatch;

import java.util.Set;

public interface ParametersContainer extends Parameters{
	Set<Long> getCommandsIds();
}
