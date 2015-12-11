package main.java.com.yvaleriy85.esm.model;

import java.time.LocalTime;

public class Simulation{
	private static LocalTime currentTimeInEnergySystem = setStartSimulationTime();
	static final int SIMULATION_STEP_IN_NANOS =  (int)Math.pow(10, 8);
	static EnergySystem energySystem  = new EnergySystem();
	
	private static LocalTime setStartSimulationTime(){
		int hour = 0;
		int minute = 0;
		int second = 0;
		int nanoOfSecond = 0;
		return LocalTime.of(hour, minute, second, nanoOfSecond);
	}

	public static LocalTime getTime(){
		return currentTimeInEnergySystem;
	}

}