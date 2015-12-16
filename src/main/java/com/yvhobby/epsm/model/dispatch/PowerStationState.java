package main.java.com.yvhobby.epsm.model.dispatch;

import java.time.LocalTime;
import java.util.Set;

public class PowerStationState {
	private LocalTime timeStamp;
	private Set<GeneratorState> generatorsStates;
	
	public PowerStationState(LocalTime timeStamp, Set<GeneratorState> generatorsStates) {
		super();
		this.timeStamp = timeStamp;
		this.generatorsStates = generatorsStates;
	}

	public LocalTime getTimeStamp() {
		return timeStamp;
	}

	public Set<GeneratorState> getGeneratorsStates() {
		return generatorsStates;
	}
}
