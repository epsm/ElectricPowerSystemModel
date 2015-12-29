package com.epsm.electricPowerSystemModel.model.dispatch;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.dispatch.GeneratorGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.dispatch.PowerStationGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.generation.GenerationException;

public class PowerStationGenerationScheduleTest{

	private PowerStationGenerationSchedule stationSchedule = new PowerStationGenerationSchedule(1);
	private GeneratorGenerationSchedule generatorSchedule =  
			new GeneratorGenerationSchedule(1, false, false, null);	
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionIfGeneratorGenerationScheduleIsNull(){
		expectedEx.expect(GenerationException.class);
	    expectedEx.expectMessage("Generation schedule must not be null.");
		
		stationSchedule.addGeneratorGenerationSchedule(null);
	}
	
	@Test
	public void exceptionIfTryToAddGeneratorScheduleWithGeneratorNumberAsPreviouslyAdded(){
		expectedEx.expect(GenerationException.class);
	    expectedEx.expectMessage("Generation schedule for generator number 1 already exists.");
		
		stationSchedule.addGeneratorGenerationSchedule(generatorSchedule);
		stationSchedule.addGeneratorGenerationSchedule(generatorSchedule);
	}
}