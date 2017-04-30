package com.epsm.epsmcore.model.json;

import com.epsm.epsmCore.model.consumption.ConsumptionPermissionStub;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

public class ConsumptionPermissionStubDeserializationTest {
	private ObjectMapper mapper;
	private String source;
	private ConsumptionPermissionStub permission;
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
		permission = mapper.readValue(source, ConsumptionPermissionStub.class);
	}
	
	@Test
	public void powerObjectIdCorrect() {
		Assert.assertEquals(POWER_OBJECT_ID, permission.getPowerObjectId());
	}
	
	@Test
	public void simulationTimeStampCorrect(){
		Assert.assertEquals(REALTIME_STAMP, permission.getRealTimeStamp());
	}
	
	@Test
	public void realTimeStampCorrect(){
		Assert.assertEquals(SIMULATION_TIMESTAMP, permission.getSimulationTimeStamp());
	}
}
