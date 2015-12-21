package main.java.com.yvhobby.epsm.model.dispatch;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration.LoadCurve;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;
import main.java.com.yvhobby.epsm.model.generation.Generator;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;
import main.java.com.yvhobby.epsm.model.generation.PowerStationException;

public class MainControlPanel {
	private ElectricPowerSystemSimulation simulation;
	private Dispatcher dispatcher;
	private PowerStation station;
	private PowerStationGenerationSchedule curentSchedule;
	private PowerStationGenerationSchedule receivedSchedule;
	private PowerStationParameters parameters;
	private Timer stateReportTransferTimer;
	private Timer generatorControlTimer;
	private StationReportTaskTask stateTransferTask = new StationReportTaskTask();
	private GeneratorsControlTaskTask generatorControlTask = new GeneratorsControlTaskTask();
	private GenerationScheduleValidator validator = new GenerationScheduleValidator();
	private Logger logger = (Logger) LoggerFactory.getLogger(MainControlPanel.class);
	
	public void performGenerationSchedule(PowerStationGenerationSchedule generationSchedule){
		receivedSchedule = generationSchedule;
		
		getStationParameters();
		
		if(isReceivedScheduleValid()){
			replaceCurrentSchedule();
			executeSchedule();
		}else if(isThereValidSchedule()){
			executeSchedule();
		}
	}
	
	private void getStationParameters(){
		if(parameters == null){
			parameters = station.getPowerStationParameters();
		}
	}
	
	private boolean isReceivedScheduleValid(){
		try{
			validator.validate(receivedSchedule, parameters);
			logger.info("Schedule was received.");
			return true;
		}catch (PowerStationException exception){
			//TODO send request to dispatcher
			logger.warn("Received ", exception);
			return false;
		}
	}
	
	private void replaceCurrentSchedule(){
		curentSchedule = receivedSchedule;
	}
	
	private void executeSchedule(){
		generatorControlTimer = new Timer();
		generatorControlTimer.schedule(generatorControlTask, 0, TimeUnit.SECONDS.toMillis(1));
	}
	
	private boolean isThereValidSchedule(){
		return curentSchedule != null;
	}
	
	public void subscribeOnReports(){
		stateReportTransferTimer = new Timer();
		stateReportTransferTimer.schedule(stateTransferTask, 0,
				GlobalConstatnts.PAUSE_BETWEEN_STATE_REPORTS_TRANSFERS_IN_MILLISECONDS);
		
		logger.info("Station will be sending reports to diapatcher.");
	}
	
	private class StationReportTaskTask extends TimerTask{
		private Set<GeneratorStateReport> generatorsReports = new TreeSet<GeneratorStateReport>();
		
		@Override
		public void run(){
			setNameToThread();
			processStateOfEveryGenerator();
		}
		
		private void setNameToThread(){
			Thread.currentThread().setName("report timer");
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
			Collection<Integer> generatorNumbers = station.getGeneratorsNumbers();
			for(Integer generatorNumber: generatorNumbers){
				Generator generator = station.getGenerator(generatorNumber);
				GeneratorStateReport generatorReport = prepareGeneratorStateReport(generator);
				addGeneratorStateReportToGeneratorsStatesReport(generatorReport);
			}
		}
		
		private GeneratorStateReport prepareGeneratorStateReport(Generator generator){
			int generatorNumber = generator.getNumber();
			boolean isTurnedOn = generator.isTurnedOn();
			float generationInWM = generator.getGenerationInMW();
			
			return new GeneratorStateReport(generatorNumber, isTurnedOn, generationInWM);
		}
		
		private void addGeneratorStateReportToGeneratorsStatesReport(GeneratorStateReport report){
			generatorsReports.add(report);
		}
		
		private PowerStationStateReport prepareStationStateReport(){
			int stationNumber = station.getNumber();
			LocalTime currentTime = simulation.getTime();
			
			return new PowerStationStateReport(stationNumber, currentTime, generatorsReports);
		}
		
		private void sendStationReport(PowerStationStateReport stationReport){
			dispatcher.acceptPowerStationStateReport(stationReport);
			
			logger.info("Station sent report to dispatcher: {}", stationReport);
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
			Collection<Integer> generatorNumbers = station.getGeneratorsNumbers();
			for(Integer generatorNumber: generatorNumbers){
				Generator generator = station.getGenerator(generatorNumber);
				rememberCurrentGenerator(generator);
				GeneratorGenerationSchedule generatorSchedule = getGeneratorGenerationSchedule(generator);
				adjustGenerationAccordingToSchedule(generatorSchedule);
			}
		}

		private void rememberCurrentGenerator(Generator generator){
			this.generator = generator;
		}
		
		private GeneratorGenerationSchedule getGeneratorGenerationSchedule(Generator generator){
			int generatorNumber = generator.getNumber();
			return curentSchedule.getGeneratorGenerationSchedule(generatorNumber);
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
