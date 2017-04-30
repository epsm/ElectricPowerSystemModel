package com.epsm.epsmcore.model.json;

import com.epsm.epsmcore.model.generation.GeneratorParameters;
import com.epsm.epsmcore.model.generation.PowerStationParameters;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

public class PowerStationParametersJsonDeserializationTest {
	private ObjectMapper mapper;
	private PowerStationParameters stationParameters;
	private GeneratorParameters firstGeneratorParameters;
	private GeneratorParameters secondGeneratorParameters;
	private String source;
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
	public void setUp() throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		
		source = "{\"powerObjectId\":995,"
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
						
		stationParameters = mapper.readValue(source, PowerStationParameters.class);
		firstGeneratorParameters = stationParameters.getGeneratorParameters(1);
		secondGeneratorParameters = stationParameters.getGeneratorParameters(2);
	}

	@Test
	public void objectIdCorrect(){
		Assert.assertEquals(POWER_OBJECT_ID, stationParameters.getPowerObjectId());
	}

	@Test
	public void realTimeStampCorrect(){
		Assert.assertEquals(REALTIME_STAMP, stationParameters.getRealTimeStamp());
	}
	
	@Test
	public void simulationTimeStampCorrect(){
		Assert.assertEquals(SIMULATION_TIMESTAMP, stationParameters.getSimulationTimeStamp());
	}
	
	@Test
	public void quantityOfGeneratorsCorrect(){
		Assert.assertEquals(QUANTITY_OF_GENERATORS, stationParameters.getQuantityOfGenerators());
	}
	
	@Test
	public void firstGeneratorNumberCorrect(){
		Assert.assertEquals(FIRST_GENERATOR_NUMBER, firstGeneratorParameters.getGeneratorNumber());
	}
	
	@Test
	public void firstGeneratorNominalPowerCorrect(){
		Assert.assertEquals(FIRST_GENERATOR_NOMINAL_POWER, 
				firstGeneratorParameters.getNominalPowerInMW(), 0);
	}
	
	@Test
	public void firstGeneratorMinimalTechnologyPowerCorrect(){
		Assert.assertEquals(FIRST_GENERATOR_MINIMAL_POWER,
				firstGeneratorParameters.getMinimalTechnologyPower(), 0);
	}
	
	@Test
	public void secondGeneratorNumberCorrect(){
		Assert.assertEquals(SECOND_GENERATOR_NUMBER, secondGeneratorParameters.getGeneratorNumber());
	}
	
	@Test
	public void secondGeneratorNominalPowerCorrect(){
		Assert.assertEquals(SECOND_GENERATOR_NOMINAL_POWER, 
				secondGeneratorParameters.getNominalPowerInMW(), 0);
	}
	
	@Test
	public void secondGeneratorMinimalTechnologyPowerCorrect(){
		Assert.assertEquals(SECOND_GENERATOR_MINIMAL_POWER,
				secondGeneratorParameters.getMinimalTechnologyPower(), 0);
	}
}
