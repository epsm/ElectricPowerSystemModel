package main.java.com.yvhobby.epsm.model.dispatch;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

public class PowerStationStateReport {
	private int powerStationNumber;
	private LocalTime timeStamp;
	private Set<GeneratorStateReport> generatorsStatesReports;
	private StringBuilder stringBuilder;

	public PowerStationStateReport(int powerStationNumber, LocalTime timeStamp,
			Set<GeneratorStateReport> generatorsStatesReports) {
		this.powerStationNumber = powerStationNumber;
		this.timeStamp = timeStamp;
		this.generatorsStatesReports = Collections.unmodifiableSet(generatorsStatesReports);
		
		stringBuilder = new StringBuilder();
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

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String time = timeStamp.format(formatter);
		
		stringBuilder.setLength(0);
		stringBuilder.append("PowerStationStateReport ");
		stringBuilder.append("[powerStationNumber=");
		stringBuilder.append(powerStationNumber);
		stringBuilder.append(", time in simulation=");
		stringBuilder.append(time);
		stringBuilder.append(", generatorsStatesReports ");
		stringBuilder.append(generatorsStatesReports);
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
