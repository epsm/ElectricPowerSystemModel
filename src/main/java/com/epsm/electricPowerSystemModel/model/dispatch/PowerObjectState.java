package com.epsm.electricPowerSystemModel.model.dispatch;

public abstract class PowerObjectState{
	protected long powerObjectId;
	
	public PowerObjectState(long powerObjectId) {
		this.powerObjectId = powerObjectId;
	}

	public long getPowerObjectId(){
		return powerObjectId;
	}

	public abstract String toString();
}
