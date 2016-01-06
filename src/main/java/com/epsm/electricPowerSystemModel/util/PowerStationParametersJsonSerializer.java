package com.epsm.electricPowerSystemModel.util;

import java.io.IOException;

import com.epsm.electricPowerSystemModel.model.generation.GeneratorParameters;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationParameters;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PowerStationParametersJsonSerializer extends JsonSerializer<PowerStationParameters>{
	private int generatorQuantity;
	private GeneratorParameters generatorParameters;
	
	@Override
	public void serialize(PowerStationParameters parameters, JsonGenerator jGenerator,
			SerializerProvider provider) throws IOException {
		
		generatorQuantity = parameters.getQuantityOfGenerators();
		
		jGenerator.writeStartObject();
		jGenerator.writeNumberField("powerObjectId", parameters.getPowerObjectId());
		//in string because LocalDateTime fields serializes in random order that brakes test.
		jGenerator.writeStringField("realTimeStamp", parameters.getRealTimeStamp().toString());
		jGenerator.writeObjectField("simulationTimeStamp", parameters.getSimulationTimeStamp());
		jGenerator.writeNumberField("generatorQuantity", generatorQuantity);
		
		for(int generatorNumber: parameters.getGeneratorParametersNumbers()){
			generatorParameters = parameters.getGeneratorParameters(generatorNumber);
			jGenerator.writeObjectField("generatorParameters", generatorParameters);
		}
		
		jGenerator.writeEndObject();
	}
	
}
