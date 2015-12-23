package main.java.com.epsm.electricPowerSystemModel.model.dispatch;

public interface Dispatcher {
	public void registerPowerStation(PowerStationParameters parameters);
	public void acceptReport(PowerObjectState report);
}
