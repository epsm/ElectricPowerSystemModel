package com.epsm.epsmCore.model.utils.json;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.consumption.ConsumerParametersStub;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/*
 * Temporary solution. Desereliztion works perfect with Tomcat8, but 
 * java.lang.NoSuchMethodError on OpenShift WildFly container.
 */
public class ConsumerParametersStubJsonDeserializer extends 
		JsonDeserializer<ConsumerParametersStub>{

	private MessageFieldsDeserializer messageDeserializer = new MessageFieldsDeserializer();
	private ConsumerParametersStub parameters;
	private long powerObjectId;
	private LocalDateTime realTimeStamp;
	private LocalDateTime simulationTimeStamp;
	private Logger logger = LoggerFactory.getLogger(ConsumerParametersStubJsonDeserializer.class);
	
	@Override
	public ConsumerParametersStub deserialize(JsonParser jParser, DeserializationContext ctx)
			throws IOException, JsonProcessingException {
		
		while(jParser.nextToken() != JsonToken.END_OBJECT){
			String name = jParser.getCurrentName();
			
			if("powerObjectId".equals(name)){
				powerObjectId = messageDeserializer.deserializePowerObjectId(jParser);
			}else if("realTimeStamp".equals(name)){
				realTimeStamp = messageDeserializer.deserializeLocalDateTime(jParser);
			}else{
				simulationTimeStamp = messageDeserializer.deserializeLocalDateTime(jParser);
			}
		}
		
		parameters = new ConsumerParametersStub(powerObjectId, realTimeStamp, simulationTimeStamp);		
		
		logger.debug("Deserialized: {} from JSON.", parameters);
		
		return parameters;
	}
}
