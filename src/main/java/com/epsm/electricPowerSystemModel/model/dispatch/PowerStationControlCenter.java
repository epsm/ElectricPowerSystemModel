package com.epsm.electricPowerSystemModel.model.dispatch;

public interface PowerStationControlCenter extends DispatchingObject{
	public void performGenerationSchedule(PowerStationGenerationSchedule generationSchedule);
}
