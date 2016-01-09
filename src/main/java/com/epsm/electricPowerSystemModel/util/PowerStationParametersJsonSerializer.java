package com.epsm.electricPowerSystemModel.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.generation.GeneratorParameters;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationParameters;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PowerStationParametersJsonSerializer extends JsonSerializer<PowerStationParameters>{
	private int generatorQuantity;
	private GeneratorParameters generatorParameters;
	private Logger logger = LoggerFactory.getLogger(PowerStationParametersJsonSerializer.class);
	
	@Override
	public void serialize(PowerStationParameters parameters, JsonGenerator jGenerator,
			SerializerProvider provider) throws IOException {
		
		generatorQuantity = parameters.getQuantityOfGenerators();
		
		jGenerator.writeStartObject();
		jGenerator.writeNumberField("powerObjectId", parameters.getPowerObjectId());
		//toString because LocalDateTime fields serializes in random order that brakes test
		jGenerator.writeStringField("realTimeStamp", parameters.getRealTimeStamp().toString());
		jGenerator.writeNumberField("simulationTimeStamp", parameters.getSimulationTimeStamp()
				.toNanoOfDay());
		jGenerator.writeNumberField("generatorQuantity", generatorQuantity);
		jGenerator.writeObjectFieldStart("generators");
		
		for(Integer generatorNumber: parameters.getGeneratorParametersNumbers()){
			generatorParameters = parameters.getGeneratorParameters(generatorNumber);
			jGenerator.writeObjectField(generatorNumber.toString(), generatorParameters);
		}
		
		jGenerator.writeEndObject();
		jGenerator.writeEndObject();
		
		logger.debug("{} serialized to JSON.", parameters);
	}
}
