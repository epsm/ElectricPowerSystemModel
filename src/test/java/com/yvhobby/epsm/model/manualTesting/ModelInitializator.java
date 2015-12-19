package test.java.com.yvhobby.epsm.model.manualTesting;

import main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration.LoadCurve;
import main.java.com.yvhobby.epsm.model.consumption.PowerConsumerWithScheduledLoad;
import main.java.com.yvhobby.epsm.model.consumption.PowerConsumerWithShockLoad;
import main.java.com.yvhobby.epsm.model.dispatch.Dispatcher;
import main.java.com.yvhobby.epsm.model.dispatch.GeneratorGenerationSchedule;
import main.java.com.yvhobby.epsm.model.dispatch.MainControlPanel;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationGenerationSchedule;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulationImpl;
import main.java.com.yvhobby.epsm.model.generation.AstaticRegulationUnit;
import main.java.com.yvhobby.epsm.model.generation.ControlUnit;
import main.java.com.yvhobby.epsm.model.generation.Generator;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;
import test.java.com.yvhobby.epsm.model.constantsForTests.TestsConstants;

public class ModelInitializator {
	private ElectricPowerSystemSimulationImpl simulation;
	private Dispatcher dispatcher;
	private MainControlPanel controlPanel;
	private PowerStationGenerationSchedule generationSchedule;
	
	public void initialize(ElectricPowerSystemSimulationImpl powerSystem, DispatcherTestImpl dispatcher){
		this.simulation = powerSystem;
		this.dispatcher = dispatcher;
		createAndBoundObjects();
		turnOnPowerStationAutoControl();
		subscribeDispatcherOnStationReports();
	}
	
	private void createAndBoundObjects(){
		createConsumerAndAddItToEnergySystem();
		createPowerStationAndAddToEnergySystem();
		createGenerationSchedule();
	}
	
	private void createConsumerAndAddItToEnergySystem(){
		float[] pattern = TestsConstants.LOAD_BY_HOURS;
		
		//first(shock)
		PowerConsumerWithShockLoad powerConsumer = new PowerConsumerWithShockLoad();
		powerConsumer.setDegreeOfDependingOnFrequency(2);
		powerConsumer.setMaxLoad(10f);
		powerConsumer.setMaxWorkDurationInSeconds(300);
		powerConsumer.setMaxPauseBetweenWorkInSeconds(200);
		powerConsumer.setElectricalPowerSystemSimulation(simulation);
		
		//second(scheduled)
		PowerConsumerWithScheduledLoad powerConsumer_2 = new PowerConsumerWithScheduledLoad();
		powerConsumer_2.setDegreeOfDependingOnFrequency(2);
		powerConsumer_2.setApproximateLoadByHoursOnDayInPercent(pattern);
		powerConsumer_2.setMaxLoadWithoutRandomInMW(100);
		powerConsumer_2.setRandomFluctuationsInPercent(10);
		powerConsumer_2.setElectricalPowerSystemSimulation(simulation);

		//adding
		simulation.addPowerConsumer(powerConsumer);
		simulation.addPowerConsumer(powerConsumer_2);
	}
	
	private void createPowerStationAndAddToEnergySystem(){
		PowerStation powerStation = new PowerStation(1);
		simulation.addPowerStation(powerStation);
		
		controlPanel = new MainControlPanel();
		controlPanel.setSimulation(simulation);
		controlPanel.setStation(powerStation);
		controlPanel.setDispatcher(dispatcher);
		
		//first(astatic)
		Generator generator_1 = new Generator(1);
		AstaticRegulationUnit regulationUnit_1 = new AstaticRegulationUnit(simulation, generator_1);
		ControlUnit controlUnit_1 = new ControlUnit(simulation, generator_1);
		
		generator_1.setAstaticRegulationUnit(regulationUnit_1);
		generator_1.setControlUnit(controlUnit_1);
		generator_1.setMinimalTechnologyPower(1);
		generator_1.setNominalPowerInMW(20);
		
		//second(static)
		Generator generator_2 = new Generator(2);
		AstaticRegulationUnit regulationUnit_2 = new AstaticRegulationUnit(simulation, generator_2);
		ControlUnit controlUnit_2 = new ControlUnit(simulation, generator_2);
		
		controlUnit_2.setPowerAtRequiredFrequency(30);
		
		generator_2.setAstaticRegulationUnit(regulationUnit_2);
		generator_2.setControlUnit(controlUnit_2);
		generator_2.setMinimalTechnologyPower(25);
		generator_2.setNominalPowerInMW(100);
		
		//adding
		powerStation.addGenerator(generator_1);
		powerStation.addGenerator(generator_2);
	}
	
	private void createGenerationSchedule(){
		LoadCurve generationCurve = null;
		GeneratorGenerationSchedule genrationSchedule_1 = null;
		GeneratorGenerationSchedule genrationSchedule_2 = null;
		
		generationSchedule = new PowerStationGenerationSchedule();
		generationCurve = new LoadCurve(TestsConstants.LOAD_BY_HOURS);
		genrationSchedule_1 = new GeneratorGenerationSchedule(1, true, true, null);
		genrationSchedule_2 = new GeneratorGenerationSchedule(2, true, false, generationCurve);
		generationSchedule.addGeneratorGenerationSchedule(genrationSchedule_1);
		generationSchedule.addGeneratorGenerationSchedule(genrationSchedule_2);
	}
	
	private void turnOnPowerStationAutoControl(){
		controlPanel.performGenerationSchedule(generationSchedule);
	}
	
	private void subscribeDispatcherOnStationReports(){
		controlPanel.subscribeOnReports();
	}
}
