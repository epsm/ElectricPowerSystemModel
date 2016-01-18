package com.epsm.epsmCore.model.utils.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.epsm.epsmCore.model.consumption.ConsumerParametersStub;
import com.epsm.epsmCore.model.consumption.ConsumerState;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Temporary solution. Desereliztion works perfect with Tomcat8, but 
 * java.lang.NoSuchMethodError on OpenShift WildFly container.
 */
public class ConsumerStateDeserializerTest {
	private ObjectMapper mapper;
	private String source;
	private ConsumerState state;
	
	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException{
		mapper = new ObjectMapper();
		source = "{\"powerObjectId\":855,\"realTimeStamp\":[1,2,3,4,5,6,7],"
				+ "\"simulationTimeStamp\":[1,2,3,4],\"load\":55.5}";
		
		mapper.findAndRegisterModules();
		state = mapper.readValue(source, ConsumerState.class);
	}
	
	@Test
	public void powerObjectIdCorrest(){
		Assert.assertEquals(855, state.getPowerObjectId());
	}
	
	@Test
	public void floadCorrest(){
		Assert.assertEquals(55.5f, state.getLoad(), 0);
	}
	
	@Test
	public void realTimeStampCorrest(){
		LocalDateTime expected = LocalDateTime.of(1,2,3,4,5,6,7);
		Assert.assertEquals(expected, state.getRealTimeStamp());
	}
	
	@Test
	public void simulationTimeStampCorrest(){
		LocalTime expected = LocalTime.of(1,2,3,4);
		Assert.assertEquals(expected, state.getSimulationTimeStamp());
	}
	
	@Test
	public void deserializesIfNotAllFielsdUsedForLocalDateTime()
			throws JsonParseException, JsonMappingException, IOException{
		
		source = "{\"powerObjectId\":855,\"simulationTimeStamp\":[1,2,3,4],"
				+ "\"realTimeStamp\":[1,2,3,4,5],\"load\":55.5}";
		mapper.readValue(source, ConsumerState.class);
	}
	
	@Test
	public void deserializesIfNotAllFielsdUsedForLocalTime()
			throws JsonParseException, JsonMappingException, IOException{
		
		source = "{\"powerObjectId\":855,\"simulationTimeStamp\":[1,2],"
				+ "\"realTimeStamp\":[1,2,3,4,5,6,7],\"load\":55.5}";
		mapper.readValue(source, ConsumerState.class);
	}
}
