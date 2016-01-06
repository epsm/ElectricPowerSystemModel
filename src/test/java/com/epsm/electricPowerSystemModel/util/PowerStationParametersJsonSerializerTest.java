package com.epsm.electricPowerSystemModel.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.generation.GeneratorParameters;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationParameters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PowerStationParametersJsonSerializerTest {
	private ObjectMapper mapper;
	private PowerStationParameters parameters;
	
	@Before
	public void setUp(){
		mapper = new ObjectMapper();
		
		LocalDateTime realTimeStamp = LocalDateTime.MIN;
		LocalTime simulationTimeStamp = LocalTime.MIN;
		parameters = new PowerStationParameters(1, realTimeStamp, simulationTimeStamp, 2);
		GeneratorParameters parameters_1 = new GeneratorParameters(1, 40, 5);
		GeneratorParameters parameters_2 = new GeneratorParameters(2, 100, 25);
			
		parameters.addGeneratorParameters(parameters_1);
		parameters.addGeneratorParameters(parameters_2);
	}
	
	@Test
	public void serializesCorrect() throws JsonProcessingException{
		String expected = "{\"powerObjectId\":1,\"realTimeStamp\":"
			+ "\"-999999999-01-01T00:00\","
			+ "\"simulationTimeStamp\":{\"hour\":0,\"minute\":0,\"second\":0,\"nano\":0},"
			+ "\"generatorQuantity\":2,"
			+ "\"generatorParameters\":"
			+ "{\"nominalPowerInMW\":40.0,\"minimalTechnologyPower\":5.0,\"generatorNumber\":1},"
			+ "\"generatorParameters\":"
			+ "{\"nominalPowerInMW\":100.0,\"minimalTechnologyPower\":25.0,\"generatorNumber\":2}}";
		
		String serialized = mapper.writeValueAsString(parameters);
		
		Assert.assertEquals(expected, serialized);
	}
}
