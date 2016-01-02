package com.epsm.electricPowerSystemModel.model.dispatch;

import java.util.Set;

public interface StatesContainer extends State{
	Set<Long> getStatesIds();
}
