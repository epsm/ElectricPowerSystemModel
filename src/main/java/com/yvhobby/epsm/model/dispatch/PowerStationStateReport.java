package main.java.com.yvhobby.epsm.model.dispatch;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

public class PowerStationStateReport {
	private int powerStationId;
	private LocalTime timeStamp;
	private Set<GeneratorStateReport> generatorsStatesReports;

	public PowerStationStateReport(int powerStationId, LocalTime timeStamp,
			Set<GeneratorStateReport> generatorsStatesReports) {
		this.powerStationId = powerStationId;
		this.timeStamp = timeStamp;
		this.generatorsStatesReports = Collections.unmodifiableSet(generatorsStatesReports);
	}

	public int getPowerStationId() {
		return powerStationId;
	}
	
	public LocalTime getTimeStamp() {
		return timeStamp;
	}

	public Set<GeneratorStateReport> getGeneratorsStatesReports() {
		return generatorsStatesReports;
	}
}
