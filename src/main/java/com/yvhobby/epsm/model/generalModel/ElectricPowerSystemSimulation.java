package main.java.com.yvhobby.epsm.model.generalModel;

import java.time.LocalTime;

import main.java.com.yvhobby.epsm.model.consumption.PowerConsumer;
import main.java.com.yvhobby.epsm.model.dispatch.SimulationReport;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;

public interface ElectricPowerSystemSimulation {
	public SimulationReport calculateNextStep();
	public float getFrequencyInPowerSystem();
	public LocalTime getTime();
	public void addPowerConsumer(PowerConsumer powerConsumer);
	public void addPowerStation(PowerStation powerStation);
}
