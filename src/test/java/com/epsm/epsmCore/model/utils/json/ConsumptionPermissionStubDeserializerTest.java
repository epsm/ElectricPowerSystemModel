package com.epsm.epsmCore.model.utils.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.consumption.ConsumptionPermissionStub;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConsumptionPermissionStubDeserializerTest {
	private ObjectMapper mapper;
	private String source;
	private ConsumptionPermissionStub permission;
	
	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		source = "{\"powerObjectId\":88,\"simulationTimeStamp\":[1,2,3,4],"
				+ "\"realTimeStamp\":[1,2,3,4,5,6,7]}";
		
		mapper.findAndRegisterModules();
		permission = mapper.readValue(source, ConsumptionPermissionStub.class);
	}
	
	@Test
	public void powerObjectIdCorrest(){
		Assert.assertEquals(88, permission.getPowerObjectId());
	}
	
	@Test
	public void realTimeStampCorrest(){
		LocalDateTime expected = LocalDateTime.of(1,2,3,4,5,6,7);
		Assert.assertEquals(expected, permission.getRealTimeStamp());
	}
	
	@Test
	public void simulationTimeStampCorrest(){
		LocalTime expected = LocalTime.of(1,2,3,4);
		Assert.assertEquals(expected, permission.getSimulationTimeStamp());
	}
	
	@Test
	public void deserializesLocalDateTimeFiveArgumentsConstructor()
			throws JsonParseException, JsonMappingException, IOException{
		
		source = "{\"powerObjectId\":88,\"simulationTimeStamp\":[1,2,3,4],"
				+ "\"realTimeStamp\":[1,2,3,4,5]}";
		mapper.readValue(source, ConsumptionPermissionStub.class);
	}
	
	@Test
	public void deserializesLocalDateTimeSixArgumentsConstructor()
			throws JsonParseException, JsonMappingException, IOException{
		
		source = "{\"powerObjectId\":88,\"simulationTimeStamp\":[1,2,3,4],"
				+ "\"realTimeStamp\":[1,2,3,4,5,6]}";
		mapper.readValue(source, ConsumptionPermissionStub.class);
	}
	
	@Test
	public void deserializesLocalTimeTwoArgumentsConstructor()
			throws JsonParseException, JsonMappingException, IOException{
		
		source = "{\"powerObjectId\":88,\"simulationTimeStamp\":[1,2],"
				+ "\"realTimeStamp\":[1,2,3,4,5,6,7]}";
		mapper.readValue(source, ConsumptionPermissionStub.class);
	}
	
	@Test
	public void deserializesLocalTimeThreeArgumentsConstructor()
			throws JsonParseException, JsonMappingException, IOException{
		
		source = "{\"powerObjectId\":88,\"simulationTimeStamp\":[1,2,3],"
				+ "\"realTimeStamp\":[1,2,3,4,5,6,7]}";
		mapper.readValue(source, ConsumptionPermissionStub.class);
	}
}
