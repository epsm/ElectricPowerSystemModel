package com.epsm.electricPowerSystemModel.model.dispatch;

public abstract class DispatcherMessage extends Message{
	public DispatcherMessage(long powerObjectId) {
		super(powerObjectId);
	}
}
