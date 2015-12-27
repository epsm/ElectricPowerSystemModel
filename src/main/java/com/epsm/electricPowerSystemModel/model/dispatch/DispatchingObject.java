package com.epsm.electricPowerSystemModel.model.dispatch;

public interface DispatchingObject {
	public void acceptMessage(DispatcherMessage message);
	public void interactWithDisparcher();
}
