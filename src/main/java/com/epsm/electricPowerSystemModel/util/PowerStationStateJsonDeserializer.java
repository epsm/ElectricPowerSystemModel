package com.epsm.electricPowerSystemModel.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.generation.GeneratorState;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationState;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class PowerStationStateJsonDeserializer extends JsonDeserializer<PowerStationState>{

	private PowerStationState stationState;
	private GeneratorState generatorState;
	private int powerObjectId;
	private LocalDateTime realTimeStamp;
	private LocalTime simulationTimeStamp;
	private float frequency;
	private int generatorQuantity;
	private int generatorNumber;
	private float generationInWM;
	private JsonParser jParser;
	private JsonNode rootNode;
	private JsonNode generatorsNode;
	private Iterator<JsonNode> iterator;
	private JsonNode generatorNode;
	private Logger logger = LoggerFactory.getLogger(PowerStationStateJsonDeserializer.class);
	
	@Override
	public PowerStationState deserialize(JsonParser jParser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		saveValue(jParser);
		getRootNode();
		getDataForCreatingPowerStationState();
		createPowerStationState();
		getNodeForCreatingGeneratorsStates();
		createGeneratorsStates();
		
		logger.debug("{} deserialized from JSON.", stationState);
		
		return stationState;
	}
	
	private void saveValue(JsonParser jParser){
		this.jParser = jParser;
	}
	
	private void getRootNode() throws JsonProcessingException, IOException{
		rootNode = jParser.getCodec().readTree(jParser);
	}
	
	private void getDataForCreatingPowerStationState(){
		powerObjectId = rootNode.get("powerObjectId").asInt();
		realTimeStamp = LocalDateTime.parse(rootNode.get("realTimeStamp").asText());
		simulationTimeStamp = LocalTime.ofNanoOfDay(rootNode.get("simulationTimeStamp").asLong());
		generatorQuantity = rootNode.get("generatorQuantity").asInt();
		frequency = (float) rootNode.get("frequency").asDouble();
	}
	
	private void createPowerStationState(){
		stationState = new PowerStationState(powerObjectId, realTimeStamp,
				simulationTimeStamp, generatorQuantity, frequency);
	}
	
	private void getNodeForCreatingGeneratorsStates(){
		generatorsNode = rootNode.path("generators");
		iterator = generatorsNode.elements();
	}
	
	private void createGeneratorsStates(){
		while(iterator.hasNext()){
			generatorNode = iterator.next();
			
			getDataForGeneratorState();
			createGeneratorState();
			addGeneratorStateToStationState();
		}
	}
	
	private void getDataForGeneratorState(){
		generatorNumber = generatorNode.get("generatorNumber").asInt();
		generationInWM = (float) generatorNode.get("generationInWM").asDouble();
	}
	
	private void createGeneratorState(){
		generatorState = new GeneratorState(generatorNumber, generationInWM);
	}
	
	private void addGeneratorStateToStationState(){
		stationState.addGeneratorState(generatorState);
	}
}
