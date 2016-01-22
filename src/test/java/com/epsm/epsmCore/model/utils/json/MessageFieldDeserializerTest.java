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

/*
 * I tried, but i couldn't test  MessageFieldDeserializer directly, so i made decision to test
 * it through ConsumerParametersStubDeserializer class as it uses all it's functionality.
 */
public class MessageFieldDeserializerTest {
	private ObjectMapper mapper;
	private String source;
	private ConsumerParametersStub parameters;
	
	@Before
	public void setUp(){
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
	}
	
	@Test
	public void powerObjectIdCorrect() throws JsonParseException,
			JsonMappingException, IOException{
		
		source = "{\"powerObjectId\":88,\"simulationTimeStamp\":[1,1,1,1,1,1,1],"
				+ "\"realTimeStamp\":[1,2,3,4,5,6,7]}";
		
		parameters = mapper.readValue(source, ConsumerParametersStub.class);
		
		Assert.assertEquals(88, parameters.getPowerObjectId());
	}
	
	@Test
	public void sevenArgumentsLocalDateTimeOfMethodCorrect() throws JsonParseException,
			JsonMappingException, IOException{
		
		source = "{\"powerObjectId\":88,\"simulationTimeStamp\":[1,1,1,1,1,1,1],"
				+ "\"realTimeStamp\":[1,2,3,4,5,6,7]}";
		LocalDateTime expected = LocalDateTime.of(1,2,3,4,5,6,7);
		
		parameters = mapper.readValue(source, ConsumerParametersStub.class);
		
		Assert.assertEquals(expected, parameters.getRealTimeStamp());
	}
	
	@Test
	public void sixArgumentsLocalDateTimeOfMethodCorrect() throws JsonParseException, 
			JsonMappingException, IOException{
		
		source = "{\"powerObjectId\":88,\"simulationTimeStamp\":[1,1,1,1,1,1,1],"
				+ "\"realTimeStamp\":[1,2,3,4,5,6]}";
		LocalDateTime expected = LocalDateTime.of(1,2,3,4,5,6);
		
		parameters = mapper.readValue(source, ConsumerParametersStub.class);
		
		Assert.assertEquals(expected, parameters.getRealTimeStamp());
	}
	
	@Test
	public void fiveArgumentsLocalDateTimeOfMethodCorrect() throws JsonParseException,
			JsonMappingException, IOException{
		
		source = "{\"powerObjectId\":88,\"simulationTimeStamp\":[1,1,1,1,1,1,1],"
				+ "\"realTimeStamp\":[1,2,3,4,5]}";
		LocalDateTime expected = LocalDateTime.of(1,2,3,4,5);
		
		parameters = mapper.readValue(source, ConsumerParametersStub.class);
		
		Assert.assertEquals(expected, parameters.getRealTimeStamp());
	}
}
