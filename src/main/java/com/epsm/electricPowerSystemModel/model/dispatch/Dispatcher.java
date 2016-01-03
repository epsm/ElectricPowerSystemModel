package com.epsm.electricPowerSystemModel.model.dispatch;

public interface Dispatcher {
	public void establishConnection(Parameters parameters);
	public void acceptState(State state);
}
