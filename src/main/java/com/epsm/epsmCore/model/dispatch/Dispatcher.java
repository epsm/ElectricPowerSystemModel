package com.epsm.epsmCore.model.dispatch;

public interface Dispatcher {
	boolean registerObject(Parameters parameters);
	void acceptState(State state);
}
