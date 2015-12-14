package main.java.com.yvhobby.epsm.model.generalModel;

import java.time.LocalTime;

public interface ElectricPowerSystemSimulation {
	public SimulationParameters calculateNextStep();
	public float getFrequencyInPowerSystem();
	public LocalTime getTime();
}
