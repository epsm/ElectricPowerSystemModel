package com.epsm.electricPowerSystemModel.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.generation.GeneratorParameters;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationParameters;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class PowerStationParametersJsonDeserializer extends 
		JsonDeserializer<PowerStationParameters>{

	private PowerStationParameters stationParameters;
	private GeneratorParameters generatorParameters;
	private int powerObjectId;
	private LocalDateTime realTimeStamp;
	private LocalTime simulationTimeStamp;
	private int generatorQuantity;
	private int generatorNumber;
	private float nominalPowerInMW;
	private float minimalTechnologyPower;
	private JsonParser jParser;
	private JsonNode rootNode;
	private JsonNode generatorsNode;
	private Iterator<JsonNode> iterator;
	private JsonNode generatorNode;
	private Logger logger = LoggerFactory.getLogger(PowerStationParametersJsonDeserializer.class);
	
	@Override
	public PowerStationParameters deserialize(JsonParser jParser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		saveValue(jParser);
		getRootNode();
		getDataForCreatingPowerStationParameters();
		createPowerStationParameters();
		getNodeForCreatingGeneratorsParameters();
		createGeneratorsParameters();
		
		logger.debug("{} deserialized from JSON.", stationParameters);
		
		return stationParameters;
	}
	
	private void saveValue(JsonParser jParser){
		this.jParser = jParser;
	}
	
	private void getRootNode() throws JsonProcessingException, IOException{
		rootNode = jParser.getCodec().readTree(jParser);
	}
	
	private void getDataForCreatingPowerStationParameters(){
		powerObjectId = rootNode.get("powerObjectId").asInt();
		realTimeStamp = LocalDateTime.parse(rootNode.get("realTimeStamp").asText());
		simulationTimeStamp = LocalTime.ofNanoOfDay(rootNode.get("simulationTimeStamp").asLong());
		generatorQuantity = rootNode.get("generatorQuantity").asInt();
	}
	
	private void createPowerStationParameters(){
		stationParameters = new PowerStationParameters(powerObjectId, realTimeStamp,
				simulationTimeStamp, generatorQuantity);
	}
	
	private void getNodeForCreatingGeneratorsParameters(){
		generatorsNode = rootNode.path("generators");
		iterator = generatorsNode.elements();
	}
	
	private void createGeneratorsParameters(){
		while(iterator.hasNext()){
			generatorNode = iterator.next();
			
			getDataForGeneratorParameters();
			createGeneratorParameters();
			addGeneratorParametersToStationParameters();
		}
	}
	
	private void getDataForGeneratorParameters(){
		generatorNumber = generatorNode.get("generatorNumber").asInt();
		nominalPowerInMW = (float) generatorNode.get("nominalPowerInMW").asDouble();
		minimalTechnologyPower = (float) generatorNode.get("minimalTechnologyPower").asDouble();
	}
	
	private void createGeneratorParameters(){
		generatorParameters = new GeneratorParameters(generatorNumber, nominalPowerInMW,
				minimalTechnologyPower);
	}
	
	private void addGeneratorParametersToStationParameters(){
		stationParameters.addGeneratorParameters(generatorParameters);
	}
}
