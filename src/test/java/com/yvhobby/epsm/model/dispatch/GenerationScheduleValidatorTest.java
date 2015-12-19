package test.java.com.yvhobby.epsm.model.dispatch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.java.com.yvhobby.epsm.model.bothConsumptionAndGeneration.LoadCurve;
import main.java.com.yvhobby.epsm.model.dispatch.GenerationScheduleValidator;
import main.java.com.yvhobby.epsm.model.dispatch.GeneratorGenerationSchedule;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationGenerationSchedule;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationParameters;
import main.java.com.yvhobby.epsm.model.generation.PowerStationException;

public class GenerationScheduleValidatorTest {
	private GenerationScheduleValidator validator;
	private PowerStationGenerationSchedule stationSchedule;
	private PowerStationParameters stationParameters;
	private Map<Integer, GeneratorGenerationSchedule> generatorSchedules;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void Initialize(){
		validator = new GenerationScheduleValidator();
		generatorSchedules = new HashMap<Integer, GeneratorGenerationSchedule>();
	}
	
	@Test
	public void exceptionIfStationParameterIsNull(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("Station parameters is null.");
		
		stationSchedule = mock(PowerStationGenerationSchedule.class);
		stationParameters = null;
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	@Test
	public void exceptionIfStationScheduleIsNull(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("Station schedule is null.");
		
		stationSchedule = null;
		stationParameters = mock(PowerStationParameters.class);
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	@Test
	public void exceptionIfOnOfGeneratorScheduleIsNull(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("Wrong schedule: one of schedules for generator is null.");
	    
		createStationScheduleWithFirstGenerator(null);
		stationParameters = mock(PowerStationParameters.class);
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void createStationScheduleWithFirstGenerator(
			GeneratorGenerationSchedule generatorSchedule){
		
		generatorSchedules.put(1, generatorSchedule);
		stationSchedule = new PowerStationGenerationSchedule(generatorSchedules);
	}
	
	@Test
	public void exceptionIfReturnedByScheduleGeneratorIsNotEqualToRequestedNumber(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("Wrong schedule: numbers of requested and received generator "
	    		+ "doesn't match.");
	    
		prepareScheduleWithWrongGeneratorNumber();
		prepareMockedStationParametersWithFirstGenerator();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void prepareScheduleWithWrongGeneratorNumber(){
		GeneratorGenerationSchedule generatorSchedule = null;
		final int WRONG_GENERATOR_NUMBER = 2;
		
		generatorSchedule = new GeneratorGenerationSchedule(WRONG_GENERATOR_NUMBER, true, true, null);
		createStationScheduleWithFirstGenerator(generatorSchedule);
	}
	
	private void prepareMockedStationParametersWithFirstGenerator(){
		stationParameters = mock(PowerStationParameters.class);
		when(stationParameters.getQuantityOfGenerators()).thenReturn(1);
		when(stationParameters.getGeneratorsNumbers()).thenReturn(Arrays.asList(new Integer[] {1}));
	}
	
	@Test
	public void exceptionIfStationParametersAndScheduleHaveDifferentAmountOfGenerators(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("Wrong schedule: station has 2 generator(s) but schedule has "
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
	    expectedEx.expectMessage("Wrong schedule: station and schedule has different generator numbers.");
	    
		prepareStationSchedule_FirstGeneratorOnAstaticRegulationOffCurveNull();
		prepareStationWithOnlyOneSecondGenerator();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	private void prepareStationSchedule_FirstGeneratorOnAstaticRegulationOffCurveNull(){
		GeneratorGenerationSchedule generatorSchedule = 
				new GeneratorGenerationSchedule(1, true, false, null);
		createStationScheduleWithFirstGenerator(generatorSchedule);
	} 
	
	private void prepareStationWithOnlyOneSecondGenerator(){
		Collection<Integer> numbers = Arrays.asList(new Integer[] {2}); 
		stationParameters = mock(PowerStationParameters.class);
		when(stationParameters.getGeneratorsNumbers()).thenReturn(numbers);
		when(stationParameters.getQuantityOfGenerators()).thenReturn(1);
	}
	
	@Test
	public void exceptionIfgenerationCurveIsAbsentWhenAstaticRegulationTurnedOff(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("Wrong schedule: there is no necessary generation curve for generator 1.");
		
	    prepareStationSchedule_FirstGeneratorOnAstaticRegulationOffCurveNull();
		prepareMockedStationParametersWithFirstGenerator();
		
		validator.validate(stationSchedule, stationParameters);
	}
	
	@Ignore
	@Test
	public void powerInGenerationCurveTooHighForGenerator(){
		
	}
	
	@Ignore
	@Test
	public void powerInGenerationCurveTooLowForGenerator(){
		
	}
	
	@Ignore
	@Test
	public void doNothingIfScheduleCorrect(){
		
	}
}
