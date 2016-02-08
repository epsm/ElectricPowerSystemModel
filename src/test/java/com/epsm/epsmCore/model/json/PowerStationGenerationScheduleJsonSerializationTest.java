package com.epsm.epsmCore.model.json;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.epsmCore.model.constantsForTests.TestsConstants;
import com.epsm.epsmCore.model.generation.GeneratorGenerationSchedule;
import com.epsm.epsmCore.model.generation.PowerStationGenerationSchedule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PowerStationGenerationScheduleJsonSerializationTest {
	private ObjectMapper mapper;
	private PowerStationGenerationSchedule schedule;
	private final long POWER_OBJECT_ID = 995;
	private final int QUANTITY_OF_GENERATORS = 2;
	private final int FIRST_GENERATOR_NUMBER = 1;
	private final int SECOND_GENERATOR_NUMBER = 2;
	private final boolean ASTATIC_REGULATION_ON = true;
	private final boolean ASTATIC_REGULATION_OFF = false;
	private final boolean GENERATOR_ON = true;
	private final LocalDateTime REALTIME_STAMP = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.of(7, 6, 5, 4, 3, 2, 1);
	private final LoadCurve NULL_CURVE = null;

	@Before
	public void setUp(){
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		
		schedule = new PowerStationGenerationSchedule(POWER_OBJECT_ID, REALTIME_STAMP, 
				SIMULATION_TIMESTAMP, QUANTITY_OF_GENERATORS);
		
		LoadCurve generationCurve = new LoadCurve(TestsConstants.LOAD_BY_HOURS);
		
		GeneratorGenerationSchedule genrationSchedule_1 = new GeneratorGenerationSchedule(
				FIRST_GENERATOR_NUMBER, GENERATOR_ON, ASTATIC_REGULATION_ON, NULL_CURVE);
		GeneratorGenerationSchedule genrationSchedule_2 = new GeneratorGenerationSchedule(
				SECOND_GENERATOR_NUMBER, GENERATOR_ON, ASTATIC_REGULATION_OFF, generationCurve);
		
		schedule.addGeneratorSchedule(genrationSchedule_1);
		schedule.addGeneratorSchedule(genrationSchedule_2);
	}
	
	@Test
	public void serializesCorrect() throws JsonProcessingException{
		String expected ="{\"powerObjectId\":995,"
				+ "\"realTimeStamp\":[1,2,3,4,5,6,7],"
				+ "\"simulationTimeStamp\":[7,6,5,4,3,2,1],"
				+ "\"quantityOfGenerators\":2,"
				+ "\"generators\":{\"inclusionQuantity\":2,"
				+ "\"inclusions\":{"
				+ "\"1\":{"
				+ "\"generatorNumber\":1,"
				+ "\"generatorTurnedOn\":true,"
				+ "\"astaticRegulatorTurnedOn\":true,"
				+ "\"generationCurve\":null},"
				+ "\"2\":{"
				+ "\"generatorNumber\":2,"
				+ "\"generatorTurnedOn\":true,"
				+ "\"astaticRegulatorTurnedOn\":false,"
				+ "\"generationCurve\":{"
				+ "\"loadByHoursInMW\":"
				+ "[64.88,59.54,55.72,51.9,48.47,48.85,48.09,57.25,76.35,91.6,100.0,99.23,"
				+ "91.6,91.6,91.22,90.83,90.83,90.83,90.83,90.83,90.83,90.83,90.83,83.96]}}}}}";
		
		String serialized = mapper.writeValueAsString(schedule);
		
		Assert.assertEquals(expected, serialized);
	}
}
