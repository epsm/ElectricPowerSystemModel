package main.java.com.yvhobby.epsm.model.generation;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration.LoadCurve;
import main.java.com.yvhobby.epsm.model.dispatch.Dispatcher;
import main.java.com.yvhobby.epsm.model.dispatch.GeneratorGenerationSchedule;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationGenerationSchedule;
import main.java.com.yvhobby.epsm.model.dispatch.GeneratorStateReport;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationStateReport;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;

public class MainControlPanel {
	private ElectricPowerSystemSimulation simulation;
	private Dispatcher dispatcher;
	private PowerStation station;
	private PowerStationGenerationSchedule stationGenerationSchedule;
	private Timer stateReportTransferTimer;
	private TransferStationStateReportToDispatcherTask stateTransferTask = 
			new TransferStationStateReportToDispatcherTask();
	private LocalTime lastMessageFromDispatcher;
	
	public void setGenerationSchedule(PowerStationGenerationSchedule generationSchedule){
		this.stationGenerationSchedule = generationSchedule;
	}
	
	public void subscribeOnReports(){
		stateReportTransferTimer = new Timer();
		stateReportTransferTimer.schedule(
				stateTransferTask, GlobalConstatnts.PAUSE_BETWEEN_STATE_REPORTS_TRANSFERS_IN_MILLISECONDS);
	}
	
	private class TransferStationStateReportToDispatcherTask extends TimerTask{
		private Set<GeneratorStateReport> generatorsReports = new HashSet<GeneratorStateReport>();
		
		@Override
		public void run(){
			clearPreviousGeneratorsReports();
			prepareGeneratorsStatesReports();
			PowerStationStateReport stationReport = prepareStationStateReport();
			sendStationReport(stationReport);
		}
		
		private void clearPreviousGeneratorsReports(){
			generatorsReports.clear();
		}
		
		private void prepareGeneratorsStatesReports(){
			for(Generator generator: station.getGenerators()){
				GeneratorStateReport generatorReport = prepareGeneratorStateReport(generator);
				addGeneratorStateReportToGeneratorsStatesReport(generatorReport);
			}
		}
		
		private GeneratorStateReport prepareGeneratorStateReport(Generator generator){
			int generatorId = generator.getId();
			boolean isTurnedOn = generator.isTurnedOn();
			float generationInWM = generator.getGenerationInMW();
			
			return new GeneratorStateReport(generatorId, isTurnedOn, generationInWM);
		}
		
		private void addGeneratorStateReportToGeneratorsStatesReport(GeneratorStateReport report){
			generatorsReports.add(report);
		}
		
		private PowerStationStateReport prepareStationStateReport(){
			int stationId = station.getId();
			LocalTime currentTime = simulation.getTime();
			
			return new PowerStationStateReport(stationId, currentTime, generatorsReports);
		}
		
		private void sendStationReport(PowerStationStateReport stationReport){
			dispatcher.acceptPowerStationState(stationReport);
		}
	}

	private class SetGenerationForGeneratorsAccordingToScheduleTask extends TimerTask{
		private GeneratorGenerationSchedule generatorGenerationSchedule;
		private ControlUnit controlUnit;
		private LoadCurve generationCurve;
		private LocalTime currentTime;
		private int generatorId;
		private float generationPower;
		
		@Override
		public void run() {
			for(Generator generator: station.getGenerators()){
				generatorId = generator.getId();
				getGenerationParametersForCurrentTime();
				adjustGenerationParameters(generator);
			}
		}
		
		private void getGenerationParametersForCurrentTime(int generatorId){
			generatorGenerationSchedule = 
					stationGenerationSchedule.getGeneratorGenerationSchedule(generatorId);
			generationCurve = generatorGenerationSchedule.getCurve();
			currentTime = simulation.getTime();
		}
		
		private void adjustGenerationParameters(Generator generator){
			if(generatorGenerationSchedule.isGeneratorTurnedOn()){
				generator.turnOnGenerator();
			}else{
				generator.turnOffGenerator();
			}
			
			if(generatorGenerationSchedule.isAstaticRegulatorTurnedOn()){
				generator.turnOnAstaticRegulation();
				return;
			}else{
				generator.turnOffAstaticRegulation();
			}
			
			generationPower = generationCurve.getPowerOnTimeInMW(currentTime);
			
			controlUnit.setPowerAtRequiredFrequency(generationPower);
		}
	}
	
	public void setSimulation(ElectricPowerSystemSimulation simulation) {
		this.simulation = simulation;
	}

	public void setStation(PowerStation station) {
		this.station = station;
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
}
