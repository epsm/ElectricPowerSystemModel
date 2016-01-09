package com.epsm.electricPowerSystemModel.model.generation;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.electricPowerSystemModel.model.constantsForTests.TestsConstants;

public class GenerationScheduleValidatorTest {
	private GenerationScheduleValidator validator;
	private PowerStationGenerationSchedule stationSchedule;
	private PowerStationParameters stationParameters;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp(){
		validator = new GenerationScheduleValidator();
		stationSchedule 
			= new PowerStationGenerationSchedule(1, LocalDateTime.MIN, LocalTime.MIN, 1);
		stationParameters = mock(PowerStationParameters.class);
		
		when(stationParameters.getPowerObjectId()).thenReturn(1L);
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
		GeneratorGenerationSchedule generatorSchedule = 
				new GeneratorGenerationSchedule(1, true, false, null);
		createStationScheduleWithFirstGenerator(generatorSchedule);
	} 
	
	private void createStationScheduleWithFirstGenerator(
			GeneratorGenerationSchedule generatorSchedule){
		stationSchedule.addGeneratorSchedule((generatorSchedule));
	}
	
	private void prepareStationWithOnlyOneSecondGenerator(){
		Set<Integer> numbers = new HashSet<Integer>();
		numbers.add(2);
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
		numbers.add(1);
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
		int generatorNumber = 1;
		float minimalPower = 1;
		float nominalPower = 1;
		
		return new GeneratorParameters(generatorNumber, nominalPower, minimalPower);
	}
	
	private void preparePowerStation(GeneratorParameters parameters){
		Set<Integer> numbers = new HashSet<Integer>();
		numbers.add(1);
		
		when(stationParameters.getGeneratorParametersNumbers()).thenReturn(numbers);
		when(stationParameters.getGeneratorParameters(anyInt())).thenReturn(parameters);
	}
	
	private void prepareStationSchedule_FirstGeneratorOnAstaticRegulationOffCurveNotNull(){
		LoadCurve generationCurve = new LoadCurve(TestsConstants.LOAD_BY_HOURS);
		GeneratorGenerationSchedule generatorSchedule = 
				new GeneratorGenerationSchedule(1, true, false, generationCurve);
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
		int generatorNumber = 1;
		float minimalPower = 100;
		float nominalPower = 1000;
		
		return new GeneratorParameters(generatorNumber, nominalPower, minimalPower);
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
		int generatorNumber = 1;
		float minimalPower = 10;
		float nominalPower = 200;
		
		return new GeneratorParameters(generatorNumber, nominalPower, minimalPower);
	}
	
	@Test
	public void noExceptionIfAstaticRegulationOnCurveIsNull(){
		prepareStationSchedule_FirstGeneratorOnAstaticRegulationOnCurveNull();
		preparePowerStation(null);
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void prepareStationSchedule_FirstGeneratorOnAstaticRegulationOnCurveNull(){
		GeneratorGenerationSchedule generatorSchedule = 
				new GeneratorGenerationSchedule(1, true, true, null);
		createStationScheduleWithFirstGenerator(generatorSchedule);
	}
	
	@Test
	public void noExceptionIfGeneratorScheduledBeTurnedOff(){
	    prepareStationSchedule_FirstGeneratorOffAstaticRegulationOffCurveNotNull();
	    prepareStationWithOnlyOneFirstGenerator();
	    
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void prepareStationSchedule_FirstGeneratorOffAstaticRegulationOffCurveNotNull(){
		GeneratorGenerationSchedule generatorSchedule = 
				new GeneratorGenerationSchedule(1, false, false, null);
		createStationScheduleWithFirstGenerator(generatorSchedule);
	} 
	
	private void prepareStationWithOnlyOneFirstGenerator(){
		Set<Integer> numbers = new HashSet<Integer>();
		numbers.add(1);
		when(stationParameters.getGeneratorParametersNumbers()).thenReturn(numbers);
	}
}