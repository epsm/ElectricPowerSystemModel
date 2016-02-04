package com.epsm.epsmCore.model.utils.json;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/*
 * Temporary solution. Desereliztion works perfect with Tomcat8, but 
 * java.lang.NoSuchMethodError on OpenShift WildFly container.
 */
public class MessageFieldsDeserializer{
	private int year;
	private int month;
	private int dayOfMonth;
	private int hour;
	private int minute;
	private int second;
	private int nanoOfSecond;
	
	public long deserializePowerObjectId(JsonParser jParser) throws IOException{
		jParser.nextToken();
		
		return jParser.getLongValue();
	}
	
	public LocalDateTime deserializeLocalDateTime(JsonParser jParser) throws IOException{
		resetState();
		
		return parseLocalDateTime(jParser);
	}
	
	private void resetState(){
		second = 0;
		nanoOfSecond = 0;
	}
	
	private LocalDateTime parseLocalDateTime(JsonParser jParser) throws IOException{
		jParser.nextToken();
		jParser.nextToken();
		year = jParser.getIntValue();
		jParser.nextToken();
		month = jParser.getIntValue();
		jParser.nextToken();
		dayOfMonth = jParser.getIntValue();
		jParser.nextToken();
		hour = jParser.getIntValue();
		jParser.nextToken();
		minute = jParser.getIntValue();
		
		if(jParser.nextToken() != JsonToken.END_ARRAY){
			second = jParser.getIntValue();
					
			if(jParser.nextToken() != JsonToken.END_ARRAY){
				nanoOfSecond = jParser.getIntValue();
				jParser.nextToken();
			}
		}
		
		return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond);
	}
}
