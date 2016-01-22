package com.epsm.epsmCore.model.utils.json;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.generation.GeneratorParameters;
import com.epsm.epsmCore.model.generation.PowerStationParameters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PowerStationParametersJsonSerializerTest {
	private ObjectMapper mapper;
	private PowerStationParameters parameters;
	
	@Before
	public void setUp(){
		mapper = new ObjectMapper();
		
		LocalDateTime realTimeStamp = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
		LocalDateTime simulationTimeStamp = LocalDateTime.of(7, 6, 5, 4, 3, 2, 1);
		parameters = new PowerStationParameters(995, realTimeStamp, simulationTimeStamp, 2);
		GeneratorParameters parameters_1 = new GeneratorParameters(1, 40, 5);
		GeneratorParameters parameters_2 = new GeneratorParameters(2, 100, 25);
			
		parameters.addGeneratorParameters(parameters_1);
		parameters.addGeneratorParameters(parameters_2);
	}
	
	@Test
	public void serializesCorrect() throws JsonProcessingException{
		String expected = 
				"{\"powerObjectId\":995"
				+ ",\"realTimeStamp\":\"0001-02-03T04:05:06.000000007\""
				+ ",\"simulationTimeStamp\":\"0007-06-05T04:03:02.000000001\","
				+ "\"generatorQuantity\":2,"
				+ "\"generators\":{"
				+ "\"1\":{\"nominalPowerInMW\":40.0,\"minimalTechnologyPower\":5.0,"
				+ "\"generatorNumber\":1},"
				+ "\"2\":{\"nominalPowerInMW\":100.0,\"minimalTechnologyPower\":25.0,"
				+ "\"generatorNumber\":2}}}";
		String serialized = mapper.writeValueAsString(parameters);
	
		Assert.assertEquals(expected, serialized);
	}
}
