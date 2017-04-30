package com.epsm.epsmcore.model.json;

import com.epsm.epsmcore.model.generation.GeneratorState;
import com.epsm.epsmcore.model.generation.PowerStationState;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

public class PowerStationStateJsonDeserializationTest {
	private ObjectMapper mapper;
	private PowerStationState stationState;
	private GeneratorState firstGeneratorState;
	private GeneratorState secondGeneratorState;
	private String source;
	private final long POWER_OBJECT_ID = 884;
	private final int QUANTITY_OF_GENERATORS = 2;
	private final float FREQUENCY = 50;
	private final int FIRST_GENERATOR_NUMBER = 1;
	private final int SECOND_GENERATOR_NUMBER = 2;
	private float FIRST_GENERATOR_GENERATION = 60;
	private float SECOND_GENERATOR_GENERATION = 70;
	private final LocalDateTime REALTIME_STAMP = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.of(7, 6, 5, 4, 3, 2, 1);

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		
		source = "{\"powerObjectId\":884,"
				+ "\"realTimeStamp\":[1,2,3,4,5,6,7],"
				+ "\"simulationTimeStamp\":[7,6,5,4,3,2,1],"
				+ "\"quantityOfGenerators\":2,"
				+ "\"frequency\":50.0,"
				+ "\"generators\":{\"inclusionQuantity\":2,\"inclusions\":{"
				+ "\"1\":{"
				+ "\"generatorNumber\":1,"
				+ "\"generationInWM\":60.0},"
				+ "\"2\":{\"generatorNumber\":2,"
				+ "\"generationInWM\":70.0}}}}";

		stationState = mapper.readValue(source, PowerStationState.class);
		firstGeneratorState = stationState.getGeneratorState(1);
		secondGeneratorState = stationState.getGeneratorState(2);
	}

	@Test
	public void objectIdCorrect(){
		Assert.assertEquals(POWER_OBJECT_ID, stationState.getPowerObjectId());
	}

	@Test
	public void realTimeStampCorrect(){
		Assert.assertEquals(REALTIME_STAMP, stationState.getRealTimeStamp());
	}
	
	@Test
	public void simulationTimeStampCorrect(){
		Assert.assertEquals(SIMULATION_TIMESTAMP, stationState.getSimulationTimeStamp());
	}
	
	@Test
	public void frequencyCorrect(){
		Assert.assertEquals(FREQUENCY, stationState.getFrequency(), 0);
	}
	
	@Test
	public void quantityOfGeneratorsCorrect(){
		Assert.assertEquals(QUANTITY_OF_GENERATORS, stationState.getQuantityOfGenerators());
	}
	
	@Test
	public void firstGeneratorNumberCorrect(){
		Assert.assertEquals(FIRST_GENERATOR_NUMBER, firstGeneratorState.getGeneratorNumber());
	}
	
	@Test
	public void firstGeneratorGenerationCorrect(){
		Assert.assertEquals(FIRST_GENERATOR_GENERATION, firstGeneratorState.getGenerationInWM(), 0);
	}
	
	@Test
	public void secondGeneratorNumberCorrect(){
		Assert.assertEquals(SECOND_GENERATOR_NUMBER, secondGeneratorState.getGeneratorNumber());
	}
	
	@Test
	public void secondGeneratorGenerationCorrect(){
		Assert.assertEquals(SECOND_GENERATOR_GENERATION, secondGeneratorState.getGenerationInWM(), 0);
	}
}
