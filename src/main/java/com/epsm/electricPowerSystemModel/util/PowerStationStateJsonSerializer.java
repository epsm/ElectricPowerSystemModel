package com.epsm.electricPowerSystemModel.util;

import java.io.IOException;

import com.epsm.electricPowerSystemModel.model.generation.GeneratorState;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationState;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PowerStationStateJsonSerializer extends JsonSerializer<PowerStationState>{
	private GeneratorState generatorState;
	private int generatorQuantity;
	
	@Override
	public void serialize(PowerStationState state, JsonGenerator jGenerator,
			SerializerProvider provider) throws IOException {
		
		generatorQuantity = state.getQuantityOfGenerators();
		
		jGenerator.writeStartObject();
		jGenerator.writeNumberField("powerObjectId", state.getPowerObjectId());
		//toString because LocalDateTime fields serializes in random order that brakes test
		jGenerator.writeStringField("realTimeStamp", state.getRealTimeStamp().toString());
		jGenerator.writeNumberField("simulationTimeStamp", state.getSimulationTimeStamp()
				.toNanoOfDay());
		jGenerator.writeNumberField("generatorQuantity", generatorQuantity);
		jGenerator.writeNumberField("frequency", state.getFrequency());
		jGenerator.writeObjectFieldStart("generators");
		
		for(Integer generatorNumber: state.getGeneratorsNumbers()){
			generatorState = state.getGeneratorState(generatorNumber);
			jGenerator.writeObjectField(generatorNumber.toString(), generatorState);
		}
		
		jGenerator.writeEndObject();
		jGenerator.writeEndObject();
	}
}
