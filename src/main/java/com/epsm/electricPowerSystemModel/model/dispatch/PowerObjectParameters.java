package com.epsm.electricPowerSystemModel.model.dispatch;

public abstract class PowerObjectParameters {
	private long powerObjectid;

	public PowerObjectParameters(long id) {
		this.powerObjectid = id;
	}

	public long getPowerObjectId() {
		return powerObjectid;
	}
}
