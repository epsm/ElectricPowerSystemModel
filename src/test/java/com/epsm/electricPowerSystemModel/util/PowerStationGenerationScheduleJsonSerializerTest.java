package com.epsm.electricPowerSystemModel.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.electricPowerSystemModel.model.constantsForTests.TestsConstants;
import com.epsm.electricPowerSystemModel.model.generation.GeneratorGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationGenerationSchedule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PowerStationGenerationScheduleJsonSerializerTest {
	private ObjectMapper mapper;
	private PowerStationGenerationSchedule schedule;
	
	@Before
	public void setUp(){
		mapper = new ObjectMapper();
		
		schedule = new PowerStationGenerationSchedule(1, LocalDateTime.MIN, LocalTime.MIN, 2);
		LoadCurve generationCurve = new LoadCurve(TestsConstants.LOAD_BY_HOURS);
		GeneratorGenerationSchedule genrationSchedule_1 = new GeneratorGenerationSchedule(
				1, true, true, null);
		GeneratorGenerationSchedule genrationSchedule_2 = new GeneratorGenerationSchedule(
				2, true, false, generationCurve);
		schedule.addGeneratorSchedule(genrationSchedule_1);
		schedule.addGeneratorSchedule(genrationSchedule_2);
	}
	
	@Test
	public void serializesCorrect() throws JsonProcessingException{
		String expected = 
			"{\"powerObjectId\":1,"
			+ "\"realTimeStamp\":\"-999999999-01-01T00:00\","
			+ "\"simulationTimeStamp\":0,"
			+ "\"generatorQuantity\":2,"
			+ "\"generators\":{"
			+ "\"1\":"
			+ "{\"generatorTurnedOn\":true,"
			+ "\"astaticRegulatorTurnedOn\":true,"
			+ "\"curve\":null},"
			+ "\"2\":"
			+ "{\"generatorTurnedOn\":true,"
			+ "\"astaticRegulatorTurnedOn\":false,"
			+ "\"curve\":{"
			+ "\"loadByHoursInMW\":"
			+ "[64.88,59.54,55.72,51.9,48.47,48.85,48.09,57.25,76.35,91.6,100.0,99.23,"
			+ "91.6,91.6,91.22,90.83,90.83,90.83,90.83,90.83,90.83,90.83,90.83,83.96]"
			+ "}}}}";
		
		String serialized = mapper.writeValueAsString(schedule);
		Assert.assertEquals(expected, serialized);
	}
}
