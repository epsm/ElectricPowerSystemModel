package com.epsm.electricPowerSystemModel.model.generation;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.electricPowerSystemModel.model.constantsForTests.TestsConstants;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

public class GeneratorControllerTest {
	private ElectricPowerSystemSimulation simulation;
	private MainControlPanel controlPanel;
	private PowerStationGenerationSchedule stationSchedule;
	private GeneratorGenerationSchedule genrationSchedule_1;
	private GeneratorGenerationSchedule genrationSchedule_2;
	private PowerStationParameters parameters;
	private PowerStation station;
	private LoadCurve generationCurve;
	private Generator generator_1;
	private Generator generator_2;
	private TimeService timeService;
	private Dispatcher dispatcher;
	
	@Before
	public void setUp(){
		stationSchedule = new PowerStationGenerationSchedule(0, LocalDateTime.MIN, LocalTime.MIN, 2);
		generationCurve = new LoadCurve(TestsConstants.LOAD_BY_HOURS);
		
		parameters = new PowerStationParameters(0, LocalDateTime.MIN, LocalTime.MIN, 2);
		GeneratorParameters parameter_1 = new GeneratorParameters(1, 100, 0);
		GeneratorParameters parameter_2 = new GeneratorParameters(2, 100, 0);
		parameters.addGeneratorParameters(parameter_1);
		parameters.addGeneratorParameters(parameter_2);
		
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		
		simulation = new ElectricPowerSystemSimulationImpl(timeService, dispatcher);
		station = new PowerStation(simulation, timeService, dispatcher, parameters);
		controlPanel = new MainControlPanel(simulation, station);
		
		generator_1 = spy( new Generator(simulation, 1));
		generator_2 = spy( new Generator(simulation, 2));
		generator_1.setNominalPowerInMW(200);
		generator_2.setNominalPowerInMW(200);
		
		station.addGenerator(generator_1);
		station.addGenerator(generator_2);
	}
	
	@Test
	public void mainControlPanelTurnsOnGeneratorIfItIsScheduledIndependentAreTheyTurnedOnOrOff()
			throws InterruptedException{
		prepareGenerationScheduleWithTurnedOnGenerators();
		turnOnfirstAndTurnOffsecondGenerators();
		doNextStep();

		Assert.assertTrue(generator_1.isTurnedOn());
		Assert.assertTrue(generator_2.isTurnedOn());
	}
	
	private void doNextStep(){
		controlPanel.acceptGenerationSchedule(stationSchedule);
		controlPanel.adjustGenerators();
	}
	
	private void prepareGenerationScheduleWithTurnedOnGenerators(){
		genrationSchedule_1 = new GeneratorGenerationSchedule(1, true, false, generationCurve);
		genrationSchedule_2 = new GeneratorGenerationSchedule(2, true, false, generationCurve);
		stationSchedule.addGeneratorSchedule(genrationSchedule_1);
		stationSchedule.addGeneratorSchedule(genrationSchedule_2);
	}
	
	private void turnOnfirstAndTurnOffsecondGenerators(){
		generator_1.turnOnGenerator();
		generator_2.turnOffGenerator();
	}
	
	@Test
	public void mainControlPanelTurnsOffGeneratorIfItIsScheduledIndependentAreTheyTurnedOnOrOff()
			throws InterruptedException{
		prepareGenerationScheduleWithTwoTurnedOffGenerators();
		turnOnfirstAndTurnOffsecondGenerators();
		doNextStep();
		
		Assert.assertFalse(generator_1.isTurnedOn());
		Assert.assertFalse(generator_2.isTurnedOn());
	}
	
	private void prepareGenerationScheduleWithTwoTurnedOffGenerators(){
		genrationSchedule_1 = new GeneratorGenerationSchedule(1, false, false, generationCurve);
		genrationSchedule_2 = new GeneratorGenerationSchedule(2, false, false, generationCurve);
		stationSchedule.addGeneratorSchedule(genrationSchedule_1);
		stationSchedule.addGeneratorSchedule(genrationSchedule_2);
	}
	
	@Test
	public void mainAstaticRegulationWillBeTurnedOnIfItIsScheduledIndependentIsItTurnedOnOrOff() 
			throws InterruptedException{
		prepareGenerationScheduleWithTurnedOnGeneratorsAndTurnedOnAstaticRegulation();
		turnOnBothGenerators();
		turnOnAstaticRegulationOnfirstAndTurnOffItOnSecondGenerators();
		doNextStep();

		Assert.assertTrue(generator_1.isAstaticRegulationTurnedOn());
		Assert.assertTrue(generator_2.isAstaticRegulationTurnedOn());
	}
	
	private void prepareGenerationScheduleWithTurnedOnGeneratorsAndTurnedOnAstaticRegulation(){
		genrationSchedule_1 = new GeneratorGenerationSchedule(1, true, true, generationCurve);
		genrationSchedule_2 = new GeneratorGenerationSchedule(2, true, true, generationCurve);
		stationSchedule.addGeneratorSchedule(genrationSchedule_1);
		stationSchedule.addGeneratorSchedule(genrationSchedule_2);
	}
	
	private void turnOnBothGenerators(){
		generator_1.turnOnGenerator();
		generator_2.turnOnGenerator();
	}
	
	private void turnOnAstaticRegulationOnfirstAndTurnOffItOnSecondGenerators(){
		generator_1.turnOnAstaticRegulation();
		generator_2.turnOffAstaticRegulation();
	}
	
	@Test
	public void mainAstaticRegulationWillBeTurnedOffIfItIsScheduledIndependentIsItTurnedOnOrOff() 
			throws InterruptedException{
		prepareGenerationScheduleWithTurnedOnGeneratorsAndTurnedOffAstaticRegulation();
		turnOnBothGenerators();
		turnOnAstaticRegulationOnfirstAndTurnOffItOnSecondGenerators();
		doNextStep();

		Assert.assertFalse(generator_1.isAstaticRegulationTurnedOn());
		Assert.assertFalse(generator_2.isAstaticRegulationTurnedOn());
	}
	
	private void prepareGenerationScheduleWithTurnedOnGeneratorsAndTurnedOffAstaticRegulation(){
		genrationSchedule_1 = new GeneratorGenerationSchedule(1, true, false, generationCurve);
		genrationSchedule_2 = new GeneratorGenerationSchedule(2, true, false, generationCurve);
		stationSchedule.addGeneratorSchedule(genrationSchedule_1);
		stationSchedule.addGeneratorSchedule(genrationSchedule_2);
	}
	
	@Test
	public void controlsGeneratorsGenerationConformToScheduledWhenGeneratorsOnAndAstaticRegulationTurnedOff() 
			throws InterruptedException{
		prepareGenerationScheduleWithTurnedOnGeneratorsAndTurnedOffAstaticRegulation();
		prepareTurnedOnGeneratorsWithTurnedOffAstaticRegulation();
		doNextStep();
		isAdjustedGenerationsOfGeneratorsConformsScheduled();
	}
	
	private void prepareTurnedOnGeneratorsWithTurnedOffAstaticRegulation(){
		generator_1.turnOffAstaticRegulation();
		generator_2.turnOffAstaticRegulation();
	}
	
	private void isAdjustedGenerationsOfGeneratorsConformsScheduled(){
		LocalTime timeInSimulation = simulation.getTimeInSimulation(); 
		float expectedGenerations = generationCurve.getPowerOnTimeInMW(timeInSimulation);
		float firstGeneratorGeneration = generator_1.getPowerAtRequiredFrequency();
		float secondGeneratorGeneration = generator_2.getPowerAtRequiredFrequency();
		
		Assert.assertEquals(expectedGenerations, firstGeneratorGeneration, 0);
		Assert.assertEquals(expectedGenerations, secondGeneratorGeneration, 0);
	}
	
	@Test
	public void mainControlPanelControlsGeneration() throws InterruptedException{
		prepareGenerationScheduleWithAnyAllowableParameters();
		doNextStep();
		hasGeneratorsWereAdjusted();
	}
	
	private void prepareGenerationScheduleWithAnyAllowableParameters(){
		prepareGenerationScheduleWithTurnedOnGeneratorsAndTurnedOffAstaticRegulation();
		prepareTurnedOnGeneratorsWithTurnedOffAstaticRegulation();
	}
	
	private void hasGeneratorsWereAdjusted(){
		verify(generator_1, atLeastOnce()).isTurnedOn();
		verify(generator_2, atLeastOnce()).isTurnedOn();
	}
}
