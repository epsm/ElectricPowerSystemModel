package com.epsm.epsmCore.model.generation;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.epsmCore.model.constantsForTests.TestsConstants;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.generalModel.Constants;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.epsmCore.model.generalModel.SimulationException;
import com.epsm.epsmCore.model.generalModel.TimeService;

public class GeneratorControllerTest {
	private SpeedController speedController;
	private GeneratorController generatorController;
	private GeneratorGenerationSchedule schedule;
	private Generator generator;
	private float generation;
	private final float GENERATION_AT_GIVEN_FREQUENCY = 200;
	private final float NORMAL_FREQUENCY = Constants.STANDART_FREQUENCY;
	private final float LOW_FREQUENCY = Constants.STANDART_FREQUENCY - 1;
	private final float HIGH_FREQUENCY = Constants.STANDART_FREQUENCY + 1;
	private final LocalDateTime STEP_TIME = LocalDateTime.MIN.plusMinutes(1);
	
	
	@Before
	public void setUp(){
		generator = mock(Generator.class);
		when(generator.getNominalPowerInMW()).thenReturn(300f);
		when(generator.getMinimalPowerInMW()).thenReturn(100f);
		when(generator.getReugulationSpeedInMWPerMinute()).thenReturn(3f);
		speedController = spy(new SpeedController(generator));
		speedController.setGenerationAtGivenFrequency(GENERATION_AT_GIVEN_FREQUENCY);
		generatorController = new GeneratorController(generator, speedController);
		
	}
	
	@Test
	public void increaseGenerationIfFrequencyIsLowAndSecondaryFrequencyRegulationOn(){
		prepareScheduleWithTurnedOnSecondaryFrequencyRegulationOn();
		performStepWithLowFrequency();
		
		getGeneration();
		
		System.out.println(generation);
		
		Assert.assertTrue(generation > GENERATION_AT_GIVEN_FREQUENCY);
	}
	
	private void prepareScheduleWithTurnedOnSecondaryFrequencyRegulationOn(){
		schedule = new GeneratorGenerationSchedule(1, true, null);
		generatorController.setSchedule(schedule);
	}
	
	private void performStepWithLowFrequency(){
		generatorController.adjustGenerator(STEP_TIME, LOW_FREQUENCY);
	}
	
	private void getGeneration(){
		generation = speedController.getGenerationAtGivenFrequency();
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
	public void astaticRegulationWillBeTurnedOffIfItIsScheduledIndependentIsItTurnedOnOrOff() 
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
		LocalTime timeInSimulation = simulation.getDateTimeInSimulation().toLocalTime(); 
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
