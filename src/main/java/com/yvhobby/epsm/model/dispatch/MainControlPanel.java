package main.java.com.yvhobby.epsm.model.dispatch;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration.LoadCurve;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;
import main.java.com.yvhobby.epsm.model.generation.Generator;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;

public class MainControlPanel {
	private ElectricPowerSystemSimulation simulation;
	private Dispatcher dispatcher;
	private PowerStation station;
	private PowerStationGenerationSchedule stationGenerationSchedule;
	private Timer stateReportTransferTimer;
	private Timer generatorControlTimer;
	private StationReportTaskTask stateTransferTask = new StationReportTaskTask();
	private GeneratorsControlTaskTask generatorControlTask = new GeneratorsControlTaskTask();
	
	public void performGenerationSchedule(PowerStationGenerationSchedule generationSchedule){
		this.stationGenerationSchedule = generationSchedule;
		generatorControlTimer = new Timer();
		generatorControlTimer.schedule(generatorControlTask, 0, TimeUnit.SECONDS.toMillis(1));
	}
	
	public void subscribeOnReports(){
		stateReportTransferTimer = new Timer();
		stateReportTransferTimer.schedule(stateTransferTask, 0,
				GlobalConstatnts.PAUSE_BETWEEN_STATE_REPORTS_TRANSFERS_IN_MILLISECONDS);
	}
	
	private class StationReportTaskTask extends TimerTask{
		private Set<GeneratorStateReport> generatorsReports = new HashSet<GeneratorStateReport>();
		
		@Override
		public void run(){
			processStateOfEveryGenerator();
		}
		
		private void processStateOfEveryGenerator(){
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
			dispatcher.acceptPowerStationStateReport(stationReport);
		}
	}

	private class GeneratorsControlTaskTask extends TimerTask{
		private Generator generator;
		private LoadCurve generationCurve;
		private boolean shouldGeneratorBeTurnedOn;
		private boolean shouldAstaticRegulationBeTurnedOn;
		
		@Override
		public void run() {
			processEveryGenerationSchedule();
		}
		
		private void processEveryGenerationSchedule(){
			for(Generator generator: station.getGenerators()){
				rememberCurrentGenerator(generator);
				GeneratorGenerationSchedule generatorSchedule = getGeneratorGenerationSchedule(generator);
				adjustGenerationAccordingToSchedule(generatorSchedule);
			}
		}

		private void rememberCurrentGenerator(Generator generator){
			this.generator = generator;
		}
		
		private GeneratorGenerationSchedule getGeneratorGenerationSchedule(Generator generator){
			int generatorId = generator.getId();
			return stationGenerationSchedule.getGeneratorGenerationSchedule(generatorId);
		}
		
		private void adjustGenerationAccordingToSchedule(GeneratorGenerationSchedule generatorSchedule){
			getNewGenerationParameters(generatorSchedule);
			adjustNecessaryParameters();
		}

		private void getNewGenerationParameters(GeneratorGenerationSchedule generatorSchedule){
			shouldGeneratorBeTurnedOn = generatorSchedule.isGeneratorTurnedOn();
			shouldAstaticRegulationBeTurnedOn = generatorSchedule.isAstaticRegulatorTurnedOn();
			generationCurve = generatorSchedule.getCurve();
		}
		
		private void adjustNecessaryParameters() {
			if(shouldGeneratorBeTurnedOn){
				vefifyAndTurnOnGenerator();
				adjustGeneration();
			}else{
				vefifyAndTurnOffGenerator();
			}
		}
		
		private void vefifyAndTurnOnGenerator(){
			if(! generator.isTurnedOn()){
				generator.turnOnGenerator();
			}
		}
		
		private void vefifyAndTurnOffGenerator() {
			if(generator.isTurnedOn()){
				generator.turnOffGenerator();
			}
		}
		
		private void adjustGeneration(){
			if(shouldAstaticRegulationBeTurnedOn){
				vefifyAndTurnOnAstaticRegulation();
			}else{
				vefifyAndTurnOffAstaticRegulation();
				adjustGenerationPower();
			}
		}
		
		private void vefifyAndTurnOnAstaticRegulation(){
			if(! generator.isAstaticRegulationTurnedOn()){
				generator.turnOnAstaticRegulation();
			}
		}
		
		private void vefifyAndTurnOffAstaticRegulation(){
			if(generator.isAstaticRegulationTurnedOn()){
				generator.turnOffAstaticRegulation();
			}
		}
		
		private void adjustGenerationPower(){
			LocalTime currentTime = simulation.getTime();
			float newGenerationPower = generationCurve.getPowerOnTimeInMW(currentTime);
			generator.setPowerAtRequiredFrequency(newGenerationPower);
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
