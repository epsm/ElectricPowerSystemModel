package com.epsm.epsmCore.model.dispatch;

public interface Dispatcher {
	boolean registerObject(Parameters parameters);
	boolean acceptState(State state);
}
