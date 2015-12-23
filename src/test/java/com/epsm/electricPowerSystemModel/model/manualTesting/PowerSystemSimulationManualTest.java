package test.java.com.epsm.electricPowerSystemModel.model.manualTesting;

import main.java.com.epsm.electricPowerSystemModel.model.control.DefaultConfigurator;
import main.java.com.epsm.electricPowerSystemModel.model.control.SimulationRunner;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;

public class PowerSystemSimulationManualTest {
	private ElectricPowerSystemSimulation simulation = new ElectricPowerSystemSimulationImpl();
	private Dispatcher dispatcher = new DispatcherImpl();
	private DefaultConfigurator configurator = new DefaultConfigurator();
	private SimulationRunner runner = new SimulationRunner();
	
	public void start(){
		configurator.initialize(simulation, dispatcher);
		runner.runSimulation(simulation);
	}
	
	public static void main(String[] args) {
		new PowerSystemSimulationManualTest().start();
	}
}
