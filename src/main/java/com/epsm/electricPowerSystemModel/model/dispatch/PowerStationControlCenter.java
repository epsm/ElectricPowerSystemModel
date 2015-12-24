package main.java.com.epsm.electricPowerSystemModel.model.dispatch;

public interface PowerStationControlCenter extends ObjectToBeDispatching{
	public void performGenerationSchedule(PowerStationGenerationSchedule generationSchedule);
}
