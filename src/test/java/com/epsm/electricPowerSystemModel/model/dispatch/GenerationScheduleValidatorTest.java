package com.epsm.electricPowerSystemModel.model.dispatch;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.electricPowerSystemModel.model.dispatch.GenerationScheduleValidator;
import com.epsm.electricPowerSystemModel.model.dispatch.GeneratorGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.dispatch.GeneratorParameters;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerStationGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerStationParameters;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationException;
import com.epsm.electricPowerSystemModel.model.constantsForTests.TestsConstants;

public class GenerationScheduleValidatorTest {
	private GenerationScheduleValidator validator;
	private PowerStationGenerationSchedule stationSchedule;
	private PowerStationParameters stationParameters;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void Initialize(){
		validator = new GenerationScheduleValidator();
		stationSchedule = new PowerStationGenerationSchedule(1);
		stationParameters = mock(PowerStationParameters.class);
		
		when(stationParameters.getPowerStationNumber()).thenReturn(1);
	}
	
	@Test
	public void exceptionIfStationParameterIsNull(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("station parameters is null.");
		
		stationSchedule = mock(PowerStationGenerationSchedule.class);
		stationParameters = null;
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	@Test
	public void exceptionIfStationScheduleIsNull(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("station schedule is null.");
		
		stationSchedule = null;
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	@Test
	public void exceptionIfPowerAndScheduleHaveDifferentNumbers(){
		expectedEx.expect(PowerStationException.class);
		expectedEx.expectMessage("wrong schedule: station has №3 but schedule has №1.");
		
		when(stationParameters.getPowerStationNumber()).thenReturn(3);
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	@Test
	public void exceptionIfStationParametersAndScheduleHaveDifferentAmountOfGenerators(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("wrong schedule: station has 2 generator(s) but schedule has "
	    		+ "3 generator(s).");
		
		prepareScheduleWithThreeGenerators();
		prepareStationWithTwoGenerators();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void prepareScheduleWithThreeGenerators(){
		stationSchedule = mock(PowerStationGenerationSchedule.class);
		when(stationSchedule.getQuantityOfGenerators()).thenReturn(3);
	}
	
	private void prepareStationWithTwoGenerators(){
		stationParameters = mock(PowerStationParameters.class);
		when(stationParameters.getQuantityOfGenerators()).thenReturn(2);
	}
	
	@Test
	public void exceptionIfStationParametersAndScheduleHaveGeneratorsWithDifferentNumbers(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("wrong schedule: station and schedule has different generator numbers.");
	    
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
		stationSchedule.addGeneratorGenerationSchedule((generatorSchedule));
	}
	
	private void prepareStationWithOnlyOneSecondGenerator(){
		Collection<Integer> numbers = Arrays.asList(new Integer[] {2});
		when(stationParameters.getGeneratorsNumbers()).thenReturn(numbers);
		when(stationParameters.getQuantityOfGenerators()).thenReturn(1);
	}
	
	@Test
	public void exceptionIfgenerationCurveIsAbsentWhenAstaticRegulationTurnedOff(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("wrong schedule: there is no necessary generation curve for generator 1.");
		
	    prepareStationSchedule_FirstGeneratorOnAstaticRegulationOffCurveNull();
		prepareMockedStationParametersWithFirstGenerator();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void prepareMockedStationParametersWithFirstGenerator(){
		when(stationParameters.getQuantityOfGenerators()).thenReturn(1);
		when(stationParameters.getGeneratorsNumbers()).thenReturn(Arrays.asList(new Integer[] {1}));
	}
	
	@Test
	public void powerInGenerationCurveTooHighForGenerator(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("wrong schedule: scheduled generation power for generator 1 is"
	    		+ " more than nominal.");
		
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
		when(stationParameters.getQuantityOfGenerators()).thenReturn(1);
		when(stationParameters.getGeneratorsNumbers()).thenReturn(Arrays.asList(new Integer[] {1}));
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
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("wrong schedule: scheduled generation power for generator 1 is"
	    		+ " less than minimal technology.");
		
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
}
