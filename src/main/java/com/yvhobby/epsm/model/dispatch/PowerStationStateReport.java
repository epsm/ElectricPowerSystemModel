package main.java.com.yvhobby.epsm.model.dispatch;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

public class PowerStationStateReport {
	private int powerStationNumber;
	private LocalTime timeStamp;
	private Set<GeneratorStateReport> generatorsStatesReports;

	public PowerStationStateReport(int powerStationNumber, LocalTime timeStamp,
			Set<GeneratorStateReport> generatorsStatesReports) {
		this.powerStationNumber = powerStationNumber;
		this.timeStamp = timeStamp;
		this.generatorsStatesReports = Collections.unmodifiableSet(generatorsStatesReports);
	}

	public int getPowerStationNumber() {
		return powerStationNumber;
	}
	
	public LocalTime getTimeStamp() {
		return timeStamp;
	}

	public Set<GeneratorStateReport> getGeneratorsStatesReports() {
		return generatorsStatesReports;
	}
}
