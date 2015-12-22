package main.java.com.yvhobby.epsm.model.control;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import main.java.com.yvhobby.epsm.model.dispatch.Dispatcher;
import main.java.com.yvhobby.epsm.model.dispatch.GeneratorStateReport;
import main.java.com.yvhobby.epsm.model.dispatch.MainControlPanel;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationStateReport;
import main.java.com.yvhobby.epsm.model.dispatch.SimulationReport;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulationImpl;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;
import main.java.com.yvhobby.epsm.model.generation.Generator;

public class SimulationRunner {
	/*private ElectricPowerSystemSimulationImpl simulation;
	private SimulationReport report;
	private Dispatcher dispatcher;
	private Timer stateReportTransferTimer;
	private SimulationReportTaskTask stateTransferTask = new SimulationReportTaskTask();
	private Logger logger = (Logger) LoggerFactory.getLogger(SimulationRunner.class);
	private final int PAUSE_BETWEEN_CALCULATING_STEPS_IN_MS = 100;
	
	public void initializeAndRun(Dispatcher dispatcher){
		createPowerSystemSimulation();
		configureSystemByDefault(dispatcher);
		runSimulation();
		
		logger.info("Simulation was created and run");
	}
	
	private void createPowerSystemSimulation(){
		simulation = new ElectricPowerSystemSimulationImpl();
	}
	
	private void configureSystemByDefault(Dispatcher dispatcher){
		new DefaultConfigurator().initialize(simulation, dispatcher);
	}
	
	private void runSimulation(){
		while(true){
			parameters = simulation.calculateNextStep();
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
	
	public void subscribeOnReports(){
		stateReportTransferTimer = new Timer();
		stateReportTransferTimer.schedule(stateTransferTask, 0,
				GlobalConstatnts.PAUSE_BETWEEN_STATE_REPORTS_TRANSFERS_IN_MILLISECONDS);
		
		logger.info("Station will be sending reports to diapatcher.");
	}
	
	/*private class SimulationReportTaskTask extends TimerTask{
		private Set<GeneratorStateReport> generatorsReports = new TreeSet<GeneratorStateReport>();
		
		@Override
		public void run(){
			setNameToThread();
			sendStationReport
		}
		
		private void setNameToThread(){
			Thread.currentThread().setName("simulation report timer");
		}
		
		
		private void sendStationReport(){
			dispatcher.acceptPowerStationStateReport(stationReport);
			
			logger.info("Simulation sent report to dispatcher: {}", parameters);
		}
	}*/
}
