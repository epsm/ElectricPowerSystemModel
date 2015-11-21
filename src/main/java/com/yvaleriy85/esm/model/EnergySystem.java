package main.java.com.yvaleriy85.esm.model;

import java.util.ArrayList;
import java.util.List;

public class EnergySystem{

	private List<PowerStation> powerStations = new ArrayList<PowerStation>();
	private List<PowerConsumer> powerConsumers = new ArrayList<PowerConsumer>();
	
	public float getBalance() {
		float totalGenerations = 0;
		float totalConsumption = 0;
		
		for(PowerStation station: powerStations){
			totalGenerations += station.getCurrentGeneration();
		}
		
		for(PowerConsumer consumer: powerConsumers){
			totalGenerations += consumer.getCurrentConsumptionInMW();
		}
		
		return totalGenerations - totalConsumption;
	}

	public void addPowerStation(PowerStation powerStation) {
		powerStations.add(powerStation);
	}

	public void addPowerConsumer(PowerConsumer powerConsumer) {
		powerConsumers.add(powerConsumer);
	}
}
