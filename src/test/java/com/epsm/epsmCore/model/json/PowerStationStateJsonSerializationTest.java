package com.epsm.epsmCore.model.json;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.generation.GeneratorState;
import com.epsm.epsmCore.model.generation.PowerStationState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PowerStationStateJsonSerializationTest {
	private ObjectMapper mapper;
	private PowerStationState state;
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
	public void setUp(){
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		
		state = new PowerStationState(POWER_OBJECT_ID, REALTIME_STAMP, SIMULATION_TIMESTAMP,
				QUANTITY_OF_GENERATORS, FREQUENCY);
		GeneratorState state_1 = new GeneratorState(FIRST_GENERATOR_NUMBER, FIRST_GENERATOR_GENERATION);
		GeneratorState state_2 = new GeneratorState(SECOND_GENERATOR_NUMBER, SECOND_GENERATOR_GENERATION);
			
		state.addGeneratorState(state_1);
		state.addGeneratorState(state_2);
	}
	
	@Test
	public void serializesCorrect() throws JsonProcessingException{
		String expected = 
			"{\"powerObjectId\":884,"
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
		
		String serialized = mapper.writeValueAsString(state);

		Assert.assertEquals(expected, serialized);
	}
}
