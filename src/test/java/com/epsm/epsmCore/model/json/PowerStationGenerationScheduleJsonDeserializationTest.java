package com.epsm.epsmCore.model.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.epsm.epsmCore.model.bothConsumptionAndGeneration.PowerCurve;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.constantsForTests.TestsConstants;
import com.epsm.epsmCore.model.generation.GeneratorGenerationSchedule;
import com.epsm.epsmCore.model.generation.PowerStationGenerationSchedule;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PowerStationGenerationScheduleJsonDeserializationTest {
	private ObjectMapper mapper;
	private PowerStationGenerationSchedule schedule;
	private GeneratorGenerationSchedule firstGeneratorSchedule;
	private GeneratorGenerationSchedule secondGeneratorSchedule;
	private String source;
	private final long POWER_OBJECT_ID = 995;
	private final int QUANTITY_OF_GENERATORS = 2;
	private final int FIRST_GENERATOR_NUMBER = 1;
	private final int SECOND_GENERATOR_NUMBER = 2;
	private final LocalDateTime REALTIME_STAMP = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.of(7, 6, 5, 4, 3, 2, 1);

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		
		source = "{\"powerObjectId\":995,"
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
		
		schedule = mapper.readValue(source, PowerStationGenerationSchedule.class);
		firstGeneratorSchedule = schedule.getGeneratorSchedule(1);
		secondGeneratorSchedule = schedule.getGeneratorSchedule(2);
	}

	@Test
	public void objectIdCorrect(){
		Assert.assertEquals(POWER_OBJECT_ID, schedule.getPowerObjectId());
	}

	@Test
	public void realTimeStampCorrect(){
		Assert.assertEquals(REALTIME_STAMP, schedule.getRealTimeStamp());
	}
	
	@Test
	public void simulationTimeStampCorrect(){
		Assert.assertEquals(SIMULATION_TIMESTAMP, schedule.getSimulationTimeStamp());
	}
	
	@Test
	public void quantityOfGeneratorsCorrect(){
		Assert.assertEquals(QUANTITY_OF_GENERATORS, schedule.getQuantityOfGenerators());
	}
	
	@Test
	public void firstGeneratorNumberCorrect(){
		Assert.assertEquals(FIRST_GENERATOR_NUMBER, firstGeneratorSchedule.getGeneratorNumber());
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
		Assert.assertNull(firstGeneratorSchedule.getGenerationCurve());
	}
	
	@Test
	public void secondGeneratorNumberCorrect(){
		Assert.assertEquals(SECOND_GENERATOR_NUMBER, secondGeneratorSchedule.getGeneratorNumber());
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
		PowerCurve generationCurve = secondGeneratorSchedule.getGenerationCurve();
		
		do{
			float actualGenerationOnThisHour = generationCurve.getPowerOnTimeInMW(pointer); 
			float expectedGenerationOnThisHour = TestsConstants.LOAD_BY_HOURS[pointer.getHour()];
			
			
			Assert.assertEquals(expectedGenerationOnThisHour, actualGenerationOnThisHour, 0);
			pointer = pointer.plusHours(1);
		}while(pointer.isAfter(LocalTime.MIDNIGHT));
	}
}
