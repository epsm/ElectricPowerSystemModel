package com.epsm.epsmCore.model.utils.json;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.consumption.ConsumerState;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/*
 * Temporary solution. Desereliztion works perfect with Tomcat8, but 
 * java.lang.NoSuchMethodError on OpenShift WildFly container.
 */
public class ConsumerStateJsonDeserializer extends 
		JsonDeserializer<ConsumerState>{

	private MessageFieldsDeserializer messageDeserializer = new MessageFieldsDeserializer();
	private ConsumerState state;
	private long powerObjectId;
	private float load;
	private LocalDateTime realTimeStamp;
	private LocalDateTime simulationTimeStamp;
	private Logger logger = LoggerFactory.getLogger(ConsumerStateJsonDeserializer.class);
	
	@Override
	public ConsumerState deserialize(JsonParser jParser, DeserializationContext ctx)
			throws IOException, JsonProcessingException {
		
		while(jParser.nextToken() != JsonToken.END_OBJECT){
			String name = jParser.getCurrentName();
			
			if("powerObjectId".equals(name)){
				powerObjectId = messageDeserializer.deserializePowerObjectId(jParser);
			}else if("realTimeStamp".equals(name)){
				realTimeStamp = messageDeserializer.deserializeLocalDateTime(jParser);
			}else if("simulationTimeStamp".equals(name)){
				simulationTimeStamp = messageDeserializer.deserializeLocalDateTime(jParser);
			}else{
				jParser.nextToken();
				load = jParser.getFloatValue();
			}
		}
		
		state = new ConsumerState(powerObjectId, realTimeStamp, simulationTimeStamp, load);
		
		logger.debug("Deserialized: {} from JSON.", state);
		
		return state;
	}
}
