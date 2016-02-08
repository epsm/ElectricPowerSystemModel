package com.epsm.epsmCore.model.json;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.generation.GeneratorParameters;
import com.epsm.epsmCore.model.generation.PowerStationParameters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PowerStationParametersJsonSerializationTest {
	private ObjectMapper mapper;
	private PowerStationParameters parameters;
	private final long POWER_OBJECT_ID = 995;
	private final int QUANTITY_OF_GENERATORS = 2;
	private final int FIRST_GENERATOR_NUMBER = 1;
	private final int SECOND_GENERATOR_NUMBER = 2;
	private float FIRST_GENERATOR_NOMINAL_POWER = 40;
	private float FIRST_GENERATOR_MINIMAL_POWER = 5;
	private float SECOND_GENERATOR_NOMINAL_POWER = 100;
	private float SECOND_GENERATOR_MINIMAL_POWER = 25;
	private final LocalDateTime REALTIME_STAMP = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.of(7, 6, 5, 4, 3, 2, 1);
	
	@Before
	public void setUp(){
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		
		parameters = new PowerStationParameters(POWER_OBJECT_ID, REALTIME_STAMP,
				SIMULATION_TIMESTAMP, QUANTITY_OF_GENERATORS);
		
		GeneratorParameters parameters_1 = new GeneratorParameters(FIRST_GENERATOR_NUMBER, 
				FIRST_GENERATOR_NOMINAL_POWER, FIRST_GENERATOR_MINIMAL_POWER);
		
		GeneratorParameters parameters_2 = new GeneratorParameters(SECOND_GENERATOR_NUMBER, 
				SECOND_GENERATOR_NOMINAL_POWER, SECOND_GENERATOR_MINIMAL_POWER);
			
		parameters.addGeneratorParameters(parameters_1);
		parameters.addGeneratorParameters(parameters_2);
	}
	
	@Test
	public void serializesCorrect() throws JsonProcessingException{
		String expected = 
				"{\"powerObjectId\":995,"
				+ "\"realTimeStamp\":[1,2,3,4,5,6,7],"
				+ "\"simulationTimeStamp\":[7,6,5,4,3,2,1],"
				+ "\"quantityOfGenerators\":2,"
				+ "\"generators\":{"
				+ "\"inclusionQuantity\":2,"
				+ "\"inclusions\":{"
				+ "\"1\":{"
				+ "\"generatorNumber\":1,"
				+ "\"nominalPowerInMW\":40.0,"
				+ "\"minimalTechnologyPower\":5.0},"
				+ "\"2\":{"
				+ "\"generatorNumber\":2,"
				+ "\"nominalPowerInMW\":100.0,"
				+ "\"minimalTechnologyPower\":25.0}}}}";
		
		String serialized = mapper.writeValueAsString(parameters);
		
		Assert.assertEquals(expected, serialized);
	}
}
