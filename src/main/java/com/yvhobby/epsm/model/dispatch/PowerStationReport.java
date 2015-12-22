package main.java.com.yvhobby.epsm.model.dispatch;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

public class PowerStationReport extends Report{
	private int powerStationNumber;
	private LocalTime timeStamp;
	private Set<GeneratorStateReport> generatorsStatesReports;
	private StringBuilder stringBuilder;
	private DateTimeFormatter formatter;

	public PowerStationReport(int powerStationNumber, LocalTime timeStamp,
			Set<GeneratorStateReport> generatorsStatesReports) {
		this.powerStationNumber = powerStationNumber;
		this.timeStamp = timeStamp;
		this.generatorsStatesReports = Collections.unmodifiableSet(generatorsStatesReports);
		
		stringBuilder = new StringBuilder();
		formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
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
		stringBuilder.setLength(0);
		stringBuilder.append("PowerStationStateReport ");
		stringBuilder.append("[time in simulation= ");
		stringBuilder.append(timeStamp.format(formatter));
		stringBuilder.append(", powerStationNumber=");
		stringBuilder.append(powerStationNumber);
		stringBuilder.append(", generatorsStatesReports ");
		stringBuilder.append(generatorsStatesReports);
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
