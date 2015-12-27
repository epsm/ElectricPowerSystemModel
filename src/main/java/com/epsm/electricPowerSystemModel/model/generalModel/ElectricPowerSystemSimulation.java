package com.epsm.electricPowerSystemModel.model.generalModel;

import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.consumption.Consumer;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;

public interface ElectricPowerSystemSimulation{
	public void calculateNextStep();
	public float getFrequencyInPowerSystem();
	public LocalTime getTimeInSimulation();
	public void addPowerConsumer(Consumer powerConsumer);
	public void addPowerStation(PowerStation powerStation);
	public PowerSystemObject getPowerStation(int powerStationNumber);
	public PowerSystemObject getConsumer(int consumerNumber);
}
