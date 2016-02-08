package com.epsm.epsmCore.model.json;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.consumption.ConsumerParametersStub;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConsumerParametersStubDeserializationTest {
	private ObjectMapper mapper;
	private String source;
	private ConsumerParametersStub parameters;
	private final long POWER_OBJECT_ID = 88;
	private final LocalDateTime REALTIME_STAMP = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.of(7, 6, 5, 4, 3, 2, 1);
	
	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		
		source = "{\"powerObjectId\":88,\"realTimeStamp\":[1,2,3,4,5,6,7],"
				+ "\"simulationTimeStamp\":[7,6,5,4,3,2,1]}";
		
		mapper.findAndRegisterModules();
		parameters = mapper.readValue(source, ConsumerParametersStub.class);
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
}
