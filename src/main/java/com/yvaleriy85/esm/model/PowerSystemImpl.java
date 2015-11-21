package main.java.com.yvaleriy85.esm.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PowerSystemImpl implements PowerSystem{

	private List<PowerStation> powerStations = new ArrayList<PowerStation>();
	private List<PowerConsumer> powerConsumers = new ArrayList<PowerConsumer>();
	private LocalTime currentTimeInSystem = LocalTime.of(0, 0);
	
	@Override
	public float getBalance() {
		float totalGenerations = 0;
		float totalConsumption = 0;
		
		for(PowerStation station: powerStations){
			totalGenerations += station.getCurrentGeneration();
		}
		
		for(PowerConsumer consumer: powerConsumers){
			totalGenerations += consumer.getCurrentConsumptionInMW();
		}
		
		changePowerSystemTime();
		
		return totalGenerations - totalConsumption;
	}
	
	private void changePowerSystemTime(){
		currentTimeInSystem = currentTimeInSystem.plusHours(1);
	}

	@Override
	public void addPowerStation(PowerStation powerStation) {
		powerStation.setPowerSystem(this);
		powerStations.add(powerStation);
	}

	@Override
	public void addPowerConsumer(PowerConsumer powerConsumer) {
		powerConsumer.setPowerSystem(this);
		powerConsumers.add(powerConsumer);
	}

	@Override
	public LocalTime getSystemTime() {
		return currentTimeInSystem;
	}
}
