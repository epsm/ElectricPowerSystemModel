package main.java.com.epsm.electricPowerSystemModel.model.control;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.Report;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSender;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSenderSource;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSource;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.SimulationReport;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;

public class SimulationRunner implements ReportSource, ReportSenderSource{
	private ElectricPowerSystemSimulationImpl simulation;
	private ReportSender sender;
	private SimulationReport report;
	private Logger logger = (Logger) LoggerFactory.getLogger(SimulationRunner.class);
	private final int PAUSE_BETWEEN_CALCULATING_STEPS_IN_MS = 100;
	
	public void initializeAndRun(Dispatcher dispatcher){
		createPowerSystemSimulation();
		configureSystemByDefault(dispatcher);
		runSimulation();
		
		sender = new ReportSender(this);
		
		logger.info("Simulation was created and run");
	}
	
	private void createPowerSystemSimulation(){
		simulation = new ElectricPowerSystemSimulationImpl();
	}
	
	private void configureSystemByDefault(Dispatcher dispatcher){
		new DefaultConfigurator().initialize(simulation, dispatcher);
	}
	
	private void runSimulation(){
		Thread.currentThread().setName("Simulation");
		
		while(true){
			report = simulation.calculateNextStep();
			pause();
		}
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
	
	@Override
	public Report getReport(){
		return report;
	}

	@Override
	public void subscribeOnReports(){
		sender.sendReports();
	}

	@Override
	public void setReportSender(ReportSender sender) {
		this.sender = sender;
	}
}
