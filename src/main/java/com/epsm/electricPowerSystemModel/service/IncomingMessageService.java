package com.epsm.electricPowerSystemModel.service;

import com.epsm.electricPowerSystemModel.model.dispatch.Command;

public interface IncomingMessageService {
	void acceptCommand(Command command);
}
