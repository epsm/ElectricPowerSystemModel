package com.epsm.electricPowerSystemModel.model.dispatch;

public interface Dispatcher {
	public void establishConnection(PowerObjectParameters parameters);
	public void acceptReport(PowerObjectState report);
}
