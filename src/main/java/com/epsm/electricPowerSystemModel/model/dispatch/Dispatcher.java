package main.java.com.epsm.electricPowerSystemModel.model.dispatch;

public interface Dispatcher {
	public void registerPowerObject(ObjectToBeDispatching powerObject);
	public void acceptReport(PowerObjectState report);
}
