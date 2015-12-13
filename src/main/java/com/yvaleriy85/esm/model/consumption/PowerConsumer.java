package main.java.com.yvaleriy85.esm.model.consumption;

import main.java.com.yvaleriy85.esm.model.generalModel.ElectricPowerSystemSimulation;

public abstract class PowerConsumer{
	public abstract float getCurrentConsumptionInMW();
	public abstract void setElectricalPowerSystemSimulation(
			ElectricPowerSystemSimulation powerSystemSimulation);
}
