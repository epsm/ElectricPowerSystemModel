package com.epsm.epsmCore.model.utils.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
public class ConsumerParametersStubDeserializer extends 
		JsonDeserializer<ConsumerParametersStub>{

	private ConsumerParametersStub parameters;
	private long powerObjectId;
	private int rtsYear;
	private int rtsMonth;
	private int rtsDayOfMonth;
	private int rtsHour;
	private int rtsMinute;
	private int rtsSecond;
	private int rtsNanoOfSecond;
	private int stsHour;
	private int stsMinute;
	private int stsSecond;
	private int stsNanoOfSecond;
	private Logger logger = LoggerFactory.getLogger(ConsumerParametersStubDeserializer.class);
	
	@Override
	public ConsumerParametersStub deserialize(JsonParser jParser, DeserializationContext ctx)
			throws IOException, JsonProcessingException {
		
		while(jParser.nextToken() != JsonToken.END_OBJECT){
			String name = jParser.getCurrentName();
			
			if("powerObjectId".equals(name)){
				jParser.nextToken();
				powerObjectId = jParser.getLongValue();
			}else if("realTimeStamp".equals(name)){
				jParser.nextToken();
				jParser.nextToken();
				rtsYear = jParser.getIntValue();
				jParser.nextToken();
				rtsMonth = jParser.getIntValue();
				jParser.nextToken();
				rtsDayOfMonth = jParser.getIntValue();
				jParser.nextToken();
				rtsHour = jParser.getIntValue();
				jParser.nextToken();
				rtsMinute = jParser.getIntValue();
				
				if(jParser.nextToken() != JsonToken.END_ARRAY){
					rtsSecond = jParser.getIntValue();
					jParser.nextToken();
					rtsNanoOfSecond = jParser.getIntValue();
					jParser.nextToken();
				}
				
			}else{
				jParser.nextToken();
				jParser.nextToken();
				stsHour = jParser.getIntValue();
				jParser.nextToken();
				stsMinute = jParser.getIntValue();
				
				if(jParser.nextToken() != JsonToken.END_ARRAY){
					stsSecond = jParser.getIntValue();
					jParser.nextToken();
					stsNanoOfSecond = jParser.getIntValue();
					jParser.nextToken();
				}
			}
		}
		
		parameters = new ConsumerParametersStub(powerObjectId, LocalDateTime.of(
				rtsYear, rtsMonth, rtsDayOfMonth, rtsHour, rtsMinute, rtsSecond, rtsNanoOfSecond),
				LocalTime.of(stsHour, stsMinute, stsSecond, stsNanoOfSecond));
		
		logger.debug("Deserialized: {} from JSON.", parameters);
		
		return parameters;
	}
}
