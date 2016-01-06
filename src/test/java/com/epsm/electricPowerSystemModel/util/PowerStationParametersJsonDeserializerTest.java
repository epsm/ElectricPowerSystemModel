package com.epsm.electricPowerSystemModel.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.electricPowerSystemModel.model.generation.GeneratorParameters;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationParameters;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PowerStationParametersJsonDeserializerTest {
	private ObjectMapper mapper;
	private PowerStationParameters stationParameters;
	private GeneratorParameters generator_1;
	private GeneratorParameters generator_2;
	private String source;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		source = 
				"{\"powerObjectId\":995"
				+ ",\"realTimeStamp\":\"0001-02-03T04:05:06.000000007\""
				+ ",\"simulationTimeStamp\":3723000000004,"
				+ "\"generatorQuantity\":2,"
				+ "\"generators\":{"
				+ "\"1\":{\"nominalPowerInMW\":40.0,\"minimalTechnologyPower\":5.0,"
				+ "\"generatorNumber\":1},"
				+ "\"2\":{\"nominalPowerInMW\":100.0,\"minimalTechnologyPower\":25.0,"
				+ "\"generatorNumber\":2}}}";
		
		stationParameters = mapper.readValue(source, PowerStationParameters.class);
		generator_1 = stationParameters.getGeneratorParameters(1);
		generator_2 = stationParameters.getGeneratorParameters(2);
	}

	@Test
	public void deserializedGeneralDataCorrect() throws IOException{
		Assert.assertEquals(995, stationParameters.getPowerObjectId());
		Assert.assertNotNull(stationParameters.getGeneratorParameters(1));
		Assert.assertNotNull(stationParameters.getGeneratorParameters(2));
		Assert.assertEquals(LocalDateTime.of(1, 2, 3, 4, 5, 6, 7), stationParameters
				.getRealTimeStamp());
		Assert.assertEquals(LocalTime.of(1, 2, 3, 4), stationParameters.getSimulationTimeStamp());
	}
	
	@Test
	public void deserializedDataForFirstGeneratorCorrect() throws IOException{
		Assert.assertEquals(1, generator_1.getGeneratorNumber());
		Assert.assertEquals(40, generator_1.getNominalPowerInMW(), 0);
		Assert.assertEquals(5, generator_1.getMinimalTechnologyPower(), 0);
	}
	
	@Test
	public void deserializedDataForSecondGeneratorCorrect() throws IOException{
		Assert.assertEquals(2, generator_2.getGeneratorNumber());
		Assert.assertEquals(100, generator_2.getNominalPowerInMW(), 0);
		Assert.assertEquals(25, generator_2.getMinimalTechnologyPower(), 0);
	}
}
