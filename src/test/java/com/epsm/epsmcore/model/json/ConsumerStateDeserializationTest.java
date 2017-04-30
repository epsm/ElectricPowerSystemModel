package com.epsm.epsmcore.model.json;

import com.epsm.epsmcore.model.consumption.ConsumerState;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

public class ConsumerStateDeserializationTest {
	private ObjectMapper mapper;
	private String source;
	private ConsumerState parameters;
	private final long POWER_OBJECT_ID = 88;
	private final LocalDateTime REALTIME_STAMP = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.of(7, 6, 5, 4, 3, 2, 1);
	private final float LOAD = 64.778f;
	
	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		
		source =  "{\"powerObjectId\":88,\"realTimeStamp\":[1,2,3,4,5,6,7],"
				+ "\"simulationTimeStamp\":[7,6,5,4,3,2,1],\"loadInMW\":64.778}";
		
		mapper.findAndRegisterModules();
		parameters = mapper.readValue(source, ConsumerState.class);
	}
	
	@Test
	public void powerObjectIdCorrect() {
		Assert.assertEquals(POWER_OBJECT_ID, parameters.getPowerObjectId());
	}
	
	@Test
	public void simulationTimeStampCorrect(){
		Assert.assertEquals(REALTIME_STAMP, parameters.getRealTimeStamp());
	}
	
	@Test
	public void realTimeStampCorrect(){
		Assert.assertEquals(SIMULATION_TIMESTAMP, parameters.getSimulationTimeStamp());
	}
	
	@Test
	public void loadCorrect(){
		Assert.assertEquals(LOAD, parameters.getLoadInMW(), 0);
	}
}
