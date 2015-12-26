package com.epsm.electricPowerSystemModel.model.dispatch;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import com.epsm.electricPowerSystemModel.model.dispatch.GeneratorGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.dispatch.MainControlPanel;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerStationGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.electricPowerSystemModel.model.generation.Generator;
import com.epsm.electricPowerSystemModel.model.generation.PowerStation;
import com.epsm.electricPowerSystemModel.model.generation.StaticRegulator;
import com.epsm.electricPowerSystemModel.model.constantsForTests.TestsConstants;

public class MainControlPanelTest {
	private ElectricPowerSystemSimulation simulation;
	private MainControlPanel stationControlPanel;
	private PowerStationGenerationSchedule stationGenerationSchedule;
	private GeneratorGenerationSchedule genrationSchedule_1;
	private GeneratorGenerationSchedule genrationSchedule_2;
	private LoadCurve generationCurve;
	private Generator generator_1;
	private Generator generator_2;
	private LocalTime CONSTANT_TIME_IN_MOCK_SIMULATION = LocalTime.NOON;
	
	@Before
	public void initialize(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		stationControlPanel = new MainControlPanel();
		stationGenerationSchedule = new PowerStationGenerationSchedule(1);
		PowerStation station = new PowerStation(1, simulation);
		generationCurve = new LoadCurve(TestsConstants.LOAD_BY_HOURS);
		Generator g_1 = new Generator(1);
		Generator g_2 = new Generator(2);
		generator_1 = spy(g_1);
		generator_2 = spy(g_2);
		StaticRegulator controlUnit_1 = new StaticRegulator(simulation, generator_1);
		StaticRegulator controlUnit_2 = new StaticRegulator(simulation, generator_2);
		
		when(simulation.getTime()).thenReturn(CONSTANT_TIME_IN_MOCK_SIMULATION);
		
		generator_1.setNominalPowerInMW(200);
		generator_2.setNominalPowerInMW(200);
		generator_1.setStaticRegulator(controlUnit_1);
		generator_2.setStaticRegulator(controlUnit_2);
		station.addGenerator(generator_1);
		station.addGenerator(generator_2);
		stationControlPanel.setSimulation(simulation);
		stationControlPanel.setStation(station);
	}
	
	@Test
	public void mainControlPanelTurnsOnGeneratorIfItIsScheduledIndependentAreTheyTurnedOnOrOff()
			throws InterruptedException{
		prepareGenerationScheduleWithTurnedOnGenerators();
		turnOnfirstAndTurnOffsecondGenerators();
		stationControlPanel.performGenerationSchedule(stationGenerationSchedule);
		doPauseUntilMainControlPanellAdjustGenerators();

		Assert.assertTrue(generator_1.isTurnedOn());
		Assert.assertTrue(generator_2.isTurnedOn());
	}
	
	private void prepareGenerationScheduleWithTurnedOnGenerators(){
		genrationSchedule_1 = new GeneratorGenerationSchedule(1, true, false, generationCurve);
		genrationSchedule_2 = new GeneratorGenerationSchedule(2, true, false, generationCurve);
		fillStationGenerationSchedule(genrationSchedule_1, genrationSchedule_2);
	}
	
	private void fillStationGenerationSchedule(
			GeneratorGenerationSchedule genrationSchedule_1, GeneratorGenerationSchedule genrationSchedule_2){
		stationGenerationSchedule.addGeneratorGenerationSchedule(genrationSchedule_1);
		stationGenerationSchedule.addGeneratorGenerationSchedule(genrationSchedule_2);
	}
	
	private void turnOnfirstAndTurnOffsecondGenerators(){
		generator_1.turnOnGenerator();
		generator_2.turnOffGenerator();
	}
	
	private void doPauseUntilMainControlPanellAdjustGenerators() throws InterruptedException{
		Thread.sleep(2000);//too many because test will be failed under maven test otherwise.
	}
	
	@Test
	public void mainControlPanelTurnsOffGeneratorIfItIsScheduledIndependentAreTheyTurnedOnOrOff()
			throws InterruptedException{
		prepareGenerationScheduleWithTwoTurnedOffGenerators();
		turnOnfirstAndTurnOffsecondGenerators();
		stationControlPanel.performGenerationSchedule(stationGenerationSchedule);
		doPauseUntilMainControlPanellAdjustGenerators();
		
		Assert.assertFalse(generator_1.isTurnedOn());
		Assert.assertFalse(generator_2.isTurnedOn());
	}
	
	private void prepareGenerationScheduleWithTwoTurnedOffGenerators(){
		genrationSchedule_1 = new GeneratorGenerationSchedule(1, false, false, generationCurve);
		genrationSchedule_2 = new GeneratorGenerationSchedule(2, false, false, generationCurve);
		fillStationGenerationSchedule(genrationSchedule_1, genrationSchedule_2);
	}
	
	@Test
	public void mainAstaticRegulationWillBeTurnedOnIfItIsScheduledIndependentIsItTurnedOnOrOff() 
			throws InterruptedException{
		prepareGenerationScheduleWithTurnedOnGeneratorsAndTurnedOnAstaticRegulation();
		turnOnBothGenerators();
		turnOnAstaticRegulationOnfirstAndTurnOffItOnSecondGenerators();
		stationControlPanel.performGenerationSchedule(stationGenerationSchedule);
		doPauseUntilMainControlPanellAdjustGenerators();

		Assert.assertTrue(generator_1.isAstaticRegulationTurnedOn());
		Assert.assertTrue(generator_2.isAstaticRegulationTurnedOn());
	}
	
	private void prepareGenerationScheduleWithTurnedOnGeneratorsAndTurnedOnAstaticRegulation(){
		genrationSchedule_1 = new GeneratorGenerationSchedule(1, true, true, generationCurve);
		genrationSchedule_2 = new GeneratorGenerationSchedule(2, true, true, generationCurve);
		fillStationGenerationSchedule(genrationSchedule_1, genrationSchedule_2);;
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
		stationControlPanel.performGenerationSchedule(stationGenerationSchedule);
		doPauseUntilMainControlPanellAdjustGenerators();

		Assert.assertFalse(generator_1.isAstaticRegulationTurnedOn());
		Assert.assertFalse(generator_2.isAstaticRegulationTurnedOn());
	}
	
	private void prepareGenerationScheduleWithTurnedOnGeneratorsAndTurnedOffAstaticRegulation(){
		genrationSchedule_1 = new GeneratorGenerationSchedule(1, true, false, generationCurve);
		genrationSchedule_2 = new GeneratorGenerationSchedule(2, true, false, generationCurve);
		fillStationGenerationSchedule(genrationSchedule_1, genrationSchedule_2);;
	}
	
	@Test
	public void controlsGeneratorsGenerationConformToScheduledWhenGeneratorsOnAndAstaticRegulationTurnedOff() 
			throws InterruptedException{
		prepareGenerationScheduleWithTurnedOnGeneratorsAndTurnedOffAstaticRegulation();
		prepareTurnedOnGeneratorsWithTurnedOffAstaticRegulation();
		stationControlPanel.performGenerationSchedule(stationGenerationSchedule);
		doPauseUntilMainControlPanellAdjustGenerators();
		isAdjustedGenerationsOfGeneratorsConformsScheduled();
	}
	
	private void prepareTurnedOnGeneratorsWithTurnedOffAstaticRegulation(){
		generator_1.turnOffAstaticRegulation();
		generator_2.turnOffAstaticRegulation();
	}
	
	private void isAdjustedGenerationsOfGeneratorsConformsScheduled(){
		float expectedGenerations = generationCurve.getPowerOnTimeInMW(CONSTANT_TIME_IN_MOCK_SIMULATION);
		float firstGeneratorGeneration = generator_1.getPowerAtRequiredFrequency();
		float secondGeneratorGeneration = generator_2.getPowerAtRequiredFrequency();
		
		Assert.assertEquals(expectedGenerations, firstGeneratorGeneration, 0);
		Assert.assertEquals(expectedGenerations, secondGeneratorGeneration, 0);
	}
	
	@Test
	public void mainControlPanelControlsGeneration() throws InterruptedException{
		prepareGenerationScheduleWithAnyAllowableParameters();
		stationControlPanel.performGenerationSchedule(stationGenerationSchedule);
		doPauseUntilMainControlPanellAdjustGenerators();
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
	
	@Test
	public void controlPanelRegisteresWithDispacherRightAfterreRisterWithDispatcherCalled(){
		Dispatcher dispatcher = mock(Dispatcher.class);
		stationControlPanel.registerWithDispatcher(dispatcher);
		
		verify(dispatcher, atLeastOnce()).registerPowerObject(any());
	}
}
