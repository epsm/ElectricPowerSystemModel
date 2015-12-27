package com.epsm.electricPowerSystemModel.model.dispatch;

public interface Dispatcher {
	public void connectToPowerObject(DispatchingObject powerObject);
	public void acceptReport(PowerObjectState report);
}
