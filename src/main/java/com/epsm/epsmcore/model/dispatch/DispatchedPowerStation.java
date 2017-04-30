package com.epsm.epsmcore.model.dispatch;

import com.epsm.epsmcore.model.generation.PowerStationGenerationSchedule;

public interface DispatchedPowerStation {
	void executeSchedule(PowerStationGenerationSchedule schedule);
}
