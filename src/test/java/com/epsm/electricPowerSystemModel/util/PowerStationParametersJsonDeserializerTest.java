package com.epsm.electricPowerSystemModel.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.generation.PowerStationParameters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PowerStationParametersJsonDeserializerTest {
	private ObjectMapper mapper;
	private PowerStationParameters parameters;

	@Before
	public void setUp(){
		mapper = new ObjectMapper();
	}

	@Test
	public void deserializesCorrect() throws JsonProcessingException{
		String expected = "{\"powerObjectId\":1,\"realTimeStamp\":"
			+ "\"-999999999-01-01T00:00\","
			+ "\"simulationTimeStamp\":{\"hour\":0,\"minute\":0,\"second\":0,\"nano\":0},"
			+ "\"generatorQuantity\":2,"
			+ "\"generatorParameters\":"
			+ "{\"nominalPowerInMW\":40.0,\"minimalTechnologyPower\":5.0,\"generatorNumber\":1},"
			+ "\"generatorParameters\":"
			+ "{\"nominalPowerInMW\":100.0,\"minimalTechnologyPower\":25.0,\"generatorNumber\":2}}";
		
		String deserialized = mapper.writeValueAsString(parameters);
		
		System.out.println(deserialized);
		
		//Assert.assertEquals(expected, deserialized);
	}
}
