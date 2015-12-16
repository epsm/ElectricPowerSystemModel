package main.java.com.yvhobby.epsm.model.consumption;

import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;

public abstract class PowerConsumer{
	public abstract float getCurrentLoadInMW();
	public abstract void setElectricalPowerSystemSimulation(
			ElectricPowerSystemSimulation simulation);
	public abstract void setDegreeOfDependingOnFrequency(float degreeOnDependingOfFrequency);
}
