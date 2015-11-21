package main.java.com.yvaleriy85.esm.model;

import java.time.LocalTime;

public interface PowerSystem {
	float getBalance();
	void addPowerStation(PowerStation powerStation);
	void addPowerConsumer(PowerConsumer powerConsumer);
	LocalTime getSystemTime();
}
