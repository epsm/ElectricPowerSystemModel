package com.epsm.electricPowerSystemModel.model.dispatch;

//Just a stub for now. There may be methods allows consumer to be turned on/of or limit consumption.
public class ConsumptionPermission extends DispatcherMessage{
	public ConsumptionPermission(long powerObjectId) {
		super(powerObjectId);
	}

	@Override
	public String toString() {
		return "ConsumptionPermission toString() stub";
	}
}
