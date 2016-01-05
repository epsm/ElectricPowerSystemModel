package com.epsm.electricPowerSystemModel.model.dispatch;

public interface Dispatcher {
	void establishConnection(Parameters parameters);
	void acceptState(State state);
}
