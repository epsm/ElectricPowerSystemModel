package com.epsm.epsmCore.model.utils.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.consumption.ConsumerParametersStub;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConsumerParametersStubDeserializerTest {
	private ObjectMapper mapper;
	private String source;
	private ConsumerParametersStub parameters;
	
	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		source = "{\"powerObjectId\":88,\"simulationTimeStamp\":[1,2,3,4],"
				+ "\"realTimeStamp\":[1,2,3,4,5,6,7]}";
		
		mapper.findAndRegisterModules();
		parameters = mapper.readValue(source, ConsumerParametersStub.class);
	}
	
	@Test
	public void powerObjectIdCorrest(){
		Assert.assertEquals(88, parameters.getPowerObjectId());
	}
	
	@Test
	public void realTimeStampCorrest(){
		LocalDateTime expected = LocalDateTime.of(1,2,3,4,5,6,7);
		Assert.assertEquals(expected, parameters.getRealTimeStamp());
	}
	
	@Test
	public void simulationTimeStampCorrest(){
		LocalTime expected = LocalTime.of(1,2,3,4);
		Assert.assertEquals(expected, parameters.getSimulationTimeStamp());
	}
	
	@Test
	public void deserializesIfNotAllFielsdUsedForLocalDateTime()
			throws JsonParseException, JsonMappingException, IOException{
		
		source = "{\"powerObjectId\":88,\"simulationTimeStamp\":[1,2,3,4],"
				+ "\"realTimeStamp\":[1,2,3,4,5]}";
		mapper.readValue(source, ConsumerParametersStub.class);
	}
	
	@Test
	public void deserializesIfNotAllFielsdUsedForLocalTime()
			throws JsonParseException, JsonMappingException, IOException{
		
		source = "{\"powerObjectId\":88,\"simulationTimeStamp\":[1,2],"
				+ "\"realTimeStamp\":[1,2,3,4,5,6,7]}";
		mapper.readValue(source, ConsumerParametersStub.class);
	}
	
	@Test
	public void hren() throws JsonProcessingException{
		LocalTime time = LocalTime.MIDNIGHT;
		
		for(int i = 0; i < 1000; i++){
			System.out.println(mapper.writeValueAsString(time));
			time = time.plusNanos(20000);
		}
	}
}
