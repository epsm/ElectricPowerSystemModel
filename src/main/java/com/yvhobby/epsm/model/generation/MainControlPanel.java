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
	private TransferStationStateReportToDispatcherTask stateTransferTask = new TransferStationStateReportToDispatcherTask();
	private LocalTime lastMessageFromDispatcher;
	
	public void setGenerationSchedule(PowerStationGenerationSchedule generationSchedule){
		this.stationGenerationSchedule = generationSchedule;
	}
	
	public void sendReportsToDispatcher(){
		stateReportTransferTimer = new Timer();
		stateReportTransferTimer.schedule(
				stateTransferTask, GlobalConstatnts.PAUSE_BETWEEN_STATE_REPORTS_TRANSFERS_IN_MILLISECONDS);
	}
	
	private class TransferStationStateReportToDispatcherTask extends TimerTask{
		private Set<GeneratorStateReport> generatorsStates = new HashSet<GeneratorStateReport>();
		private PowerStationStateReport stationStateReport;
		private LocalTime currentTime;
		
		@Override
		public void run(){
			generatorsStates.clear();
			currentTime = simulation.getTime();
			int stationId = station.getId();
						
			for(Generator generator: station.getGenerators()){
				int generatorId = generator.getId();
				boolean isTurnedOn = generator.isTurnedOn();
				float generationInWM = generator.getGenerationInMW();
				GeneratorStateReport generatorStateReport = 
						new GeneratorStateReport(generatorId, isTurnedOn, generationInWM);
				generatorsStates.add(generatorStateReport);
			}
			
			stationStateReport = new PowerStationStateReport(
					stationId, currentTime,Collections.unmodifiableSet(generatorsStates));
			dispatcher.acceptPowerStationState(stationStateReport);
		}
	}

	private class SetPowerForGeneratorsTask extends TimerTask{
		private GeneratorGenerationSchedule generationSchedule;
		private ControlUnit controlUnit;
		private LoadCurve generationCurve;
		private LocalTime currentTime;
		private int generatorId;
		private float generationPower;
		
		
		@Override
		public void run() {
			for(Generator generator: station.getGenerators()){
				getGeneratorParameters(generator);
				getGenerationParametersOnCurrentTime();
				adjustGenerationParameters(generator);
			}
		}
		
		private void getGeneratorParameters(Generator generator){
			generatorId = generator.getId();
			controlUnit = generator.getControlUnit();
		}
		
		private void getGenerationParametersOnCurrentTime(){
			generationSchedule = stationGenerationSchedule.getGeneratorGenerationSchedule(generatorId);
			generationCurve = generationSchedule.getCurve();
			currentTime = simulation.getTime();
		}
		
		private void adjustGenerationParameters(Generator generator){
			if(generationSchedule.isGeneratorTurnedOn()){
				generator.turnOnGenerator();
			}else{
				generator.turnOffGenerator();
			}
			
			if(generationSchedule.isAstaticRegulatorTurnedOn()){
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
