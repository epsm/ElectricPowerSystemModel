package main.java.com.yvaleriy85.esm.model.generalModel;

import java.time.LocalTime;

public interface ElectricPowerSystemSimulation {
	public SimulationParameters calculateNextStep();
	public float getFrequencyInPowerSystem();
	public LocalTime getTime();
}
