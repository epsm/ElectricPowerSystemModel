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
	private GeneratorParameters firstGeneratorParameters;
	private GeneratorParameters secondGeneratorParameters;
	private String source;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		source = "{\"powerObjectId\":995"
				 + ",\"realTimeStamp\":\"0001-02-03T04:05:06.000000007\""
				 + ",\"simulationTimeStamp\":3723000000004,"
				 + "\"generatorQuantity\":2,"
				 + "\"generators\":{"
				 + "\"1\":{\"nominalPowerInMW\":40.0,\"minimalTechnologyPower\":5.0,"
				 + "\"generatorNumber\":1},"
				 + "\"2\":{\"nominalPowerInMW\":100.0,\"minimalTechnologyPower\":25.0,"
				 + "\"generatorNumber\":2}}}";
		
		stationParameters = mapper.readValue(source, PowerStationParameters.class);
		firstGeneratorParameters = stationParameters.getGeneratorParameters(1);
		secondGeneratorParameters = stationParameters.getGeneratorParameters(2);
	}

	@Test
	public void objectIdCorrect(){
		Assert.assertEquals(995, stationParameters.getPowerObjectId());
	}

	@Test
	public void realTimeStampCorrect(){
		Assert.assertEquals(LocalDateTime.of(1, 2, 3, 4, 5, 6, 7), stationParameters
				.getRealTimeStamp());
	}
	
	@Test
	public void simulationTimeStampCorrect(){
		Assert.assertEquals(LocalTime.of(1, 2, 3, 4), stationParameters.getSimulationTimeStamp());
	}
	
	@Test
	public void firstGeneratorNumberCorrect(){
		Assert.assertEquals(1, firstGeneratorParameters.getGeneratorNumber());
	}
	
	@Test
	public void firstGeneratorNominalPowerCorrect(){
		Assert.assertEquals(40, firstGeneratorParameters.getNominalPowerInMW(), 0);
	}
	
	@Test
	public void firstGeneratorMinimalTechnologyPowerCorrect(){
		Assert.assertEquals(5, firstGeneratorParameters.getMinimalTechnologyPower(), 0);
	}
	
	@Test
	public void secondGeneratorNumberCorrect(){
		Assert.assertEquals(2, secondGeneratorParameters.getGeneratorNumber());
	}
	
	@Test
	public void secondGeneratorNominalPowerCorrect(){
		Assert.assertEquals(100, secondGeneratorParameters.getNominalPowerInMW(), 0);
	}
	
	@Test
	public void secondGeneratorMinimalTechnologyPowerCorrect(){
		Assert.assertEquals(25, secondGeneratorParameters.getMinimalTechnologyPower(), 0);
	}
}
