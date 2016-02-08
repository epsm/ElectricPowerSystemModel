package com.epsm.epsmCore.model.generation;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.epsmCore.model.constantsForTests.TestsConstants;

public class GenerationScheduleValidatorTest {
	private GenerationScheduleValidator validator;
	private PowerStationGenerationSchedule stationSchedule;
	private PowerStationParameters stationParameters;
	private final long POWER_OBJECT_ID = 1;
	private final int FIRST_GENERATOR_NUMBER = 1;
	private final int QUANTITY_OF_GENERATORS = 1;
	private final boolean GENERATOR_ON = true;
	private final boolean GENERATOR_OFF = false;
	private final boolean ASTATIC_REGULATION_OFF = false;
	private final boolean ASTATIC_REGULATION_ON = true;
	private final LoadCurve NULL_CURVE = null;
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.MIN;
	private final LocalDateTime REAL_TIMESTAMP = LocalDateTime.MIN;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp(){
		validator = new GenerationScheduleValidator();
		stationSchedule = new PowerStationGenerationSchedule(POWER_OBJECT_ID, REAL_TIMESTAMP, 
				SIMULATION_TIMESTAMP, QUANTITY_OF_GENERATORS);
		stationParameters = mock(PowerStationParameters.class);
		
		when(stationParameters.getPowerObjectId()).thenReturn(POWER_OBJECT_ID);
	}
	
	@Test
	public void exceptionIfStationParametersAndScheduleHaveGeneratorsWithDifferentNumbers(){
		expectedEx.expect(GenerationException.class);
	    expectedEx.expectMessage("GenerationScheduleValidator: station has generator(s)"
	    		+ " with number(s) [2], but schedule has generator(s) with number(s) [1].");
	    
		prepareStationSchedule_FirstGeneratorOnAstaticRegulationOffCurveNull();
		prepareStationWithOnlyOneSecondGenerator();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void prepareStationSchedule_FirstGeneratorOnAstaticRegulationOffCurveNull(){
		GeneratorGenerationSchedule generatorSchedule = new GeneratorGenerationSchedule(
				FIRST_GENERATOR_NUMBER, GENERATOR_ON, ASTATIC_REGULATION_OFF, NULL_CURVE);
		createStationScheduleWithFirstGenerator(generatorSchedule);
	} 
	
	private void createStationScheduleWithFirstGenerator(
			GeneratorGenerationSchedule generatorSchedule){
		stationSchedule.addGeneratorSchedule((generatorSchedule));
	}
	
	private void prepareStationWithOnlyOneSecondGenerator(){
		final int generatorNumber = 2;
		Set<Integer> numbers = new HashSet<Integer>();
		numbers.add(generatorNumber);
		when(stationParameters.getGeneratorParametersNumbers()).thenReturn(numbers);
	}
	
	@Test
	public void exceptionIfgenerationCurveIsAbsentWhenAstaticRegulationTurnedOff(){
		expectedEx.expect(GenerationException.class);
	    expectedEx.expectMessage("GenerationScheduleValidator: there is no necessary"
	    		+ " generation curve for generator#1.");
		
	    prepareStationSchedule_FirstGeneratorOnAstaticRegulationOffCurveNull();
		prepareMockedStationParametersWithFirstGenerator();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void prepareMockedStationParametersWithFirstGenerator(){
		Set<Integer> numbers = new HashSet<Integer>();
		numbers.add(FIRST_GENERATOR_NUMBER);
		when(stationParameters.getGeneratorParametersNumbers()).thenReturn(numbers);
	}
	
	@Test
	public void powerInGenerationCurveTooHighForGenerator(){
		expectedEx.expect(GenerationException.class);
	    expectedEx.expectMessage("GenerationScheduleValidator: scheduled generation power"
	    		+ " for generator#1 is more than nominal.");
		
		GeneratorParameters parameters = prepareGeneratorParametersForTooWeakGenerator();
		preparePowerStation(parameters);
		prepareStationSchedule_FirstGeneratorOnAstaticRegulationOffCurveNotNull();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private GeneratorParameters prepareGeneratorParametersForTooWeakGenerator(){
		final float MINIMAL_POWER = 1;
		final float NOMINAL_POWER = 1;
		
		return new GeneratorParameters(FIRST_GENERATOR_NUMBER, NOMINAL_POWER, MINIMAL_POWER);
	}
	
	private void preparePowerStation(GeneratorParameters parameters){
		Set<Integer> numbers = new HashSet<Integer>();
		numbers.add(FIRST_GENERATOR_NUMBER);
		
		when(stationParameters.getGeneratorParametersNumbers()).thenReturn(numbers);
		when(stationParameters.getGeneratorParameters(anyInt())).thenReturn(parameters);
	}
	
	private void prepareStationSchedule_FirstGeneratorOnAstaticRegulationOffCurveNotNull(){
		LoadCurve generationCurve = new LoadCurve(TestsConstants.LOAD_BY_HOURS);
		GeneratorGenerationSchedule generatorSchedule = new GeneratorGenerationSchedule(
				FIRST_GENERATOR_NUMBER, GENERATOR_ON, ASTATIC_REGULATION_OFF, generationCurve);
		createStationScheduleWithFirstGenerator(generatorSchedule);
	} 
	
	@Test
	public void powerInGenerationCurveTooLowForGenerator(){
		expectedEx.expect(GenerationException.class);
	    expectedEx.expectMessage("GenerationScheduleValidator: scheduled generation power"
	    		+ " for generator#1 is less than minimal technology.");
		
		GeneratorParameters parameters = prepareGeneratorParametersForTooPowerfullGenerator();
		preparePowerStation(parameters);
		prepareStationSchedule_FirstGeneratorOnAstaticRegulationOffCurveNotNull();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private GeneratorParameters prepareGeneratorParametersForTooPowerfullGenerator(){
		final float MINIMAL_POWER = 100;
		final float NOMINAL_POWER = 1000;
		
		return new GeneratorParameters(FIRST_GENERATOR_NUMBER, NOMINAL_POWER, MINIMAL_POWER);
	}
	
	@Test
	public void noExceptionIfAstaticRegulationOffAndCurveConformsToGenerator(){
		GeneratorParameters parameters = 
				prepareGeneratorParametersThatConformsTestsConstants_LOAD_BY_HOURSCurve();
		preparePowerStation(parameters);
		prepareStationSchedule_FirstGeneratorOnAstaticRegulationOffCurveNotNull();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private GeneratorParameters prepareGeneratorParametersThatConformsTestsConstants_LOAD_BY_HOURSCurve(){
		final float MINIMAL_POWER = 10;
		final float NOMINAL_POWER = 200;
		
		return new GeneratorParameters(FIRST_GENERATOR_NUMBER, NOMINAL_POWER, MINIMAL_POWER);
	}
	
	@Test
	public void noExceptionIfAstaticRegulationOnCurveIsNull(){
		prepareStationSchedule_FirstGeneratorOnAstaticRegulationOnCurveNull();
		preparePowerStation(null);
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void prepareStationSchedule_FirstGeneratorOnAstaticRegulationOnCurveNull(){
		GeneratorGenerationSchedule generatorSchedule = new GeneratorGenerationSchedule(
				FIRST_GENERATOR_NUMBER, GENERATOR_ON, ASTATIC_REGULATION_ON, NULL_CURVE);
		createStationScheduleWithFirstGenerator(generatorSchedule);
	}
	
	@Test
	public void noExceptionIfGeneratorScheduledBeTurnedOff(){
	    prepareStationSchedule_FirstGeneratorOffAstaticRegulationOffCurveNotNull();
	    prepareStationWithOnlyOneFirstGenerator();
	    
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void prepareStationSchedule_FirstGeneratorOffAstaticRegulationOffCurveNotNull(){
		GeneratorGenerationSchedule generatorSchedule = new GeneratorGenerationSchedule(
				FIRST_GENERATOR_NUMBER, GENERATOR_OFF, ASTATIC_REGULATION_OFF, NULL_CURVE);
		createStationScheduleWithFirstGenerator(generatorSchedule);
	} 
	
	private void prepareStationWithOnlyOneFirstGenerator(){
		Set<Integer> numbers = new HashSet<Integer>();
		numbers.add(FIRST_GENERATOR_NUMBER);
		when(stationParameters.getGeneratorParametersNumbers()).thenReturn(numbers);
	}
}