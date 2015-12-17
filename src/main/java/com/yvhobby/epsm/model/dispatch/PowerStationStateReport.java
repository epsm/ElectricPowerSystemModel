package main.java.com.yvhobby.epsm.model.dispatch;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

public class PowerStationStateReport {
	private int id;
	private LocalTime timeStamp;
	private Set<GeneratorStateReport> generatorsStatesReports;

	public PowerStationStateReport(int id, LocalTime timeStamp,
			Set<GeneratorStateReport> generatorsStatesReports) {
		this.id = id;
		this.timeStamp = timeStamp;
		this.generatorsStatesReports = Collections.unmodifiableSet(generatorsStatesReports);
	}

	public int getId() {
		return id;
	}
	
	public LocalTime getTimeStamp() {
		return timeStamp;
	}

	public Set<GeneratorStateReport> getGeneratorsStatesReports() {
		return generatorsStatesReports;
	}
}
