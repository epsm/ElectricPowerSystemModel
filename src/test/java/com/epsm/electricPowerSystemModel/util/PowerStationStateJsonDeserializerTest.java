package com.epsm.electricPowerSystemModel.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.generation.GeneratorState;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationState;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PowerStationStateJsonDeserializerTest {
	private ObjectMapper mapper;
	private PowerStationState stationState;
	private GeneratorState firstGeneratorState;
	private GeneratorState secondGeneratorState;
	private String source;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		source = "{\"powerObjectId\":884,"
				 + "\"realTimeStamp\":\"0001-02-03T04:05:06.000000007\","
				 + "\"simulationTimeStamp\":3723000000004,"
				 + "\"generatorQuantity\":2,"
				 + "\"frequency\":50.0,"
				 + "\"generators\":{"
				 + "\"1\":{\"generationInWM\":60.0,\"generatorNumber\":1},"
				 + "\"2\":{\"generationInWM\":70.0,\"generatorNumber\":2}}}";
		
		stationState = mapper.readValue(source, PowerStationState.class);
		firstGeneratorState = stationState.getGeneratorState(1);
		secondGeneratorState = stationState.getGeneratorState(2);
	}

	@Test
	public void objectIdCorrect(){
		Assert.assertEquals(884, stationState.getPowerObjectId());
	}

	@Test
	public void realTimeStampCorrect(){
		Assert.assertEquals(LocalDateTime.of(1, 2, 3, 4, 5, 6, 7), stationState
				.getRealTimeStamp());
	}
	
	@Test
	public void simulationTimeStampCorrect(){
		Assert.assertEquals(LocalTime.of(1, 2, 3, 4), stationState.getSimulationTimeStamp());
	}
	
	@Test
	public void frequencyCorrect(){
		Assert.assertEquals(50, stationState.getFrequency(), 0);
	}
	
	@Test
	public void firstGeneratorNumberCorrect(){
		Assert.assertEquals(1, firstGeneratorState.getGeneratorNumber());
	}
	
	@Test
	public void firstGeneratorGenerationCorrect(){
		Assert.assertEquals(60, firstGeneratorState.getGenerationInWM(), 0);
	}
	
	@Test
	public void secondGeneratorNumberCorrect(){
		Assert.assertEquals(2, secondGeneratorState.getGeneratorNumber());
	}
	
	@Test
	public void secondGeneratorGenerationCorrect(){
		Assert.assertEquals(70, secondGeneratorState.getGenerationInWM(), 0);
	}
}
