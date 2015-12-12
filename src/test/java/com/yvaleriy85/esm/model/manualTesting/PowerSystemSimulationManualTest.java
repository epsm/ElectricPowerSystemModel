package test.java.com.yvaleriy85.esm.model.manualTesting;

import java.util.Formatter;

import main.java.com.yvaleriy85.esm.model.generalModel.ElectricPowerSystemSimulationImpl;
import main.java.com.yvaleriy85.esm.model.generalModel.SimulationParameters;


public class PowerSystemSimulationManualTest {
	private Formatter fmt;
	private ElectricPowerSystemSimulationImpl powerSystemsimulation;
	private SimulationParameters parameters;
	private StringBuilder sb;
	private int counter;
	private final int INTERVAL_BETWEEN_PRINTS = 100;
	private final int PAUSE_BETWEEN_CALCULATING_STEPS_IN_MS = 0;
	
	public static void main(String[] args) {
		PowerSystemSimulationManualTest test = new PowerSystemSimulationManualTest();
		
		test.initialize();
		test.go();
	}
	
	private void initialize(){
		powerSystemsimulation = new ElectricPowerSystemSimulationImpl();
		sb = new StringBuilder();
		fmt = new Formatter(sb);
		
		new ModelInitializator().initialize(powerSystemsimulation);
	}
	
	public void go(){
		while(true){
			parameters = powerSystemsimulation.calculateNextStep();
			
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
