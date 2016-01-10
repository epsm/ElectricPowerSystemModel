package com.epsm.electricPowerSystemModel.util;

import java.io.IOException;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LoadCurveJsonSerializer extends JsonSerializer<LoadCurve>{
	private LocalTime pointer = LocalTime.MIDNIGHT;
	private Logger logger = LoggerFactory.getLogger(LoadCurveJsonSerializer.class);
	
	@Override
	public void serialize(LoadCurve curve, JsonGenerator jGenerator,
			SerializerProvider provider) throws IOException {
		
		jGenerator.writeStartObject();
		jGenerator.writeArrayFieldStart("loadByHoursInMW");
		
		do{
			jGenerator.writeNumber(curve.getPowerOnTimeInMW(pointer));
			pointer = pointer.plusHours(1);
		}while(pointer != LocalTime.MIDNIGHT);
		
		jGenerator.writeEndArray();
		jGenerator.writeEndObject();
		
		logger.debug("{} serialized to JSON.", curve);
	}
}
