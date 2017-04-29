package com.epsm.epsmCore.model.dispatch;

import com.epsm.epsmCore.model.generation.PowerStationGenerationSchedule;

public interface PowerStation {
	void executeSchedule(PowerStationGenerationSchedule schedule);
}
