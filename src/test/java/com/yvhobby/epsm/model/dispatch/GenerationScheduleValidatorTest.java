package test.java.com.yvhobby.epsm.model.dispatch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import main.java.com.yvhobby.epsm.model.dispatch.GenerationScheduleValidator;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationGenerationSchedule;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationParameters;
import main.java.com.yvhobby.epsm.model.generation.PowerStationException;

public class GenerationScheduleValidatorTest {
	private GenerationScheduleValidator validator = new GenerationScheduleValidator();
	private PowerStationGenerationSchedule schedule;
	private PowerStationParameters stationParameters;
	
	@Test(expected = PowerStationException.class)
	public void numbersOfGeneratorsOnStationConformsToTheirNubersInSchedule(){
		prepareScheduleWithThreeGenerators();
		prepareStationWithTwoGenerators();
		validator.validate(schedule, stationParameters);
	}
	
	private void prepareScheduleWithThreeGenerators(){
		schedule = mock(PowerStationGenerationSchedule.class);
		when(schedule.getNumbersOfGenerators()).thenReturn(3);
	}
	
	private void prepareStationWithTwoGenerators(){
		stationParameters = mock(PowerStationParameters.class);
		when(stationParameters.getNumbersOfGenerators()).thenReturn(2);
	}
	
	@Test(expected = PowerStationException.class)
	public void scheduleContainsTheSameGeneratorsIdAsPowerStation(){
		prepareSchedulesWithSecondAndThirdGenerators();
		prepareStationWithFirstAndSecondGenerators();
		validator.validate(schedule, stationParameters);
	}
	
	private void prepareSchedulesWithSecondAndThirdGenerators(){
		Collection<Integer> idNumbers = Arrays.asList(new Integer[] {2, 3}); 
		schedule = mock(PowerStationGenerationSchedule.class);
		when(schedule.getGeneratorsId()).thenReturn(idNumbers);
	}
	
	private void prepareStationWithFirstAndSecondGenerators(){
		Collection<Integer> idNumbers = Arrays.asList(new Integer[] {1, 2}); 
		stationParameters = mock(PowerStationParameters.class);
		when(stationParameters.getGeneratorsId()).thenReturn(idNumbers);
	}
	
	public void astaticRegulationTurnedOffAndThereIsNoGenerationCurve(){
		
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
}
