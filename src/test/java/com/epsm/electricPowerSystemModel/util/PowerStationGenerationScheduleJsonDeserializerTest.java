package com.epsm.electricPowerSystemModel.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.electricPowerSystemModel.model.constantsForTests.TestsConstants;
import com.epsm.electricPowerSystemModel.model.generation.GeneratorGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationGenerationSchedule;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PowerStationGenerationScheduleJsonDeserializerTest {
	private ObjectMapper mapper;
	private PowerStationGenerationSchedule schedule;
	private GeneratorGenerationSchedule firstGeneratorSchedule;
	private GeneratorGenerationSchedule secondGeneratorSchedule;
	private String source;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		source = "{"
				+ "\"powerObjectId\":995,"
				+ "\"realTimeStamp\":\"0001-02-03T04:05:06.000000007\","
				+ "\"simulationTimeStamp\":3723000000004,"
				+ "\"generatorQuantity\":2,"
				+ "\"generators\":{"
				+ "\"1\":{"
				+ "\"generatorTurnedOn\":true,"
				+ "\"astaticRegulatorTurnedOn\":true,"
				+ "\"generatorNumber\":1,"
				+ "\"curve\":null},"
				+ "\"2\":{"
				+ "\"generatorTurnedOn\":true,"
				+ "\"astaticRegulatorTurnedOn\":false,"
				+ "\"generatorNumber\":2,"
				+ "\"curve\":{"
				+ "\"loadByHoursInMW\":["
				+ "64.88,59.54,55.72,51.9,48.47,48.85,48.09,57.25,76.35,91.6,100.0,99.23,"
				+ "91.6,91.6,91.22,90.83,90.83,90.83,90.83,90.83,90.83,90.83,90.83,83.96"
				+ "]}}}}";
		
		schedule = mapper.readValue(source, PowerStationGenerationSchedule.class);
		firstGeneratorSchedule = schedule.getGeneratorSchedule(1);
		secondGeneratorSchedule = schedule.getGeneratorSchedule(2);
	}

	@Test
	public void objectIdCorrect(){
		Assert.assertEquals(995, schedule.getPowerObjectId());
	}

	@Test
	public void realTimeStampCorrect(){
		Assert.assertEquals(LocalDateTime.of(1, 2, 3, 4, 5, 6, 7), schedule.getRealTimeStamp());
	}
	
	@Test
	public void simulationTimeStampCorrect(){
		Assert.assertEquals(LocalTime.of(1, 2, 3, 4), schedule.getSimulationTimeStamp());
	}
	
	@Test
	public void firstGeneratorNumberCorrect(){
		Assert.assertEquals(1, firstGeneratorSchedule.getGeneratorNumber());
	}
	
	@Test
	public void firstGeneratorTunedOnCorrect(){
		Assert.assertTrue(firstGeneratorSchedule.isGeneratorTurnedOn());
	}
	
	@Test
	public void firstGeneratorAstaticRegulationTurnedOnCorrect(){
		Assert.assertTrue(firstGeneratorSchedule.isAstaticRegulatorTurnedOn());
	}
	
	@Test
	public void firstGeneratorGenerationCurveCorrect(){
		Assert.assertNull(firstGeneratorSchedule.getCurve());
	}
	
	@Test
	public void secondGeneratorNumberCorrect(){
		Assert.assertEquals(2, secondGeneratorSchedule.getGeneratorNumber());
	}
	
	@Test
	public void secondGeneratorTunedOnCorrect(){
		Assert.assertTrue(secondGeneratorSchedule.isGeneratorTurnedOn());
	}
	
	@Test
	public void secondGeneratorAstaticRegulationTurnedOnCorrect(){
		Assert.assertFalse(secondGeneratorSchedule.isAstaticRegulatorTurnedOn());
	}
	
	@Test
	public void secondGeneratorGenerationCurveCorrect(){
		LocalTime pointer = LocalTime.MIDNIGHT;
		LoadCurve generationCurve = secondGeneratorSchedule.getCurve();
		
		do{
			float actualGenerationOnThisHour = generationCurve.getPowerOnTimeInMW(pointer); 
			float expectedGenerationOnThisHour = TestsConstants.LOAD_BY_HOURS[pointer.getHour()];
			
			
			Assert.assertEquals(expectedGenerationOnThisHour, actualGenerationOnThisHour, 0);
			pointer = pointer.plusHours(1);
		}while(pointer.isAfter(LocalTime.MIDNIGHT));
	}
}
