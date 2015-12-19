package test.java.com.yvhobby.epsm.model.dispatch;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.java.com.yvhobby.epsm.model.dispatch.GeneratorGenerationSchedule;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationGenerationSchedule;
import main.java.com.yvhobby.epsm.model.generation.PowerStationException;

public class PowerStationGenerationScheduleTest{

	private PowerStationGenerationSchedule stationSchedule = new PowerStationGenerationSchedule();
	private GeneratorGenerationSchedule generatorSchedule =  
			new GeneratorGenerationSchedule(1, false, false, null);	
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionIfGeneratorGenerationScheduleIsNull(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("Generation schedule must not be null.");
		
		stationSchedule.addGeneratorGenerationSchedule(null);
	}
	
	@Test
	public void exceptionIfTryToAddGeneratorScheduleWithGeneratorNumberAsPreviouslyAdded(){
		expectedEx.expect(PowerStationException.class);
	    expectedEx.expectMessage("Generation schedule for generator number 1 already exists.");
		
		stationSchedule.addGeneratorGenerationSchedule(generatorSchedule);
		stationSchedule.addGeneratorGenerationSchedule(generatorSchedule);
	}
}