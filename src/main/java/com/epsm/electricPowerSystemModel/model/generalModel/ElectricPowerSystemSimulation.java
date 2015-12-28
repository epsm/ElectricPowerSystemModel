package com.epsm.electricPowerSystemModel.model.generalModel;

import java.time.LocalTime;

import com.epsm.electricPowerSystemModel.model.consumption.Consumer;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;

public interface ElectricPowerSystemSimulation{
	public long generateId();
	public void calculateNextStep();
	public float getFrequencyInPowerSystem();
	public LocalTime getTimeInSimulation();
	public void addPowerConsumer(Consumer powerConsumer);
	public void addPowerStation(PowerStation powerStation);
	public PowerObject getPowerStation(int powerStationNumber);
	public PowerObject getConsumer(int consumerNumber);
}
