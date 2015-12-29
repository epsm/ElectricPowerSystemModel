package com.epsm.electricPowerSystemModel.model.dispatch;

public abstract class Message {
	protected long powerObjectId;
	
	public Message(long powerObjectId) {
		this.powerObjectId = powerObjectId;
	}

	public long getPowerObjectId(){
		return powerObjectId;
	}

	public abstract String toString();
}
