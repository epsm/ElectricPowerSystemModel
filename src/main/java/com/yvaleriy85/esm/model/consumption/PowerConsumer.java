package main.java.com.yvaleriy85.esm.model.consumption;

import main.java.com.yvaleriy85.esm.model.generalModel.ElectricPowerSystemSimulationImpl;

public abstract class PowerConsumer{
	public abstract float getCurrentConsumptionInMW();
	public abstract void setElectricalPowerSystemSimulation(
			ElectricPowerSystemSimulationImpl powerSystemSimulation);
}
