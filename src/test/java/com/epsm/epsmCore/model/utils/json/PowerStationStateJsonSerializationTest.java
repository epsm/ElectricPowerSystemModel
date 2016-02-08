package com.epsm.epsmCore.model.utils.json;

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
	
	@Before
	public void setUp(){
		mapper = new ObjectMapper();
		
		LocalDateTime realTimeStamp = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
		LocalDateTime simulationTimeStamp = LocalDateTime.of(7, 6, 5, 4, 3, 2, 1);
		state = new PowerStationState(884, realTimeStamp, simulationTimeStamp, 2, 50);
		GeneratorState state_1 = new GeneratorState(1, 60);
		GeneratorState state_2 = new GeneratorState(2, 70);
			
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
		System.out.println(serialized);
		Assert.assertEquals(expected, serialized);
	}
}
