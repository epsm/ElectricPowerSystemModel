package com.epsm.epsmcore.model.json;

import com.epsm.epsmCore.model.consumption.ConsumptionPermissionStub;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

public class ConsumptionPermissionStubSerializationTest {
	private ObjectMapper mapper;
	private ConsumptionPermissionStub permission;
	private final long POWER_OBJECT_ID = 88;
	private final LocalDateTime REALTIME_STAMP = LocalDateTime.of(1, 2, 3, 4, 5, 6, 7);
	private final LocalDateTime SIMULATION_TIMESTAMP = LocalDateTime.of(7, 6, 5, 4, 3, 2, 1);
	
	@Before
	public void setUp(){
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		
		permission = new ConsumptionPermissionStub(POWER_OBJECT_ID, REALTIME_STAMP, 
				SIMULATION_TIMESTAMP);
	}
	
	@Test
	public void serializesCorrect() throws JsonProcessingException{
		String expected = "{\"powerObjectId\":88,\"realTimeStamp\":[1,2,3,4,5,6,7],"
						+ "\"simulationTimeStamp\":[7,6,5,4,3,2,1]}";
		
		String serialized = mapper.writeValueAsString(permission);
		
		Assert.assertEquals(expected, serialized);
	}
}
