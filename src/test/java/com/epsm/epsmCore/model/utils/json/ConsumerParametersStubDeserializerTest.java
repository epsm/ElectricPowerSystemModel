package com.epsm.epsmCore.model.utils.json;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.consumption.ConsumerParametersStub;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConsumerParametersStubDeserializerTest {
	private ObjectMapper mapper;
	private String source;
	private ConsumerParametersStub parameters;
	
	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		source = "{\"powerObjectId\":88,\"simulationTimeStamp\":[7,6,5,4,3,2,1],"
				+ "\"realTimeStamp\":[1,2,3,4,5,6,7]}";
		
		mapper.findAndRegisterModules();
		parameters = mapper.readValue(source, ConsumerParametersStub.class);
	}
	
	@Test
	public void powerObjectIdCorrect() {
		Assert.assertEquals(88, parameters.getPowerObjectId());
	}
	
	@Test
	public void simulationTimeStampCorrect(){
		LocalDateTime expected = LocalDateTime.of(7,6,5,4,3,2,1);
		
		Assert.assertEquals(expected, parameters.getSimulationTimeStamp());
	}
	
	@Test
	public void realTimeStampCorrect(){
		LocalDateTime expected = LocalDateTime.of(1,2,3,4,5,6,7);
		
		Assert.assertEquals(expected, parameters.getRealTimeStamp());
	}
}
