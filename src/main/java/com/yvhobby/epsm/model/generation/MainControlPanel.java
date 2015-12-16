package main.java.com.yvhobby.epsm.model.generation;

import main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration.LoadCurve;

public class MainControlPanel {
	private PowerStation station;
	private LoadCurve schedule;
	private boolean isStationRegistered;
	
	public void registerStation(){
		isStationRegistered = true;
	}
	
	public void setGenerationSchedule(LoadCurve schedule){
		this.schedule = schedule;
	}
}
