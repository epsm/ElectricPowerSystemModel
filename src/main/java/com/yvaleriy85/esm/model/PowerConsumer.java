package main.java.com.yvaleriy85.esm.model;

public interface PowerConsumer {
	public float getRandomComponentInPercent();
	public float getInstalledPowerInMW();
	public float getCurrentConsumptionInMW();
	public void setPowerSystem(PowerSystem powerSystem);
}
