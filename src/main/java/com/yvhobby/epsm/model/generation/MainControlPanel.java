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
		private Generator generator;
		private LoadCurve generationCurve;
		private boolean generatorTurnedOn;
		private boolean astaticRegulationTurnedOn;
		
		@Override
		public void run() {
			for(Generator generator: station.getGenerators()){
				GeneratorGenerationSchedule generatorSchedule = getGeneratorGenerationSchedule(generator);
				adjustGenerationAccordingToSchedule(generatorSchedule);
			}
		}

		private GeneratorGenerationSchedule getGeneratorGenerationSchedule(Generator generator){
			int generatorId = generator.getId();
			return stationGenerationSchedule.getGeneratorGenerationSchedule(generatorId);
		}
		
		private void adjustGenerationAccordingToSchedule(GeneratorGenerationSchedule generatorSchedule){
			getGenerationParametersForCurrentTime(generatorSchedule);
			adjustNecessaryParameters();
		}

		private void getGenerationParametersForCurrentTime(GeneratorGenerationSchedule generatorSchedule){
			generatorTurnedOn = generatorSchedule.isGeneratorTurnedOn();
			astaticRegulationTurnedOn = generatorSchedule.isAstaticRegulatorTurnedOn();
			generationCurve = generatorSchedule.getCurve();
		}
		
		private void adjustNecessaryParameters() {
			if(generatorTurnedOn){
				vefifyAndTurnOnGenerator();
			}
		}
		
		private void vefifyAndTurnOnGenerator() {
			// TODO Auto-generated method stub
			
		}

		private void adjustGenerationParameters(Generator generator){
			if(generatorGenerationSchedule.isGeneratorTurnedOn() && !generator.isTurnedOn()){
				generator.turnOnGenerator();
			}else{
				if(generator.isTurnedOn()){
					generator.turnOffGenerator();
				}
				return;
			}
			
			
			
			adjustGenerationPower();
		}
		
		private void adjustAstaticRegulation(){
			if(generatorGenerationSchedule.isAstaticRegulatorTurnedOn() && 
					!generator.isAstaticRegulationTurnedOn()){
				generator.turnOnAstaticRegulation();
				return;
			}else if(generator.isAstaticRegulationTurnedOn()){
				generator.turnOffAstaticRegulation();
			}
		}
		
		private void adjustGenerationPower(){
			float generationPower = generationCurve.getPowerOnTimeInMW(currentTime);
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
