package main.java.com.yvhobby.epsm.model.generation;

public class MainControlPanel {
	private PowerStation station;
	private GenerationSchedule schedule;
	private boolean isStationRegistered;
	
	public void registerStation(){
		isStationRegistered = true;
	}
	
	public void setGenerationSchedule(GenerationSchedule schedule){
		this.schedule = schedule;
	}
}
