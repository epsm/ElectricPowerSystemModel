package com.epsm.electricPowerSystemModel.model.dispatch;

public interface ObjectToBeDispatching {
	public void acceptMessage(DispatcherMessage message);
	public void doNextStep();
}
