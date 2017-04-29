package com.epsm.epsmCore.model.generation;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.PowerCurve;
import com.epsm.epsmCore.model.constantsForTests.TestsConstants;
import com.epsm.epsmCore.model.dispatch.Dispatcher;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulation;
import com.epsm.epsmCore.model.generalModel.ElectricPowerSystemSimulationImpl;
import com.epsm.epsmCore.model.generalModel.TimeService;

public class GeneratorControllerTest {
	private ElectricPowerSystemSimulation simulation;
	private MainControlPanel controlPanel;
	private PowerStationGenerationSchedule stationSchedule;
	private GeneratorGenerationSchedule genrationSchedule_1;
	private GeneratorGenerationSchedule genrationSchedule_2;
	private PowerStationParameters parameters;
	private PowerStation station;
	private PowerCurve generationCurve;
	private Generator generator_1;
	private Generator generator_2;
	private TimeService timeService;
	private Dispatcher dispatcher;
	private final long POWER_OBJECT_ID = 0;
	private final int QUANTITY_OF_GENERATORS = 2;
	private final int FIRST_GENERATOR_NUMBER = 1;
	private final int SECOND_GENERATOR_NUMBER = 2;
	private final float NOMINAL_POWER_IN_MW = 200;
	private final float MINIMAL_POWER_IN_MW = 0;
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.MIN;
	private final LocalDateTime REAL_TIMESTAMP = LocalDateTime.MIN;
	private final boolean GENERATOR_ON = true;
	private final boolean GENERATOR_OFF = false;
	private final boolean ASTATIC_REGULATION_ON = true;
	private final boolean ASTATIC_REGULATION_OFF = false;
	
	@Before
	public void setUp(){
		stationSchedule = new PowerStationGenerationSchedule(POWER_OBJECT_ID, REAL_TIMESTAMP, 
				SIMULATION_TIMESTAMP, QUANTITY_OF_GENERATORS);
		
		generationCurve = new PowerCurve(TestsConstants.LOAD_BY_HOURS);
		
		parameters = new PowerStationParameters(POWER_OBJECT_ID, REAL_TIMESTAMP, SIMULATION_TIMESTAMP,
				QUANTITY_OF_GENERATORS);
		
		GeneratorParameters parameter_1 = new GeneratorParameters(FIRST_GENERATOR_NUMBER, 
				NOMINAL_POWER_IN_MW, MINIMAL_POWER_IN_MW);
		
		GeneratorParameters parameter_2 = new GeneratorParameters(SECOND_GENERATOR_NUMBER, 
				NOMINAL_POWER_IN_MW, MINIMAL_POWER_IN_MW);
		
		parameters.addGeneratorParameters(parameter_1);
		parameters.addGeneratorParameters(parameter_2);
		
		timeService = new TimeService();
		dispatcher = mock(Dispatcher.class);
		
		simulation = new ElectricPowerSystemSimulationImpl(timeService, dispatcher,
				TestsConstants.START_DATETIME);
		
		station = new PowerStation(simulation, timeService, dispatcher, parameters);
		controlPanel = new MainControlPanel(simulation, station);
		
		generator_1 = spy( new Generator(simulation, FIRST_GENERATOR_NUMBER));
		generator_2 = spy( new Generator(simulation, SECOND_GENERATOR_NUMBER));
		generator_1.setNominalPowerInMW(NOMINAL_POWER_IN_MW);
		generator_2.setNominalPowerInMW(NOMINAL_POWER_IN_MW);
		
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
		genrationSchedule_1 = new GeneratorGenerationSchedule(FIRST_GENERATOR_NUMBER, GENERATOR_ON, 
				ASTATIC_REGULATION_OFF, generationCurve);
		genrationSchedule_2 = new GeneratorGenerationSchedule(SECOND_GENERATOR_NUMBER, GENERATOR_ON, 
				ASTATIC_REGULATION_OFF, generationCurve);
		
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
		genrationSchedule_1 = new GeneratorGenerationSchedule(FIRST_GENERATOR_NUMBER, GENERATOR_OFF, 
				ASTATIC_REGULATION_OFF, generationCurve);
		genrationSchedule_2 = new GeneratorGenerationSchedule(SECOND_GENERATOR_NUMBER, GENERATOR_OFF, 
				ASTATIC_REGULATION_OFF, generationCurve);
		
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
		genrationSchedule_1 = new GeneratorGenerationSchedule(FIRST_GENERATOR_NUMBER, GENERATOR_ON, 
				ASTATIC_REGULATION_ON, generationCurve);
		genrationSchedule_2 = new GeneratorGenerationSchedule(SECOND_GENERATOR_NUMBER, GENERATOR_ON, 
				ASTATIC_REGULATION_ON, generationCurve);
		
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
		genrationSchedule_1 = new GeneratorGenerationSchedule(FIRST_GENERATOR_NUMBER, GENERATOR_ON, 
				ASTATIC_REGULATION_OFF, generationCurve);
		genrationSchedule_2 = new GeneratorGenerationSchedule(SECOND_GENERATOR_NUMBER, GENERATOR_ON, 
				ASTATIC_REGULATION_OFF, generationCurve);
		
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
