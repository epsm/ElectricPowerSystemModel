package com.epsm.epsmCore.model.dispatch;

public interface Dispatcher {
	void establishConnection(Parameters parameters);
	void acceptState(State state);
}
