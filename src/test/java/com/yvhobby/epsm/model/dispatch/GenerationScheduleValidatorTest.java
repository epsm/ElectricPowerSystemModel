package test.java.com.yvhobby.epsm.model.dispatch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import main.java.com.yvhobby.epsm.model.dispatch.GenerationScheduleValidator;
import main.java.com.yvhobby.epsm.model.dispatch.GeneratorGenerationSchedule;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationGenerationSchedule;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationParameters;
import main.java.com.yvhobby.epsm.model.generation.PowerStationException;

public class GenerationScheduleValidatorTest {
	private GenerationScheduleValidator validator = new GenerationScheduleValidator();
	private PowerStationGenerationSchedule stationSchedule;
	private PowerStationParameters stationParameters;
	private Map<Integer, GeneratorGenerationSchedule> generatorSchedules;
	
	@Test(expected = PowerStationException.class)
	public void stationParameterIsNullAndScheduleIsNotNull(){
		stationSchedule = mock(PowerStationGenerationSchedule.class);
		stationParameters = null;
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	@Test(expected = PowerStationException.class)
	public void scheduleIsNullAndStationParameterIsNull(){
		stationSchedule = null;
		stationParameters = mock(PowerStationParameters.class);
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	@Test(expected = PowerStationException.class)
	public void quantityOfGeneratorsOnStationDoesNotConformToTheirQuantityInSchedule(){
		prepareScheduleWithThreeGenerators();
		prepareStationWithTwoGenerators();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void prepareScheduleWithThreeGenerators(){
		stationSchedule = mock(PowerStationGenerationSchedule.class);
		when(stationSchedule.getQuantityOfGener()).thenReturn(3);
	}
	
	private void prepareStationWithTwoGenerators(){
		stationParameters = mock(PowerStationParameters.class);
		when(stationParameters.getQuantityOfGenerators()).thenReturn(2);
	}
	
	@Test(expected = PowerStationException.class)
	public void scheduleAndPowerStationContainsDifferentGeneratorsNumbers(){
		prepareSchedulesWithSecondAndThirdGenerators();
		prepareStationWithFirstAndSecondGenerators();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void prepareSchedulesWithSecondAndThirdGenerators(){
		Collection<Integer> numbers = Arrays.asList(new Integer[] {2, 3}); 
		stationSchedule = mock(PowerStationGenerationSchedule.class);
		when(stationSchedule.getGeneratorsNumbers()).thenReturn(numbers);
	}
	
	private void prepareStationWithFirstAndSecondGenerators(){
		Collection<Integer> numbers = Arrays.asList(new Integer[] {1, 2}); 
		stationParameters = mock(PowerStationParameters.class);
		when(stationParameters.getGeneratorsNumbers()).thenReturn(numbers);
	}
	
	@Test(expected = PowerStationException.class)
	public void generatorSchedulesContainerIsNull(){
		prepareNullGeneratorSchedulesContainerAndNormalStationParameters();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void prepareNullGeneratorSchedulesContainerAndNormalStationParameters(){
		stationParameters = mock(PowerStationParameters.class);
		stationSchedule = mock(PowerStationGenerationSchedule.class);
		when(stationSchedule.getGeneratorGenerationSchedules()).thenReturn(null);
	}
	
	@Test(expected = PowerStationException.class)
	public void oneScheduleIsNull(){
		createStationScheduleWithGivenSingleGeneratorSchedule(null);
		stationParameters = mock(PowerStationParameters.class);
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void createStationScheduleWithGivenSingleGeneratorSchedule(
			GeneratorGenerationSchedule generatorSchedule){
		
		generatorSchedules = new HashMap<Integer, GeneratorGenerationSchedule>();
		generatorSchedules.put(1, generatorSchedule);
		stationSchedule = new PowerStationGenerationSchedule(generatorSchedules);
	}

	@Test(expected = PowerStationException.class)
	public void generationCurveIsAbsentWhenAstaticRegulationTurnedOff(){
		GeneratorGenerationSchedule generatorSchedule = 
				prepareScheduleGeneratorOnAstaticRegulationOffGenerationCurveNull();
		
		createStationScheduleWithGivenSingleGeneratorSchedule(generatorSchedule);
		prepareMockedStationParametrsWithOneGenerator();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private GeneratorGenerationSchedule prepareScheduleGeneratorOnAstaticRegulationOffGenerationCurveNull(){
		return new GeneratorGenerationSchedule(1, true, false, null);
	}
	
	private void prepareMockedStationParametrsWithOneGenerator(){
		stationParameters = mock(PowerStationParameters.class);
		when(stationParameters.getQuantityOfGenerators()).thenReturn(1);
		when(stationParameters.getGeneratorsNumbers()).thenReturn(Arrays.asList(new Integer[] {1}));
	}
	
	public void powerInGenerationCurveTooHighForGenerator(){
		
	}
	
	public void powerInGenerationCurveTooLowForGenerator(){
		
	}
	
	public void generationCurveHasLessThan24hours(){
		
	}
	
	public void generationCurveHasMoreThan24hours(){
		
	}
	
	public void doNothingIfScheduleCorrect(){
		
	}
	
	public void generatorNumbersInScheduleWasSetWrong(){
		
	}
}
