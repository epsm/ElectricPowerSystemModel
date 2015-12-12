package test.java.com.yvaleriy85.esm.model.manualTesting;

import java.util.Formatter;

import main.java.com.yvaleriy85.esm.model.ElectricPowerSystemSimulation;
import main.java.com.yvaleriy85.esm.model.SimulationParameters;

public class PowerSystemSimulationManualTest {
	private Formatter fmt;
	private ElectricPowerSystemSimulation simulation;
	private SimulationParameters parameters;
	private StringBuilder sb;
	private int counter;
	private final int INTERVAL_BETWEEN_PRINTS = 0;
	private final int PAUSE_BETWEEN_CALCULATING_STEPS_IN_MS = 5;
	
	public static void main(String[] args) {
		PowerSystemSimulationManualTest test = new PowerSystemSimulationManualTest();
		
		test.initialize();
		test.go();
	}
	
	private void initialize(){
		simulation = new ElectricPowerSystemSimulation();
		sb = new StringBuilder();
		fmt = new Formatter(sb);
		
		new ModelInitializator().initialize(simulation);
	}
	
	public void go(){
		while(true){
			parameters = simulation.calculateNextStep();
			
			if(isItTimeToPrint()){
				printState();
			}
			
			pause();
		}
	}
	
	private boolean isItTimeToPrint(){
		
		if(INTERVAL_BETWEEN_PRINTS == 0){
			return true;
		}else if(counter % INTERVAL_BETWEEN_PRINTS == 0){
			counter = 1;
			return true;
		}else{
			counter ++;
			return false;
		}
	}
	
	private void printState(){
		fmt.format(
				"%12s"+ 
				", totalGeneration= %10f, totalConsumption= %10f ,frequency= %10f", 
				parameters.getCurrentTimeInSimulation(), parameters.getTotalGenerations(),
				parameters.getTotalConsumption(), parameters.getFrequencyInPowerSystem());
				
		System.out.println(sb.toString());
		sb.setLength(0);
	}
	
	private void pause(){
		if(PAUSE_BETWEEN_CALCULATING_STEPS_IN_MS != 0){
			try {
				Thread.sleep(PAUSE_BETWEEN_CALCULATING_STEPS_IN_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
