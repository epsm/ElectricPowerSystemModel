package com.epsm.electricPowerSystemModel.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.LoadCurve;
import com.epsm.electricPowerSystemModel.model.generation.GeneratorGenerationSchedule;
import com.epsm.electricPowerSystemModel.model.generation.PowerStationGenerationSchedule;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class PowerStationGenerationScheduleJsonDeserializer extends 
		JsonDeserializer<PowerStationGenerationSchedule>{

	private PowerStationGenerationSchedule stationSchedule;
	private GeneratorGenerationSchedule generatorSchedule;
	private int powerObjectId;
	private LocalDateTime realTimeStamp;
	private LocalTime simulationTimeStamp;
	private int generatorQuantity;
	private int generatorNumber;
	private boolean generatorTurnedOn;
	private boolean astaticRegulatorTurnedOn;
	private JsonParser jParser;
	private JsonNode rootNode;
	private JsonNode allGeneratorsNode;
	private JsonNode curveNode;
	private JsonNode loadByHoursInMWNode;
	private Iterator<JsonNode> generatorsIterator;
	private JsonNode generatorNode;
	private LoadCurve generationCurve;
	private Logger logger = LoggerFactory.getLogger(PowerStationGenerationScheduleJsonDeserializer.class);
	
	@Override
	public PowerStationGenerationSchedule deserialize(JsonParser jParser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		saveValue(jParser);
		getRootNode();
		getDataForCreatingStationGenerationSchedule();
		createPowerStationGenerationSchedule();
		getNodeForCreatingGeneratorsSchedules();
		createAndFillOutGeneratorsSchedules();
		
		logger.debug("{} deserialized from JSON.", stationSchedule);
		
		return stationSchedule;
	}
	
	private void saveValue(JsonParser jParser){
		this.jParser = jParser;
	}
	
	private void getRootNode() throws JsonProcessingException, IOException{
		rootNode = jParser.getCodec().readTree(jParser);
	}
	
	private void getDataForCreatingStationGenerationSchedule(){
		powerObjectId = rootNode.get("powerObjectId").asInt();
		realTimeStamp = LocalDateTime.parse(rootNode.get("realTimeStamp").asText());
		simulationTimeStamp = LocalTime.ofNanoOfDay(rootNode.get("simulationTimeStamp").asLong());
		generatorQuantity = rootNode.get("generatorQuantity").asInt();
	}
	
	private void createPowerStationGenerationSchedule(){
		stationSchedule = new PowerStationGenerationSchedule(powerObjectId, realTimeStamp,
				simulationTimeStamp, generatorQuantity);
	}
	
	private void getNodeForCreatingGeneratorsSchedules(){
		allGeneratorsNode = rootNode.path("generators");
		generatorsIterator = allGeneratorsNode.elements();
	}
	
	private void createAndFillOutGeneratorsSchedules(){
		while(generatorsIterator.hasNext()){
			generatorNode = generatorsIterator.next();
			
			getDataForGeneratorSchedule();
			createGeneratorSchedule();
			addGeneratorScheduleToPowerStationSchedule();
		}
	}
	
	private void getDataForGeneratorSchedule(){
		getPrimiteveFields();
		getCurve();
	}
	
	private void getPrimiteveFields(){
		generatorNumber = generatorNode.get("generatorNumber").asInt();
		generatorTurnedOn = generatorNode.get("generatorTurnedOn").asBoolean();
		astaticRegulatorTurnedOn = generatorNode.get("astaticRegulatorTurnedOn").asBoolean();
	}
	
	private void getCurve(){
		getNodeForCreatingCurve();
		
		if(isThereValues()){
			createAndFillCurve();
		}
	}
	
	private void getNodeForCreatingCurve(){
		curveNode = generatorNode.path("curve");
		loadByHoursInMWNode = curveNode.path("loadByHoursInMW");
	}
	
	private boolean isThereValues(){
		return loadByHoursInMWNode.isArray();
	}
	
	private void createAndFillCurve(){
		float[] loadByHoursInMW = new float[24];
		int hour = 0;
		
		for(JsonNode loadNode: loadByHoursInMWNode){
			float load = (float) loadNode.asDouble();
			loadByHoursInMW[hour++] = load;
		}
		
		generationCurve = new LoadCurve(loadByHoursInMW);
	}
	
	private void createGeneratorSchedule(){
		generatorSchedule = new GeneratorGenerationSchedule(generatorNumber,
				generatorTurnedOn, astaticRegulatorTurnedOn, generationCurve);
	}
	
	private void addGeneratorScheduleToPowerStationSchedule(){
		stationSchedule.addGeneratorSchedule(generatorSchedule);
	}
}
