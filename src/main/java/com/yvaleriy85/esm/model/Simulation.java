package main.java.com.yvaleriy85.esm.model;

import java.time.LocalTime;

public class Simulation{
	private static LocalTime currentTimeInSimulation = setStartSimulationTime();
	
	private static  LocalTime setStartSimulationTime(){
		int hour = 0;
		int minute = 0;
		return LocalTime.of(hour, minute);
	}
	
	public static void nextState(){
		currentTimeInSimulation = currentTimeInSimulation.plusHours(1);
	}
	
	public static LocalTime getTime(){
		return currentTimeInSimulation;
	}
}