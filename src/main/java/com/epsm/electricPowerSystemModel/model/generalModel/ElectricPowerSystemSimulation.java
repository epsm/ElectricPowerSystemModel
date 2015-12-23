package main.java.com.epsm.electricPowerSystemModel.model.generalModel;

import java.time.LocalTime;

import main.java.com.epsm.electricPowerSystemModel.model.consumption.Consumer;
import main.java.com.epsm.electricPowerSystemModel.model.generation.PowerStation;

public interface ElectricPowerSystemSimulation{
	public void calculateNextStep();
	public float getFrequencyInPowerSystem();
	public LocalTime getTime();
	public void addPowerConsumer(Consumer powerConsumer);
	public void addPowerStation(PowerStation powerStation);
}
