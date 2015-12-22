package main.java.com.yvhobby.epsm.model.dispatch;

public interface Dispatcher {
	public void registerPowerStation(PowerStationParameters parameters);
	public void acceptPowerStationStateReport(PowerStationStateReport report);
	public void acceptSimulationParameters(SimulationReport report);
	public void acceptConsumerReport(ConsumerReport report);
}
