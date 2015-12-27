package com.epsm.electricPowerSystemModel.model.dispatch;

public interface Dispatcher {
	public void connectToPowerObject(ObjectToBeDispatching powerObject);
	public void acceptReport(PowerObjectState report);
}
