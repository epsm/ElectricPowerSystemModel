package main.java.com.yvhobby.epsm.model.dispatch;

public interface Dispatcher {
	public void registerPowerStation(PowerStationParameters parameters);
	public void acceptPowerStationState(PowerStationStateReport report);
}
