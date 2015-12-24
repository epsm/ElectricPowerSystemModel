package main.java.com.epsm.electricPowerSystemModel.model.dispatch;

public interface StateSenderSource {
	public PowerObjectState getState();
	public void setStateSender(StateSender sender);
}
