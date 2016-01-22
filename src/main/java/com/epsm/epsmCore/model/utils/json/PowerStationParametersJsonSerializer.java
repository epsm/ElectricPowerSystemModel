package com.epsm.epsmCore.model.utils.json;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.generation.GeneratorParameters;
import com.epsm.epsmCore.model.generation.PowerStationParameters;
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
		jGenerator.writeStringField("realTimeStamp", parameters.getRealTimeStamp().toString());
		jGenerator.writeStringField("simulationTimeStamp", parameters.getSimulationTimeStamp()
				.toString());
		jGenerator.writeNumberField("generatorQuantity", generatorQuantity);
		jGenerator.writeObjectFieldStart("generators");
		
		for(Integer generatorNumber: parameters.getGeneratorParametersNumbers()){
			generatorParameters = parameters.getGeneratorParameters(generatorNumber);
			jGenerator.writeObjectField(generatorNumber.toString(), generatorParameters);
		}
		
		jGenerator.writeEndObject();
		jGenerator.writeEndObject();
		
		logger.debug("Serialized: {} to JSON.", parameters);
	}
}
