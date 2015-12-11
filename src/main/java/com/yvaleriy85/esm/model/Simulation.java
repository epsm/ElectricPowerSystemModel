package main.java.com.yvaleriy85.esm.model;

import java.time.LocalTime;

public class Simulation{
	private static LocalTime currentTimeInSimulation = setStartSimulationTime();
	private static EnergySystem energySystem;
	static final int SIMULATION_STEP_IN_NANOS =  (int)Math.pow(10, 8);
	
	private static LocalTime setStartSimulationTime(){
		int hour = 0;
		int minute = 0;
		int second = 0;
		int nanoOfSecond = 0;
		return LocalTime.of(hour, minute, second, nanoOfSecond);
	}

	public static LocalTime getTime(){
		return currentTimeInSimulation;
	}

	static public void startSimulation(){
		while(true){
			energySystem.calculateNextStep();
			currentTimeInSimulation = currentTimeInSimulation.
					plusNanos(SIMULATION_STEP_IN_NANOS);
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void setEnergySystem(EnergySystem es){
		energySystem = es;
	}
}