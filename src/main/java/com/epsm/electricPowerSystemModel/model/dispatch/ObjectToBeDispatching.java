package com.epsm.electricPowerSystemModel.model.dispatch;

public interface ObjectToBeDispatching {
	public void sendReports();
	public void registerWithDispatcher(Dispatcher dispatcher);
}
