package com.epsm.electricPowerSystemModel.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.generation.GeneratorGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationGenerationSchedule;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PowerStationGenerationScheduleJsonSerializer 
		extends JsonSerializer<PowerStationGenerationSchedule>{
	
	private GeneratorGenerationSchedule generatorSchedule;
	private int generatorQuantity;
	private Logger logger = LoggerFactory.getLogger(
			PowerStationGenerationScheduleJsonSerializer.class);
	
	@Override
	public void serialize(PowerStationGenerationSchedule schedule, JsonGenerator jGenerator,
			SerializerProvider provider) throws IOException {
		
		generatorQuantity = schedule.getQuantityOfGenerators();
		
		jGenerator.writeStartObject();
		jGenerator.writeNumberField("powerObjectId", schedule.getPowerObjectId());
		//toString because LocalDateTime fields serializes in random order that brakes test
		jGenerator.writeStringField("realTimeStamp", schedule.getRealTimeStamp().toString());
		jGenerator.writeNumberField("simulationTimeStamp", schedule.getSimulationTimeStamp()
				.toNanoOfDay());
		jGenerator.writeNumberField("generatorQuantity", generatorQuantity);
		jGenerator.writeObjectFieldStart("generators");
		
		for(Integer generatorNumber: schedule.getGeneratorsNumbers()){
			generatorSchedule = schedule.getGeneratorSchedule(generatorNumber);
			jGenerator.writeObjectField(generatorNumber.toString(), generatorSchedule);
		}
		
		jGenerator.writeEndObject();
		jGenerator.writeEndObject();
		
		logger.debug("{} serialized to JSON.", schedule);
	}
}
