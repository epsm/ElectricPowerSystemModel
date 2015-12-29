package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;

//Just a stub for now. There may be methods allows consumer to be turned on/of or limit consumption.
public class PermissionForConsumption extends DispatcherMessage{
	public PermissionForConsumption(LocalDateTime creationTime) {
		super(creationTime);
	}
}
