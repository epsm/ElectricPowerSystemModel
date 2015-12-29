package com.epsm.electricPowerSystemModel.model.dispatch;

public abstract class PowerObjectState extends Message{
	public PowerObjectState(long powerObjectId) {
		super(powerObjectId);
	}
}
